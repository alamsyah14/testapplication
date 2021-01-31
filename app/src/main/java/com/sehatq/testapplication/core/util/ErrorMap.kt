package com.sehatq.testapplication.core.util

sealed class ErrorEmail {
    object Empty : ErrorEmail()
    object InvalidFormat : ErrorEmail()
}

sealed class ErrorPassword {
    object Empty : ErrorPassword()
    object InvalidFormat : ErrorPassword()
    object MinCharacter : ErrorPassword()
}

sealed class ErrorPhone {
    object StartIdNumber : ErrorPhone()
    object MinCharacter : ErrorPhone()
    object MaxCharacter : ErrorPhone()
    object Empty : ErrorPhone()
    object SamePhoneNumber : ErrorPhone()
}