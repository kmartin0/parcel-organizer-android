package com.km.parcelorganizer.enums

enum class ApiErrorEnum {
    invalid_token,
    invalid_grant,
    INVALID_ARGUMENTS, // 400
    MESSAGE_NOT_READABLE, // 400
    UNAUTHENTICATED, // 401
    PERMISSION_DENIED, // 403
    RESOURCE_NOT_FOUND, // 404
    URI_NOT_FOUND, // 404
    METHOD_NOT_ALLOWED, // 405
    ALREADY_EXISTS, // 409
    UNSUPPORTED_MEDIA_TYPE, // 415
    INTERNAL, // 500
    UNAVAILABLE; // 503
}