package com.exchangecalculator.app.presentation.utils

fun getEmojiFlag(currencyCode: String): String {
    return when (currencyCode.uppercase()) {
        "USDC", "USDC" -> "🇺🇸"
        "MXN" -> "🇲🇽"
        "ARS" -> "🇦🇷"
        "COP" -> "🇨🇴"
        "EURC", "EUR" -> "🇪🇺"
        "BRL" -> "🇧🇷"
        else -> "🏳️"
    }
}

fun getCountryCode(currencyCode: String): String {
    return when (currencyCode.uppercase()) {
        "USDC", "USD" -> "us"
        "MXN" -> "mx"
        "ARS" -> "ar"
        "COP" -> "co"
        "EURC", "EUR" -> "eu"
        "BRL" -> "br"
        else -> "un"
    }
}

fun getFlagUrl(currencyCode: String): String {
    val countryCode = getCountryCode(currencyCode)
    return "https://flagcdn.com/w80/$countryCode.png"
}