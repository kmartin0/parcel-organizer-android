package com.km.parceltracker.api.request

data class RegisterRequestBody(
    val email: String,
    val name: String,
    val password: String
)