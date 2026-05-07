package com.exchangecalculator.app.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.exchangecalculator.app.data.datasource.HardcodedCurrencyDataSource
import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.presentation.ui.theme.ExchangeCalculatorTheme
import com.exchangecalculator.app.presentation.ui.theme.GreenAccent
import com.exchangecalculator.app.presentation.utils.getFlagUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyPickerBottomSheet(
    currencies: List<Currency>,
    selectedCurrency: Currency?,
    onCurrencySelected: (Currency) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFFF8F8F8),
        shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(
                            color = Color(0xFFD1D1D6),
                            shape = RoundedCornerShape(100.dp)
                        )
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Choose currency",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C2C2E),
                    letterSpacing = (-0.6).sp
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF2C2C2E),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(vertical = 8.dp)
            ) {
                currencies.forEach { currency ->
                    CurrencyPickerItem(
                        currency = currency,
                        isSelected = currency.code == selectedCurrency?.code,
                        onSelected = {
                            onCurrencySelected(currency)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CurrencyPickerItem(
    currency: Currency,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelected() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(0xFFF2F2F7),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(getFlagUrl(currency.code))
                        .crossfade(true)
                        .build(),
                    contentDescription = currency.code,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                )
            }

            Column {
                Text(
                    text = currency.code,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C2C2E),
                    letterSpacing = 0.4.sp
                )

                if (!currency.isAvailable) {
                    Text(
                        text = "Rate unavailable",
                        fontSize = 12.sp,
                        color = Color(0xFFFF6B6B)
                    )
                }
            }
        }

        if (!currency.isAvailable) {
        } else if (isSelected) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(GreenAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color.White, CircleShape)
                    .padding(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFD1D1D6), CircleShape)
                        .padding(2.dp)
                        .background(Color.White, CircleShape)
                )
            }
        }
    }
}

// Previews

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Bottom Sheet", showBackground = true)
@Composable
private fun CurrencyPickerBottomSheetPreview() {
    val previewCurrencies = listOf(
        Currency(
            code = "ARS",
            name = "Argentine Peso",
            askRate = 1465.24,
            bidRate = 1458.58,
            flagEmoji = "🇦🇷"
        ),
        Currency(
            code = "EURc",
            name = "Euro",
            askRate = 0.0,
            bidRate = 0.0,
            flagEmoji = "🇪🇺",
            isAvailable = false
        ),
        Currency(
            code = "COP",
            name = "Colombian Peso",
            askRate = 3765.85,
            bidRate = 3725.06,
            flagEmoji = "🇨🇴"
        ),
        Currency(
            code = "MXN",
            name = "Mexican Peso",
            askRate = 17.26,
            bidRate = 17.25,
            flagEmoji = "🇲🇽"
        ),
        Currency(
            code = "BRL",
            name = "Brazilian Real",
            askRate = 4.95,
            bidRate = 4.90,
            flagEmoji = "🇧🇷"
        )
    )

    ExchangeCalculatorTheme(darkTheme = false) {
        CurrencyPickerBottomSheet(
            currencies = previewCurrencies,
            selectedCurrency = previewCurrencies[3],
            onCurrencySelected = {},
            onDismiss = {}
        )
    }
}

@Preview(name = "Item - Selected", showBackground = true)
@Composable
private fun ItemSelectedPreview() {
    ExchangeCalculatorTheme {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
        ) {
            CurrencyPickerItem(
                currency = Currency(
                    code = "MXN",
                    name = "Mexican Peso",
                    askRate = 17.26,
                    bidRate = 17.25,
                    flagEmoji = "🇲🇽"
                ),
                isSelected = true,
                onSelected = {}
            )
        }
    }
}

@Preview(name = "Item - Unselected", showBackground = true)
@Composable
private fun ItemUnselectedPreview() {
    ExchangeCalculatorTheme {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
        ) {
            CurrencyPickerItem(
                currency = Currency(
                    code = "ARS",
                    name = "Argentine Peso",
                    askRate = 1465.24,
                    bidRate = 1458.58,
                    flagEmoji = "🇦🇷"
                ),
                isSelected = false,
                onSelected = {}
            )
        }
    }
}

@Preview(name = "Item - Unavailable", showBackground = true)
@Composable
private fun ItemUnavailablePreview() {
    ExchangeCalculatorTheme {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
        ) {
            CurrencyPickerItem(
                currency = Currency(
                    code = "EURc",
                    name = "Euro",
                    askRate = 0.0,
                    bidRate = 0.0,
                    flagEmoji = "🇪🇺",
                    isAvailable = false
                ),
                isSelected = false,
                onSelected = {}
            )
        }
    }
}
