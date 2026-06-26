package com.vahitkeskin.matatik

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform