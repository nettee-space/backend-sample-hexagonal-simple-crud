package me.nettee.board.adapter.driving.web

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.FreeSpec
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardCreateCommand
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardUpdateCommand
import me.nettee.board.adapter.driving.web.mapper.BoardDtoMapper
import me.nettee.board.application.domain.Board
import me.nettee.board.application.domain.type.BoardStatus
import me.nettee.board.application.usecase.BoardCreateUseCase
import me.nettee.board.application.usecase.BoardDeleteUseCase
import me.nettee.board.application.usecase.BoardUpdateUseCase
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(BoardCommandController::class)
@AutoConfigureMockMvc
class BoardCommandControllerTest(
    @Autowired private val mvc: MockMvc,
    @MockitoBean private val boardCreateUseCase : BoardCreateUseCase,
    @MockitoBean private val boardUpdateUseCase : BoardUpdateUseCase,
    @MockitoBean private val boardDeleteUseCase : BoardDeleteUseCase,
    @MockitoBean private val boardDtoMapper : BoardDtoMapper
): FreeSpec({

    "게시판 커맨트 컨트롤러 테스트"-{
        // given
        val objectMapper = ObjectMapper()
        val boardDomain = Board.builder()
            .id(1L).title("테스트게시판").content("테스트게시판내용").status(BoardStatus.ACTIVE).build()
        // when
        `when` (boardDtoMapper.toDomain(any())).thenReturn(boardDomain)

        "게시판 생성"-{
            // given
            val boardCreateResponse = boardDomain
            val mvcPost = fun(request: BoardCreateCommand): ResultActionsDsl {
                return mvc.post("/api/v1/board"){
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }.andDo { print() }
            }
            // when
            `when` (boardCreateUseCase.createBoard(any())).thenReturn(boardCreateResponse)

            "필수값 NotNull 조건 충족으로 2XX 상태 반환"{
                // given
                val createCommand = BoardCreateCommand("테스트게시판","테스트게시판내용")
                // then
                mvcPost(createCommand)
                    .andExpect{
                        status { is2xxSuccessful() }
                        jsonPath("board.id"){value(boardCreateResponse.id)}
                        jsonPath("board.title"){value(boardCreateResponse.title)}
                        jsonPath("board.content"){value(boardCreateResponse.content)}
                    }
            }
            "필수값 NotNul 조건 불충족으로 4xx 상태 반환"-{
                "title Null 4XX 상태 반환"{
                    // given
                    val failCreateCommand = BoardCreateCommand(null,"테스트게시판내용")
                    // then
                    mvcPost(failCreateCommand)
                        .andExpect {
                            status { is4xxClientError() }
                        }
                }
                "content Null 4XX 상태 반환"{
                    // given
                    val failCreateCommand = BoardCreateCommand("테스트게시판",null)
                    // then
                    mvcPost(failCreateCommand)
                        .andExpect {
                            status { is4xxClientError() }
                        }
                }
            }
        }
        "게시판 수정"-{
            // given
            val boardUpdateResponse = Board.builder()
                .id(1L).title("테스트게시판수정").content("테스트게시판내용수정").status(BoardStatus.ACTIVE).build()

            val mvcPut = fun(request: BoardUpdateCommand): ResultActionsDsl {
                return mvc.patch("/api/v1/board/{id}", boardDomain.id){
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }.andDo { print() }
            }
            // when
            `when` (boardUpdateUseCase.updateBoard(any())).thenReturn(boardUpdateResponse)

            "필수값 NotNull 조건 충족으로 2XX 상태 반환"{
                // given
                val updateCommand = BoardUpdateCommand("테스트게시판수정","테스트게시판내용수정")
                // then
                mvcPut(updateCommand)
                    .andExpect{
                        status { is2xxSuccessful() }
                        jsonPath("board.id"){value(boardDomain.id)}
                        jsonPath("board.title"){value(boardUpdateResponse.title)}
                        jsonPath("board.content"){value(boardUpdateResponse.content)}
                    }
            }
            "필수값 NotNul 조건 불충족으로 4xx 상태 반환"-{
                "title Null 4XX 상태 반환" {
                    // given
                    val failUpdateCommand = BoardUpdateCommand(null, "테스트게시판내용수정")
                    // then
                    mvcPut(failUpdateCommand)
                        .andExpect {
                            status { is4xxClientError() }
                        }
                }
                "content Null 4XX 상태 반환" {
                    // given
                    val failUpdateCommand = BoardUpdateCommand("테스트게시판수정", null)
                    // then
                    mvcPut(failUpdateCommand)
                        .andExpect {
                            status { is4xxClientError() }
                        }
                }
            }
        }
        "게시판 삭제"-{
            // given
            val mvcDelete = fun(): ResultActionsDsl {
                return mvc.delete("/api/v1/board/{id}", boardDomain.id){
                    contentType = MediaType.APPLICATION_JSON
                }.andDo { print() }
            }

            "삭제 성공으로 2xx 상태 반환"{
                // when
                doNothing().`when`(boardDeleteUseCase).deleteBoard(any())

                // then
                mvcDelete()
                    .andExpect {
                        status { is2xxSuccessful() }
                    }
            }
        }
    }
})