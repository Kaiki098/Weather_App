package br.com.kaiki.weatherapp.utils

import java.util.Locale

class StringUtils {

    fun capitalize(string: String): String {
        return string.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }
    }

}