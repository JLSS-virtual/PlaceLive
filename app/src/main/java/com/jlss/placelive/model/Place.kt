package com.jlss.placelive.model

data class Place(
    val id: Long? = null,
    val name: String,
    val description: String,
    val regionId: Long? = null,
    val ownerId: Long,
    val type: String? = null,
    val tags: List<String> = emptyList()
)
