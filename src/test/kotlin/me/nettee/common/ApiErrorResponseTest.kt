package me.nettee.common

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.nettee.common.exeption.response.ApiErrorResponse
import org.springframework.http.HttpStatus

enum class ErrorCode(val message: String, val httpStatus: HttpStatus) {
    BOARD_NOT_FOUND("게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND)
}

class ApiErrorResponseTest : FreeSpec({
    // 공통 검증 로직
    fun ApiErrorResponse.shouldBeBoardNotFound() {
        status() shouldBe 404
        code() shouldBe "BOARD_NOT_FOUND"
        message() shouldBe "게시물을 찾을 수 없습니다."
    }

    // 공통 상수
    val status = ErrorCode.BOARD_NOT_FOUND.httpStatus.value()
    val code = ErrorCode.BOARD_NOT_FOUND.name
    val message = ErrorCode.BOARD_NOT_FOUND.message

    // 빌더 전역 변수
    val baseResponseBuilder = ApiErrorResponse.builder()
            .status(status)
            .code(code)
            .message(message)

    "생성자 테스트" - {
        "with null payload" {
            val response = ApiErrorResponse(status, code, message, null)
            response.shouldBeBoardNotFound()
            response.payload().shouldBeNull()
        }

        "with empty payload" {
            val response = ApiErrorResponse(status, code, message, emptyMap<String, Any>())
            response.shouldBeBoardNotFound()
            response.payload().shouldBeNull()
        }

        "with non-empty payload" {
            val payload = mapOf("key" to "value")
            val response = ApiErrorResponse(status, code, message, payload)
            response.shouldBeBoardNotFound()
            response.payload() shouldNotBe null
            response.payload() shouldBe payload
        }
    }

    "빌더 테스트" - {
        "with null payload" {
            val response = baseResponseBuilder.payload(null).build()
            response.shouldBeBoardNotFound()
            response.payload().shouldBeNull()
        }

        "with empty payload" {
            val response = baseResponseBuilder.payload(emptyMap<String, Any>()).build()
            response.shouldBeBoardNotFound()
            response.payload().shouldBeNull()
        }

        "with non-empty payload" {
            val payload = mapOf("key" to "value")
            val response = baseResponseBuilder.payload(payload).build()
            response.shouldBeBoardNotFound()
            response.payload() shouldNotBe null
            response.payload() shouldBe payload
        }
    }
})