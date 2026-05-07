package com.exchangecalculator.app.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.exchangecalculator.app.presentation.ui.theme.ExchangeCalculatorTheme
import com.exchangecalculator.app.presentation.utils.getEmojiFlag
import com.exchangecalculator.app.presentation.utils.getFlagUrl

@Composable
fun CurrencyFlag(
    currencyCode: String,
    modifier: Modifier = Modifier,
    size: Dp = 30.dp,
    withShadow: Boolean = false
) {
    val flagUrl = getFlagUrl(currencyCode)
    val emoji = getEmojiFlag(currencyCode)
    var imageLoadFailed by remember(currencyCode) { mutableStateOf(false) }

    if (withShadow) {
        Box(
            modifier = modifier
                .size(30.dp)
                .shadow(elevation = 2.dp, shape = CircleShape, clip = false)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            FlagContent(
                flagUrl = flagUrl,
                emoji = emoji,
                size = size,
                imageLoadFailed = imageLoadFailed,
                onError = { imageLoadFailed = true }
            )
        }
    } else {
        FlagContent(
            flagUrl = flagUrl,
            emoji = emoji,
            size = size,
            imageLoadFailed = imageLoadFailed,
            onError = { imageLoadFailed = true },
            modifier = modifier
        )
    }
}

@Composable
private fun FlagContent(
    flagUrl: String,
    emoji: String,
    size: Dp,
    imageLoadFailed: Boolean,
    onError: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (imageLoadFailed) {
        Box(
            modifier = modifier
                .size(size)
                .clip(CircleShape)
                .background(Color(0xFFE5E5EA)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = (size.value * 0.5f).sp,
                textAlign = TextAlign.Center
            )
        }
    } else {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(flagUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(size)
                .clip(CircleShape),
            onState = { state ->
                if (state is AsyncImagePainter.State.Error) {
                    onError()
                }
            }
        )
    }
}

// Previews

@Preview(name = "Flag - Card Style", showBackground = true)
@Composable
private fun CurrencyFlagCardPreview() {
    ExchangeCalculatorTheme {
        Box(
            modifier = Modifier
                .background(Color.White)
                .size(80.dp),
            contentAlignment = Alignment.Center
        ) {
            CurrencyFlag(currencyCode = "MXN", size = 40.dp, withShadow = false)
        }
    }
}

@Preview(name = "Flag - Bottom Sheet Style", showBackground = true)
@Composable
private fun CurrencyFlagBottomSheetPreview() {
    ExchangeCalculatorTheme {
        Box(
            modifier = Modifier
                .background(Color(0xFFF8F8F8))
                .size(80.dp),
            contentAlignment = Alignment.Center
        ) {
            CurrencyFlag(currencyCode = "MXN", size = 32.dp, withShadow = true)
        }
    }
}

@Preview(name = "All Flags", showBackground = true)
@Composable
private fun AllFlagsPreview() {
    ExchangeCalculatorTheme {
        Box(
            modifier = Modifier
                .background(Color(0xFFF8F8F8))
                .size(300.dp)
        ) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.align(Alignment.Center),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                listOf("USDC", "MXN", "ARS", "COP", "EURC", "BRL").forEach { code ->
                    CurrencyFlag(currencyCode = code, size = 36.dp)
                }
            }
        }
    }
}
