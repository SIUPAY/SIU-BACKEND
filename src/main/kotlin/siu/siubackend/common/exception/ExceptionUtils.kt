package siu.siubackend.common.exception

import siu.siubackend.common.exception.domain.BusinessException
import siu.siubackend.common.exception.domain.EntityNotFoundException
import siu.siubackend.common.exception.domain.ErrorCode
import siu.siubackend.common.exception.domain.UnauthorizedException
import siu.siubackend.common.exception.domain.ValidationException

object ExceptionUtils {
    
    fun throwUserNotFound(userId: String? = null): Nothing {
        val message = if (userId != null) {
            "ID '$userId'에 해당하는 사용자를 찾을 수 없습니다"
        } else {
            ErrorCode.USER_NOT_FOUND.message
        }
        throw EntityNotFoundException(ErrorCode.USER_NOT_FOUND.code, message)
    }
    
    fun throwStoreNotFound(storeId: String? = null): Nothing {
        val message = if (storeId != null) {
            "ID '$storeId'에 해당하는 상점을 찾을 수 없습니다"
        } else {
            ErrorCode.STORE_NOT_FOUND.message
        }
        throw EntityNotFoundException(ErrorCode.STORE_NOT_FOUND.code, message)
    }
    
    fun throwMenuNotFound(menuId: String? = null): Nothing {
        val message = if (menuId != null) {
            "ID '$menuId'에 해당하는 메뉴를 찾을 수 없습니다"
        } else {
            ErrorCode.MENU_NOT_FOUND.message
        }
        throw EntityNotFoundException(ErrorCode.MENU_NOT_FOUND.code, message)
    }
    
    fun throwCategoryNotFound(categoryId: String? = null): Nothing {
        val message = if (categoryId != null) {
            "ID '$categoryId'에 해당하는 카테고리를 찾을 수 없습니다"
        } else {
            ErrorCode.CATEGORY_NOT_FOUND.message
        }
        throw EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND.code, message)
    }
    
    fun throwUnauthorized(message: String = ErrorCode.UNAUTHORIZED_ACCESS.message): Nothing {
        throw UnauthorizedException(ErrorCode.UNAUTHORIZED_ACCESS.code, message)
    }
    
    fun throwValidationError(message: String): Nothing {
        throw ValidationException(ErrorCode.VALIDATION_ERROR.code, message)
    }
    
    fun throwBusinessError(code: String, message: String): Nothing {
        throw BusinessException(code, message)
    }
}
