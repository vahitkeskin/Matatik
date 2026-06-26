package com.vahitkeskin.matatik.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vahitkeskin.matatik.ui.theme.MatatikColors
import com.vahitkeskin.matatik.ui.theme.MatatikTheme
import com.vahitkeskin.matatik.ui.theme.ThemeMode

/** Degrade dolgulu, cam parlamalı birincil aksiyon butonu. */
@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 28.dp, vertical = 14.dp)
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .alpha(if (enabled) 1f else 0.45f)
            .drawBehind {
                drawRoundRect(
                    brush = Brush.linearGradient(
                        listOf(MatatikColors.Violet, MatatikColors.Blue)
                    )
                )
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.30f), Color.Transparent),
                        endY = size.height * 0.5f
                    )
                )
            }
            .clickable(enabled = enabled, onClick = onClick)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Composable
private fun GlassButtonLightPreview() {
    MatatikTheme(ThemeMode.LIGHT) {
        Box(Modifier.padding(16.dp)) { GlassButton("Çöz", {}) }
    }
}

@Preview
@Composable
private fun GlassButtonDarkPreview() {
    MatatikTheme(ThemeMode.DARK) {
        Box(Modifier.padding(16.dp)) { GlassButton("Çöz", {}) }
    }
}
