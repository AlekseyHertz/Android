package ru.netology.nmedia.model

data class RegistrationState (
    val userAlreadyExists: Boolean = false,
    val error: Boolean = false,
    val loading: Boolean = false
        )