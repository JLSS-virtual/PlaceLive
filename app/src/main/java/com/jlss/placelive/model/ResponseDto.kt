package com.jlss.placelive.model

data class ResponseDto<T>(
    val success: Boolean,
    val data: T,
    val paginatedDto: PaginatedDto? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null
) {

}
