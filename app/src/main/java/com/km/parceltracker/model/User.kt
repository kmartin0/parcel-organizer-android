package com.km.parceltracker.model

data class User(
    val id: Long,
    val email: String,
    val userName: String,
    val password: String,
    var authorization: Authorization
)