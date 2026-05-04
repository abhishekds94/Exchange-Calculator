package com.exchangecalculator.app.domain.model

data class Currency(
    val code: String,
    val name: String,
    val exchangeRate: Double
) {
    override fun toString(): String = "$code - $name"
}
