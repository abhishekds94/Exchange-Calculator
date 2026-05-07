package com.exchangecalculator.app.domain.model

data class Currency(
    val code: String,
    val name: String,
    val askRate: Double,        // USDc → currency rate
    val bidRate: Double,        // currency → USDc rate
    val flagEmoji: String = "",
    val isAvailable: Boolean = true  // false when API returns null (e.g. EUR)
) {
    override fun toString(): String = "$code - $name"
}
