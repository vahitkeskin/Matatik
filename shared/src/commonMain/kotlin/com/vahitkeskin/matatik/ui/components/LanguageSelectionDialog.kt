package com.vahitkeskin.matatik.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vahitkeskin.matatik.core.localization.Language
import com.vahitkeskin.matatik.core.localization.LocalizedStrings
import com.vahitkeskin.matatik.ui.theme.LocalIsDarkTheme
import com.vahitkeskin.matatik.ui.theme.AppColors

@Composable
fun LanguageSelectionDialog(
    strings: LocalizedStrings,
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            GlassyCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                cornerRadius = 24.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = strings.languageSelectorTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (LocalIsDarkTheme.current) androidx.compose.ui.graphics.Color.White else AppColors.Slate900
                    )

                    // Tekerlek seçici
                    MatatikVerticalPicker(
                        items = Language.entries.toList(),
                        selectedItem = currentLanguage,
                        onItemSelected = { language ->
                            onLanguageSelected(language)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        visibleItemsCount = 3,
                        infinite = true,
                        label = { "${it.flag}  ${it.nativeName}" }
                    )

                    // Kapat/Tamam Butonu
                    GlassyButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.fillMaxWidth(0.6f)
                    ) {
                        Text(
                            text = "OK",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
