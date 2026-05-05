package com.exchangecalculator.app.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrencyFlag(
    currencyCode: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    val backgroundColor = getCurrencyColor(currencyCode)
    val initials = currencyCode.take(2).uppercase()

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontSize = (size.value * 0.3f).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

fun getCurrencyColor(currencyCode: String): Color {
    return when (currencyCode.uppercase()) {
        "MXN" -> Color(0xFF006847)
        "ARS" -> Color(0xFF74ACDF)
        "COP" -> Color(0xFFFCD116)
        "EURC", "EUR" -> Color(0xFF003399)
        "BRL" -> Color(0xFF009C3B)
        "USD", "USDC" -> Color(0xFF1F4E79)
        else -> Color(0xFF6200EE)
    }
}
