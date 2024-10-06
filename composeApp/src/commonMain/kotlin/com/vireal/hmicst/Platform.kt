package com.vireal.hmicst

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform