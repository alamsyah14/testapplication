package com.sehatq.testapplication.core.util

import android.text.InputFilter

class InputFilterUtilities {
    companion object {
        /**
         * Filter OneTextFieldChar
         */
        var filterTextField = InputFilter { source, _, _, _, _, _ ->
            val stringTemp = source.toString()
            stringTemp.forEach { char ->
                val charTemp = char.toString()
                if (!charTemp.validOneTextFieldChar())
                    return@InputFilter stringTemp.replace(charTemp, "")
            }
            null
        }
    }
}

fun CharSequence.validOneTextFieldChar() =
    this.isNotEmpty() && RegexPatterns.TextFieldChar.matches(this)