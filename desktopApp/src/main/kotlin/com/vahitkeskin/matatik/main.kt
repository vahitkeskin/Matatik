package com.vahitkeskin.matatik

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    val icon = painterResource("app_icon.png")
    Window(
        onCloseRequest = ::exitApplication,
        title = "Matatik",
        icon = icon
    ) {
        App()
    }
}
