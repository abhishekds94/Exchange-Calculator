package com.exchangecalculator.app.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exchangecalculator.app.presentation.ui.theme.CardColor
import com.exchangecalculator.app.presentation.ui.theme.CursorColor
import com.exchangecalculator.app.presentation.ui.theme.TextPrimary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CurrencyInputCard(
    currencyCode: String,
    flagEmoji: String,
    amount: String,
    onAmountChanged: (String) -> Unit,
    isEnabled: Boolean = true,
    showSelector: Boolean = false,
    onSelectorClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    fun formatToDisplay(raw: String): String {
        if (raw.isEmpty()) return ""
        val parts = raw.split(".")
        val intPart = parts[0]
        val decPart = if (parts.size > 1) parts[1] else null
        val formattedInt = if (intPart.isEmpty()) "0" else {
            try {
                NumberFormat.getNumberInstance(Locale.US).format(intPart.toLong())
            } catch (e: Exception) { intPart }
        }
        return buildString {
            append("$")
            append(formattedInt)
            if (raw.contains(".")) {
                append(".")
                if (decPart != null) append(decPart)
            }
        }
    }

    var tfValue by remember(currencyCode, amount) {
        val display = formatToDisplay(amount)
        mutableStateOf(
            TextFieldValue(
                text = display,
                selection = TextRange(display.length)
            )
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.06f),
                spotColor = Color.Black.copy(alpha = 0.06f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = if (showSelector && onSelectorClick != null)
                    Modifier.clickable { onSelectorClick() }
                else
                    Modifier
            ) {
                CurrencyLabel(
                    flagEmoji = flagEmoji,
                    currencyCode = currencyCode
                )
                if (showSelector) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select currency",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            BasicTextField(
                value = tfValue,
                onValueChange = { newTfValue ->
                    if (!isEnabled) return@BasicTextField

                    val stripped = newTfValue.text
                        .replace("$", "")
                        .replace(",", "")

                    val isValid = stripped.isEmpty() ||
                            stripped == "." ||
                            stripped.matches(Regex("^\\d*\\.?\\d*$"))

                    if (!isValid) return@BasicTextField

                    val raw = if (stripped.contains(".")) {
                        val dotIndex = stripped.indexOf(".")
                        val decimals = stripped.substring(dotIndex + 1)
                        if (decimals.length > 2) stripped.substring(0, dotIndex + 3)
                        else stripped
                    } else {
                        stripped
                    }

                    val newDisplay = formatToDisplay(raw)

                    tfValue = TextFieldValue(
                        text = newDisplay,
                        selection = TextRange(newDisplay.length)
                    )

                    onAmountChanged(raw)
                },
                enabled = isEnabled,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.None
                ),
                cursorBrush = SolidColor(CursorColor),
                textStyle = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    textAlign = TextAlign.End
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (tfValue.text.isEmpty()) {
                            Text(
                                text = "$0",
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    textAlign = TextAlign.End
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}
