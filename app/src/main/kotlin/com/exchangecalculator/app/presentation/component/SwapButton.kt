package com.exchangecalculator.app.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exchangecalculator.app.R
import com.exchangecalculator.app.presentation.ui.theme.ExchangeCalculatorTheme
import com.exchangecalculator.app.presentation.ui.theme.GreenAccent

@Composable
fun SwapButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClick() }
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(GreenAccent),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_down_arrow),
                contentDescription = "Swap currencies",
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Preview(name = "Swap Button - Light", showBackground = true)
@Composable
private fun SwapButtonLightPreview() {
    ExchangeCalculatorTheme(darkTheme = false) {
        Surface(
            color = Color(0xFFF8F8F8)
        ) {
            Box(
                modifier = Modifier.padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                SwapButton(onClick = {})
            }
        }
    }
}

@Preview(name = "Swap Button - Dark", showBackground = true)
@Composable
private fun SwapButtonDarkPreview() {
    ExchangeCalculatorTheme(darkTheme = true) {
        Surface(
            color = Color(0xFF1A1C19)
        ) {
            Box(
                modifier = Modifier.padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                SwapButton(onClick = {})
            }
        }
    }
}
