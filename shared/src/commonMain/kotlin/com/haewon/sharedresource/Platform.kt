package com.haewon.sharedresource

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform