package com.km.parceltracker.model

data class User(
    val id: Long,
    val email: String,
    val name: String,
    val password: String,
    var OAuth2Credentials: OAuth2Credentials?
)