package siu.siubackend.common.exception.domain

enum class ErrorCode(val code: String, val message: String) {
    INVALID_REQUEST_PARAMETER("E001", "잘못된 요청 파라미터입니다"),
    MISSING_REQUEST_PARAMETER("E002", "필수 요청 파라미터가 누락되었습니다"),
    JSON_PARSE_ERROR("E003", "JSON 파싱 에러가 발생했습니다"),
    VALIDATION_ERROR("E004", "유효성 검사 에러가 발생했습니다"),
    ILLEGAL_STATE_ERROR("E409", "현재 상태에서 요청을 처리할 수 없습니다"),
    
    USER_NOT_FOUND("E101", "사용자를 찾을 수 없습니다"),
    STORE_NOT_FOUND("E102", "상점을 찾을 수 없습니다"),
    MENU_NOT_FOUND("E103", "메뉴를 찾을 수 없습니다"),
    CATEGORY_NOT_FOUND("E104", "카테고리를 찾을 수 없습니다"),
    
    UNAUTHORIZED_ACCESS("E201", "인증되지 않은 접근입니다"),
    
    INTERNAL_SERVER_ERROR("E500", "서버 내부 오류가 발생했습니다"),
    DATABASE_ERROR("E501", "데이터베이스 오류가 발생했습니다"),
    EXTERNAL_SERVICE_ERROR("E502", "외부 서비스 호출 중 오류가 발생했습니다")
}
