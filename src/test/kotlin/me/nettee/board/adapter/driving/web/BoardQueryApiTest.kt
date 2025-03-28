package me.nettee.board.adapter.driving.web

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.kotest.core.spec.style.FreeSpec
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardDetailResponse
import me.nettee.board.adapter.driving.web.mapper.BoardDtoMapper
import me.nettee.board.application.domain.type.BoardStatus
import me.nettee.board.application.model.BoardQueryModels.BoardSummary
import me.nettee.board.application.model.BoardQueryModels.BoardDetail
import me.nettee.board.application.usecase.BoardReadByStatusesUseCase
import me.nettee.board.application.usecase.BoardReadUseCase
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@WebMvcTest(BoardQueryApi::class)
@ExtendWith(SpringExtension::class)
class BoardQueryApiTest(
    @MockitoBean private val boardReadUseCase: BoardReadUseCase,
    @MockitoBean private val boardReadByStatusesUseCase: BoardReadByStatusesUseCase,
    @MockitoBean private val boardDtoMapper: BoardDtoMapper,
    @Autowired private val mvc: MockMvc
) : FreeSpec({

    lateinit var boardList: List<BoardSummary>

    "[GET]게시판 상세 조회" - {
        val mvcGet = fun(boardId: Long): ResultActionsDsl {
            return mvc.get("/api/v1/boards/$boardId") {
                contentType = MediaType.APPLICATION_JSON
            }
        }

        "[2xx] null 포함된 데이터 조회 시 필드 제외 확인"{
            mvc.get("/api/v1/boards/2")
                .andExpect { status { is2xxSuccessful() } }
                .andDo { print() }
                .andReturn()
        }

        "[2xx] 정상 요청 상태 반환" {
            mvcGet(1L)
                .andExpect { status { is2xxSuccessful() } }
                .andDo { print() }
                .andReturn()
        }

        "[4xx] 비정상 요청 상태 반환" {
            mvcGet(200L)
                .andExpect { status { is4xxClientError() } }
                .andDo { print() }
                .andReturn()
        }
    }

    "[GET] statuses 사용 게시판 목록 조회" - {
        val mvcGet = fun(statuses: Set<BoardStatus>, page: Int): ResultActionsDsl {
            return mvc.get("/api/v1/boards") {
                queryParam("statuses", statuses.joinToString(",") { it.name })
                queryParam("page", page.toString())
                queryParam("size", "10")
                contentType = MediaType.APPLICATION_JSON
            }
        }

        "[2xx] 정상 요청 상태 반환" {
            val statuses = setOf(BoardStatus.ACTIVE, BoardStatus.SUSPENDED)
            mvcGet(statuses, 1)
                .andExpect { status { is2xxSuccessful() } }
                .andDo { print() }
                .andReturn()
        }

        "[4xx] 비정상 요청 상태 반환" {
            val statuses = setOf(BoardStatus.REMOVED, BoardStatus.ACTIVE)
            mvcGet(statuses, 100)
                .andExpect { status { is4xxClientError() } }
                .andDo { print() }
                .andReturn()
        }
    }

    var boardReadSummaryModelPage: (
        List<BoardSummary>,
        Pageable,
        Set<BoardStatus>
    ) -> Page<BoardSummary>

    beforeSpec {
        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule())
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }

        boardReadSummaryModelPage = { boardList, pageable, boardStatus ->
            val filteredBoards = boardList
                .takeIf { it.isNotEmpty() }
                ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)

            val pageContent = filteredBoards.drop(pageable.pageNumber * pageable.pageSize)
                .take(pageable.pageSize)
                .takeIf { it.isNotEmpty() }
                ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)

            PageImpl(pageContent, pageable, filteredBoards.size.toLong())
        }

        val boardDetail = BoardDetail(1L, "title1", "content1", BoardStatus.ACTIVE, Instant.now(), Instant.now())
        val boardDetailWithNull = BoardDetail(2L, null, null, BoardStatus.ACTIVE, Instant.now(), Instant.now())

        // list 반복문 완성 코드 ***
        boardList = (1..14).flatMap {
            listOf(
                BoardSummary(it.toLong(), "title$it", BoardStatus.ACTIVE,  Instant.now(), Instant.now()),
                BoardSummary(it.toLong(), "title$it", BoardStatus.SUSPENDED,  Instant.now(), Instant.now()) ,
                BoardSummary(it.toLong(), null, BoardStatus.SUSPENDED,  Instant.now(), Instant.now())
            )
        }

        val jsonResult = objectMapper.writeValueAsString(boardDetailWithNull)
        val nonNullResponse = objectMapper.readValue(jsonResult, BoardDetail::class.java)

        `when`(boardReadUseCase.getBoard(1L)).thenAnswer { boardDetail }
        `when`(boardReadUseCase.getBoard(2L)).thenAnswer { boardDetailWithNull }
        `when`(boardReadUseCase.getBoard(argThat { it != 1L && it != 2L })).thenThrow(ResponseStatusException(HttpStatus.NOT_FOUND))
        `when`(
            boardReadByStatusesUseCase.findByStatuses(
                any<Set<BoardStatus>>(),
                any<Pageable>()
            )
        ).thenAnswer { invocation ->
            val statuses = invocation.getArgument<Set<BoardStatus>>(0)
            val pageable = invocation.getArgument<Pageable>(1)
            boardReadSummaryModelPage(boardList, pageable, statuses)
        }
        `when`(boardDtoMapper.toDtoDetail(boardDetail)).thenReturn(BoardDetailResponse(boardDetail))
        `when`(boardDtoMapper.toDtoDetail(boardDetailWithNull)).thenReturn(BoardDetailResponse(nonNullResponse))
    }
}) {
    @TestConfiguration
    class JacksonTestConfig {
        @Bean
        fun objectMapper(): ObjectMapper {
            return ObjectMapper()
                .registerModule(JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }

        @Bean
        fun mappingJackson2HttpMessageConverter(objectMapper: ObjectMapper): MappingJackson2HttpMessageConverter {
            return MappingJackson2HttpMessageConverter(objectMapper)
        }
    }
}
