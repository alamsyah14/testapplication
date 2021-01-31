package com.sehatq.testapplication.core.util

import android.util.Patterns

class Validator {
    fun phone(value: CharSequence?): Boolean {
        return value?.let { Patterns.PHONE.matcher(it).matches() } ?: false
    }

    fun cellphone(value: CharSequence?): Boolean {
        return value?.let { RegexPatterns.Cellphone.matches(it) } ?: false
    }

    fun email(value: CharSequence?): Boolean {
        return value?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() } ?: false
    }

    fun password(value: CharSequence?): Boolean {
        return value?.let { it.trim().length >= 6 } ?: false
    }

    fun numeric(value: CharSequence?): Boolean {
        return value?.let { RegexPatterns.Numeric.matches(it) } ?: false
    }

    private fun name(value: CharSequence?): Boolean {
        return value?.let { RegexPatterns.AlphabetWithSpace.matches(it) } ?: false
    }

    fun validateEmail(value: CharSequence, onError : (ErrorEmail) -> Unit = {}) : Boolean {
        return when {
            value.isBlank() -> {
                onError(ErrorEmail.Empty)
                false
            }
            !email(value) -> {
                onError(ErrorEmail.InvalidFormat)
                false
            }
            else -> {
                true
            }
        }
    }

    fun validatePassword(value: CharSequence, onError: (ErrorPassword) -> Unit = {}) : Boolean {
        return when {
            value.isBlank() || value.isEmpty() -> {
                onError(ErrorPassword.Empty)
                false
            }
            value.trim().length <= 6 -> {
                onError(ErrorPassword.MinCharacter)
                false
            }
            !RegexPatterns.AlphaNumeric.matches(value) -> {
                onError(ErrorPassword.InvalidFormat)
                false
            }
            else -> true
        }
    }

}

object RegexPatterns {
    private const val REGEX_ALPHANUMERIC = "[A-Za-z0-9]+"
    private const val REGEX_TEXT_FIELD = "[A-Za-z 0-9,.:;'+()?/\\-]+"
    private const val REGEX_CELLPHONE_NUMBER = "^(^08)(\\d{8,11})\$"
    private const val REGEX_ALPHABET_WITH_SPACE = "[A-Za-z ]+"
    private const val REGEX_NUMERIC = "[0-9]+"

    val AlphaNumeric = REGEX_ALPHANUMERIC.toRegex()
    val TextFieldChar = REGEX_TEXT_FIELD.toRegex()
    val Cellphone = REGEX_CELLPHONE_NUMBER.toRegex()
    val AlphabetWithSpace = REGEX_ALPHABET_WITH_SPACE.toRegex()
    val Numeric = REGEX_NUMERIC.toRegex()
}