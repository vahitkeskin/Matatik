package com.vahitkeskin.matatik.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vahitkeskin.matatik.ui.theme.LocalIsDarkTheme
import com.vahitkeskin.matatik.ui.theme.AppColors
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.abs

/**
 * iOS tarzı dikey **tekerlek (wheel) seçici**. Öğeler dikey kayar; ekranın tam ortasındaki öğe
 * seçili kabul edilir ([GlassyCard] cam bandıyla vurgulanır). Snap davranışı sayesinde bırakınca
 * en yakın öğeye oturur. Ortadaki öğe her değiştiğinde [onItemSelected] tetiklenir ve
 * [hapticOnChange] açıksa cihaz **hafifçe titrer** (detent geri bildirimi).
 *
 * Genel amaçlıdır (`<T>`): hız, dil, birim vb. herhangi bir kısa listeyle kullanılabilir.
 */
@Composable
fun <T> MatatikVerticalPicker(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 44.dp,
    visibleItemsCount: Int = 3,
    hapticOnChange: Boolean = true,
    showHighlight: Boolean = true,
    fadeEdges: Boolean = true,
    infinite: Boolean = false,
    selectedFontSize: TextUnit = 18.sp,
    unselectedFontSize: TextUnit = 14.sp,
    selectedFontWeight: FontWeight = FontWeight.Black,
    onSelectedClick: (() -> Unit)? = null,
    accentWhen: (T) -> Boolean = { true },
    neutralColor: Color? = null,
    label: (T) -> String = { it.toString() },
) {
    if (items.isEmpty()) return
    val isDark = LocalIsDarkTheme.current
    val haptic = LocalHapticFeedback.current
    val accentColor = MaterialTheme.colorScheme.primary
    val onBackgroundColor = if (isDark) Color.White else AppColors.Slate900

    val density = LocalDensity.current
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val realInitial = items.indexOf(selectedItem).coerceAtLeast(0)
    val itemCount = if (infinite) Int.MAX_VALUE else items.size
    val initialIndex = if (infinite) {
        val mid = Int.MAX_VALUE / 2
        mid - (mid % items.size) + realInitial
    } else {
        realInitial
    }

    val centerOffsetPx = with(density) { (itemHeight * ((visibleItemsCount - 1) / 2)).roundToPx() }
    LaunchedEffect(Unit) {
        listState.scrollToItem(initialIndex, -centerOffsetPx)
    }

    val centerIndex by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val visible = info.visibleItemsInfo
            if (visible.isEmpty()) {
                initialIndex
            } else {
                val center = (info.viewportStartOffset + info.viewportEndOffset) / 2
                visible.minByOrNull { abs((it.offset + it.size / 2) - center) }?.index ?: initialIndex
            }
        }
    }

    var lastEmittedIndex by remember { mutableStateOf(initialIndex) }
    LaunchedEffect(centerIndex) {
        if (centerIndex != lastEmittedIndex) {
            lastEmittedIndex = centerIndex
            if (hapticOnChange) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }
            onItemSelected(items[centerIndex.mod(items.size)])
        }
    }

    Box(
        modifier = modifier.height(itemHeight * visibleItemsCount),
        contentAlignment = Alignment.Center,
    ) {
        if (showHighlight) {
            GlassyCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight),
                cornerRadius = 14.dp,
            ) {}
        }

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (fadeEdges) {
                        Modifier
                            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        0f to Color.Transparent,
                                        0.14f to Color.Black,
                                        0.86f to Color.Black,
                                        1f to Color.Transparent,
                                    ),
                                    blendMode = BlendMode.DstIn,
                                )
                            }
                    } else {
                        Modifier
                    }
                ),
            contentPadding = PaddingValues(vertical = itemHeight * (visibleItemsCount / 2)),
            verticalArrangement = Arrangement.Center,
        ) {
            items(itemCount) { index ->
                val realIndex = index.mod(items.size)
                val isSelected = index == centerIndex
                val useAccent = isSelected && accentWhen(items[realIndex])
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .then(
                            if (isSelected && onSelectedClick != null) {
                                Modifier.clickable { onSelectedClick() }
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label(items[realIndex]),
                        fontSize = if (isSelected) selectedFontSize else unselectedFontSize,
                        fontWeight = if (isSelected) selectedFontWeight else FontWeight.Normal,
                        color = if (useAccent) accentColor else (neutralColor ?: onBackgroundColor),
                        letterSpacing = if (isSelected) 0.3.sp else 0.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier.fillMaxWidth().alpha(if (isSelected) 1f else 0.55f),
                    )
                }
            }
        }
    }
}

@Composable
private fun PickerPreviewHost(isDark: Boolean, content: @Composable () -> Unit) {
    androidx.compose.runtime.CompositionLocalProvider(LocalIsDarkTheme provides isDark) {
        Box(
            modifier = Modifier
                .background(if (isDark) AppColors.DarkSlate else Color.White)
                .padding(20.dp),
            contentAlignment = Alignment.Center,
            content = { content() },
        )
    }
}

@Preview
@Composable
private fun MatatikVerticalPickerDarkPreview() {
    val items = listOf("0.5x", "0.75x", "1x", "1.25x", "1.5x", "2x")
    var selected by remember { mutableStateOf(items[2]) }
    PickerPreviewHost(isDark = true) {
        MatatikVerticalPicker(
            items = items,
            selectedItem = selected,
            onItemSelected = { selected = it },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun MatatikVerticalPickerLightPreview() {
    val items = listOf("0.5x", "0.75x", "1x", "1.25x", "1.5x", "2x")
    var selected by remember { mutableStateOf(items[3]) }
    PickerPreviewHost(isDark = false) {
        MatatikVerticalPicker(
            items = items,
            selectedItem = selected,
            onItemSelected = { selected = it },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
