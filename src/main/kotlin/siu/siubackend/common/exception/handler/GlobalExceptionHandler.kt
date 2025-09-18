package siu.siubackend.common.exception.handler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import siu.siubackend.common.exception.domain.BusinessException
import siu.siubackend.common.exception.domain.EntityNotFoundException
import siu.siubackend.common.exception.domain.ErrorCode
import siu.siubackend.common.exception.domain.ErrorResponse
import siu.siubackend.common.exception.domain.SiuException
import siu.siubackend.common.exception.domain.UnauthorizedException
import siu.siubackend.common.exception.domain.ValidationException
import jakarta.validation.ConstraintViolationException
import java.nio.file.AccessDeniedException

@RestControllerAdvice(basePackages = ["siu.siubackend"])
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(SiuException::class)
    fun handleSiuException(
        ex: SiuException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("SiuException occurred: ${ex.message}", ex)
        
        val status = when (ex) {
            is ValidationException -> HttpStatus.BAD_REQUEST
            is EntityNotFoundException -> HttpStatus.NOT_FOUND
            is UnauthorizedException -> HttpStatus.UNAUTHORIZED
            is BusinessException -> HttpStatus.BAD_REQUEST
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        val errorResponse = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            code = ex.errorCode,
            message = ex.message ?: status.reasonPhrase,
            path = getPath(request)
        )

        return ResponseEntity(errorResponse, status)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        ex: HttpMessageNotReadableException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("HttpMessageNotReadableException occurred: ${ex.message}", ex)
        
        val detailedMessage = extractDetailedMessage(ex)
        
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            code = ErrorCode.JSON_PARSE_ERROR.code,
            message = detailedMessage,
            path = getPath(request)
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("MethodArgumentNotValidException occurred: ${ex.message}", ex)
        
        val details = ex.bindingResult.fieldErrors.map { error ->
            "${error.field}: ${error.defaultMessage}"
        }
        val originalMessage = ex.message

        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            code = ErrorCode.VALIDATION_ERROR.code,
            message = originalMessage ?: "요청 데이터가 유효하지 않습니다",
            path = getPath(request),
            details = details
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(
        ex: BindException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val details = ex.bindingResult.fieldErrors.map { error ->
            "${error.field}: ${error.defaultMessage}"
        }
        val originalMessage = ex.message

        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            code = ErrorCode.VALIDATION_ERROR.code,
            message = originalMessage ?: "요청 데이터가 유효하지 않습니다",
            path = getPath(request),
            details = details
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val details = ex.constraintViolations.map { violation ->
            "${violation.propertyPath}: ${violation.message}"
        }
        val originalMessage = ex.message

        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            code = ErrorCode.VALIDATION_ERROR.code,
            message = originalMessage ?: "유효성 검사 오류가 발생했습니다",
            path = getPath(request),
            details = details
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(
        ex: MissingServletRequestParameterException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            code = ErrorCode.MISSING_REQUEST_PARAMETER.code,
            message = ex.message ?: "필수 요청 파라미터가 누락되었습니다",
            path = getPath(request)
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(
        ex: MethodArgumentTypeMismatchException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            code = ErrorCode.INVALID_REQUEST_PARAMETER.code,
            message = ex.message ?: "잘못된 요청 파라미터입니다",
            path = getPath(request)
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(
        ex: HttpRequestMethodNotSupportedException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.METHOD_NOT_ALLOWED.value(),
            error = HttpStatus.METHOD_NOT_ALLOWED.reasonPhrase,
            code = "E405",
            message = ex.message ?: "지원되지 않는 HTTP 메서드입니다",
            path = getPath(request)
        )

        return ResponseEntity(errorResponse, HttpStatus.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected exception occurred: ${ex.message}", ex)
        
        val status = when (ex) {
            is IllegalArgumentException -> HttpStatus.BAD_REQUEST
            is IllegalStateException -> HttpStatus.CONFLICT
            is NoSuchElementException -> HttpStatus.NOT_FOUND
            is AccessDeniedException -> HttpStatus.FORBIDDEN
            is UnsupportedOperationException -> HttpStatus.NOT_IMPLEMENTED
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
        
        val code = when (ex) {
            is IllegalArgumentException -> ErrorCode.INVALID_REQUEST_PARAMETER.code
            is IllegalStateException -> ErrorCode.ILLEGAL_STATE_ERROR.code
            is NoSuchElementException -> "E404"
            is AccessDeniedException -> "E403"
            is UnsupportedOperationException -> "E501"
            else -> ErrorCode.INTERNAL_SERVER_ERROR.code
        }

        val errorResponse = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            code = code,
            message = ex.message ?: "알 수 없는 오류가 발생했습니다",
            path = getPath(request)
        )

        return ResponseEntity(errorResponse, status)
    }

    private fun getPath(request: WebRequest): String {
        return request.getDescription(false).replace("uri=", "")
    }

    private fun extractDetailedMessage(ex: HttpMessageNotReadableException): String {
        val message = ex.message ?: return ErrorCode.JSON_PARSE_ERROR.message
        
        return when {
            message.contains("missing (therefore NULL)") -> {
                val fieldMatch = Regex("for creator parameter (\\w+)").find(message)
                val field = fieldMatch?.groupValues?.get(1)
                if (field != null) {
                    "필수 필드 '$field'가 누락되었습니다"
                } else {
                    "필수 필드가 누락되었습니다"
                }
            }
            message.contains("JSON parse error") -> {
                val rootCauseMatch = Regex("Instantiation of \\[simple type, class ([^\\]]+)\\]").find(message)
                val className = rootCauseMatch?.groupValues?.get(1)?.substringAfterLast('.')
                if (className != null) {
                    "$className 객체 생성 중 JSON 파싱 오류가 발생했습니다"
                } else {
                    "JSON 파싱 오류가 발생했습니다"
                }
            }
            message.contains("not a valid") -> "유효하지 않은 데이터 형식입니다"
            else -> message
        }
    }
}
