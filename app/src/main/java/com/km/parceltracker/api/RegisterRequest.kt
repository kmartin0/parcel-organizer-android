package com.km.parceltracker.api

data class RegisterRequest(
    val email: String,
    val name: String,
    val password: String
)