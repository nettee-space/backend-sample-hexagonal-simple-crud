//package me.nettee.board.adapter.driving.web
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import io.kotest.core.spec.style.FreeSpec
//import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardCreateCommand
//import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardUpdateCommand
//import me.nettee.board.adapter.driving.web.mapper.BoardDtoMapper
//import me.nettee.board.application.domain.Board
//import me.nettee.board.application.domain.type.BoardStatus
//import me.nettee.board.application.usecase.BoardCreateUseCase
//import me.nettee.board.application.usecase.BoardDeleteUseCase
//import me.nettee.board.application.usecase.BoardUpdateUseCase
//import org.junit.jupiter.api.extension.ExtendWith
//import org.mockito.ArgumentMatchers.any
//import org.mockito.Mockito.doNothing
//import org.mockito.Mockito.`when`
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.http.HttpMethod
//import org.springframework.http.MediaType
//import org.springframework.test.context.bean.override.mockito.MockitoBean
//import org.springframework.test.context.junit.jupiter.SpringExtension
//import org.springframework.test.web.servlet.*
//
//@ExtendWith(SpringExtension::class)
//@WebMvcTest(BoardCommandApi::class)
//@AutoConfigureMockMvc
//class BoardCommandApiTest(
//    @Autowired private val mvc: MockMvc,
//    @Autowired private val objectMapper: ObjectMapper,
//    @MockitoBean private val boardCreateUseCase: BoardCreateUseCase,
//    @MockitoBean private val boardUpdateUseCase: BoardUpdateUseCase,
//    @MockitoBean private val boardDeleteUseCase: BoardDeleteUseCase,
//    @MockitoBean private val boardDtoMapper: BoardDtoMapper
//) : FreeSpec({
//
//    // given
//    val boardDomain = Board.builder().id(1L).title("테스트게시판").content("테스트게시판내용").status(BoardStatus.ACTIVE).build()
//    val mvcRequest: (HttpMethod, String, Map<String, Any>, Any?) -> ResultActionsDsl =
//        { method, url, pathVariables, requestBody ->
//            mvc.request(method, url, *pathVariables.values.toTypedArray()) {
//                contentType = MediaType.APPLICATION_JSON
//                if (requestBody != null) {
//                    content = objectMapper.writeValueAsString(requestBody)
//                }
//            }
//        }
//
//    "[POST] 게시판 생성 요청" - {
//        // given
//        val boardCreateResponse = boardDomain
//
//        "[정상 요청] 제목과 내용이 3글자 이상과 공백이 아닐 때" - {
//            // when
//            val createCommand = BoardCreateCommand("테스트게시판", "테스트게시판내용")
//
//            "2xx 응답 상태 반환" {
//                // then
//                mvcRequest(HttpMethod.POST, "/api/v1/boards", emptyMap(), createCommand)
//                    .andExpect {
//                        status { is2xxSuccessful() }
//                        jsonPath("board.id") { value(boardCreateResponse.id) }
//                        jsonPath("board.title") { value(boardCreateResponse.title) }
//                        jsonPath("board.content") { value(boardCreateResponse.content) }
//                    }
//                    .andDo { print() }
//                    .andReturn()
//            }
//        }
//
//        "[실패 요청] 제목과 내용이 3글자 미만 일 때" - {
//            // when
//            val failCreateCommand = BoardCreateCommand("테스", "테스")
//
//            // then
//            "4xx 응답 상태 반환" {
//                mvcRequest(HttpMethod.POST, "/api/v1/boards", emptyMap(), failCreateCommand)
//                    .andExpect {
//                        status { is4xxClientError() }
//                    }
//                    .andDo { print() }
//                    .andReturn()
//            }
//        }
//
//        "[실패 요청] 제목과 내용이 공백 일 때" - {
//            // when
//            val failCreateCommand = BoardCreateCommand(null, null)
//
//            // then
//            "4xx 응답 상태 반환" {
//                mvcRequest(HttpMethod.POST, "/api/v1/boards", emptyMap(), failCreateCommand)
//                    .andExpect {
//                        status { is4xxClientError() }
//                    }
//                    .andDo { print() }
//                    .andReturn()
//            }
//        }
//    }
//
//    "[PATCH] 게시판 수정 요청" - {
//        // given
//        val boardUpdateResponse = Board.builder()
//            .id(1L).title("테스트게시판수정").content("테스트게시판내용수정").status(BoardStatus.ACTIVE).build()
//
//        "[정상 요청] 제목과 내용이 3글자 이상과 공백이 아닐 때" - {
//            // when
//            val updateCommand = BoardUpdateCommand("테스트게시판", "테스트게시판내용")
//
//            "2xx 정상 상태 반환" {
//                // then
//                mvcRequest(HttpMethod.PATCH, "/api/v1/boards/{id}", mapOf("id" to boardUpdateResponse.id), updateCommand)
//                    .andExpect {
//                        status { is2xxSuccessful() }
//                        jsonPath("board.id") { value(boardUpdateResponse.id) }
//                        jsonPath("board.title") { value(updateCommand.title) }
//                        jsonPath("board.content") { value(updateCommand.content) }
//                    }
//                    .andDo { print() }
//                    .andReturn()
//            }
//        }
//
//        "[실패 요청] 제목과 내용이 3글자 이하 일 때" - {
//            // when
//            val failUpdateCommand = BoardUpdateCommand("테스", "테스")
//
//            "4xx 응답 상태 반환" {
//                // then
//                mvcRequest(
//                    HttpMethod.PATCH,
//                    "/api/v1/boards/{id}",
//                    mapOf("id" to boardUpdateResponse.id),
//                    failUpdateCommand
//                )
//                    .andExpect {
//                        status { is4xxClientError() }
//                    }
//                    .andDo { print() }
//                    .andReturn()
//            }
//        }
//
//        "[실패 요청] 제목과 내용이 공백 일 때" - {
//            // when
//            val failUpdateCommand = BoardUpdateCommand(null, null)
//
//            "4xx 응답 상태 반환" {
//                // then
//                mvcRequest(
//                    HttpMethod.PATCH,
//                    "/api/v1/boards/{id}",
//                    mapOf("id" to boardUpdateResponse.id),
//                    failUpdateCommand
//                )
//                    .andExpect {
//                        status { is4xxClientError() }
//                    }
//                    .andDo { print() }
//                    .andReturn()
//            }
//        }
//    }
//
//    "[DELETE] 게시판 삭제 요청" - {
//        "[정상 요청] 해당 커뮤니티 게시판 ID가 존재 할 때" - {
//            "2xx 응답 상태 반환" {
//                mvcRequest(HttpMethod.DELETE, "/api/v1/boards/{id}", mapOf("id" to boardDomain.id), null)
//                    .andExpect {
//                        status { is2xxSuccessful() }
//                    }
//            }
//        }
//    }
//
//    beforeSpec {
//        `when`(boardDtoMapper.toDomain(any())).thenReturn(boardDomain)
//        `when`(boardCreateUseCase.createBoard(any())).thenReturn(boardDomain)
//        `when`(boardUpdateUseCase.updateBoard(any())).thenReturn(boardDomain)
//        doNothing().`when`(boardDeleteUseCase).deleteBoard(any())
//    }
//})