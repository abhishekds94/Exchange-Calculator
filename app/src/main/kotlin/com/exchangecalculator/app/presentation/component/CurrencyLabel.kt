package com.exchangecalculator.app.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exchangecalculator.app.presentation.ui.theme.TextPrimary

@Composable
fun CurrencyLabel(
    flagEmoji: String,
    currencyCode: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        CurrencyFlag(
            currencyCode = currencyCode,
            size = 16.dp,
            withShadow = false
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = currencyCode,
            style = TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        )
    }
}
