package siu.siubackend.common.exception.handler

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import siu.siubackend.common.exception.ExceptionUtils
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.springframework.context.annotation.Profile
import io.swagger.v3.oas.annotations.Hidden

@Hidden
@Profile("local")
@RestController
@RequestMapping("/api/test/exceptions")
class ExceptionTestController {

    @GetMapping("/user-not-found/{userId}")
    fun testUserNotFound(@PathVariable userId: String) {
        ExceptionUtils.throwUserNotFound(userId)
    }

    @GetMapping("/store-not-found/{storeId}")
    fun testStoreNotFound(@PathVariable storeId: String) {
        ExceptionUtils.throwStoreNotFound(storeId)
    }

    @GetMapping("/unauthorized")
    fun testUnauthorized() {
        ExceptionUtils.throwUnauthorized()
    }

    @GetMapping("/validation-error")
    fun testValidationError() {
        ExceptionUtils.throwValidationError("테스트 유효성 검사 오류입니다")
    }

    @GetMapping("/runtime-error")
    fun testRuntimeError() {
        throw RuntimeException("테스트 런타임 에러입니다")
    }

    @GetMapping("/require-test/{value}")
    fun testRequire(@PathVariable value: String) {
        require(value != "invalid") { "value는 'invalid'가 될 수 없습니다" }
        require(value.length > 2) { "value의 길이는 2보다 커야 합니다. 현재 길이: ${value.length}" }
    }

    @GetMapping("/check-test/{number}")
    fun testCheck(@PathVariable number: Int) {
        check(number > 0) { "number는 0보다 커야 합니다. 현재 값: $number" }
        check(number < 100) { "number는 100보다 작아야 합니다. 현재 값: $number" }
    }

    @PostMapping("/json-parse-test")
    fun testJsonParse(@Valid @RequestBody request: JsonParseTestRequest) {
        // 이 메서드는 실행되지 않을 것입니다 - JSON 파싱 에러가 먼저 발생
    }

    @PostMapping("/validation-test")
    fun testValidation(@Valid @RequestBody request: ValidationTestRequest) {
        // 이 메서드는 실행되지 않을 것입니다 - 유효성 검사 에러가 먼저 발생
    }

    data class JsonParseTestRequest(
        val requiredField: String,
        val numberField: Int
    )

    data class ValidationTestRequest(
        @field:NotBlank(message = "이름은 필수입니다")
        val name: String,
        
        @field:Positive(message = "나이는 양수여야 합니다")
        val age: Int
    )
}
