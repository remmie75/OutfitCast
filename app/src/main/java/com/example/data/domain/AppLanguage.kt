package com.example.data.domain

enum class AppLanguage(val code: String, val displayName: String, val flagEmoji: String) {
    EN("en", "English", "🇺🇸"),
    DE("de", "Deutsch", "🇩🇪"),
    FR("fr", "Français", "🇫🇷"),
    NL("nl", "Nederlands", "🇳🇱"),
    ES("es", "Español", "🇪🇸");

    companion object {
        val DEFAULT = EN
    }
}
