package com.jlss.placelive.model

data class ResponseListDto<T>(
    val success: Boolean,
    val data: List<T>? = emptyList(),  // ✅ Default to empty list
    val paginatedDto: PaginatedDto? = null,  // ✅ Nullable, prevents crashes
    val errorCode: String? = null,
    val errorMessage: String? = null
)

data class PaginatedDto(
    val totalElements: Int = 0,  // ✅ Default values
    val page: Int = 0,
    val size: Int = 10
)

