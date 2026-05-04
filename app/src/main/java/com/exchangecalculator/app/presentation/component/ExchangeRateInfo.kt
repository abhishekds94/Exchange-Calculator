package com.exchangecalculator.app.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.exchangecalculator.app.domain.model.Currency
import java.text.DecimalFormat

@Composable
fun ExchangeRateInfo(
    currency: Currency,
    modifier: Modifier = Modifier
) {
    val formatter = DecimalFormat("#,##0.00##")
    val formattedRate = formatter.format(currency.exchangeRate)

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Exchange Rate",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            val annotatedString = buildAnnotatedString {
                append("1 USDc = ")
                withStyle(
                    style = MaterialTheme.typography.headlineSmall.toSpanStyle()
                ) {
                    append(formattedRate)
                }
                append(" ${currency.code}")
            }

            Text(
                text = annotatedString,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
