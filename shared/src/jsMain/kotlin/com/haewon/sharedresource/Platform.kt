package com.haewon.sharedresource

class JsPlatform : Platform {
    override val name: String = "Web (JavaScript)"
}

actual fun getPlatform(): Platform = JsPlatform()
