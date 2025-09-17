package siu.siubackend.common.exception.domain

abstract class SiuException(
    val errorCode: String,
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)

class BusinessException(
    errorCode: String,
    message: String,
    cause: Throwable? = null
) : SiuException(errorCode, message, cause)

class ValidationException(
    errorCode: String,
    message: String,
    cause: Throwable? = null
) : SiuException(errorCode, message, cause)

class EntityNotFoundException(
    errorCode: String,
    message: String,
    cause: Throwable? = null
) : SiuException(errorCode, message, cause)

class UnauthorizedException(
    errorCode: String,
    message: String,
    cause: Throwable? = null
) : SiuException(errorCode, message, cause)
