package com.haewon.sharedresource

import kotlin.js.JsExport
import kotlin.js.JsName
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.ImageResource

@JsExport
class ColorData(
    @JsName("mainBrand") val mainBrand: String,
    @JsName("subBrand") val subBrand: String,
    @JsName("white") val white: String,
    @JsName("black") val black: String,
    @JsName("error") val error: String
)

@JsExport
class ImageData(
    @JsName("home") val home: String,
    @JsName("chat") val chat: String,
    @JsName("location") val location: String,
    @JsName("profile") val profile: String,
    @JsName("users") val users: String
)

@JsExport
object AppResources {
    @JsName("colors")
    val colors = ColorData(
        mainBrand = getColorHex(SharedResource.colors.main_brand),
        subBrand = getColorHex(SharedResource.colors.sub_brand),
        white = getColorHex(SharedResource.colors.white),
        black = getColorHex(SharedResource.colors.black),
        error = getColorHex(SharedResource.colors.error)
    )

    @JsName("images")
    val images = ImageData(
        home = SharedResource.images.home.fileUrl,
        chat = SharedResource.images.chat.fileUrl,
        location = SharedResource.images.location.fileUrl,
        profile = SharedResource.images.profile.fileUrl,
        users = SharedResource.images.users.fileUrl
    )

    private fun getColorHex(colorResource: ColorResource): String {
        val color = colorResource.lightColor
        val r = color.red.toString(16).padStart(2, '0')
        val g = color.green.toString(16).padStart(2, '0')
        val b = color.blue.toString(16).padStart(2, '0')
        return "#$r$g$b".uppercase()
    }
}
