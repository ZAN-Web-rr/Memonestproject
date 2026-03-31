package com.memonest.app.auth

data class UserSession(
    val ownerId: String,
    val displayName: String,
    val email: String? = null,
    val isGuest: Boolean = true
)
