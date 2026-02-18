package com.haewon.sharedresource

import kotlin.js.JsExport
import kotlin.js.JsName
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.ImageResource

@JsExport
class ColorData(
    val mainBrand: String,
    val subBrand: String,
    val white: String,
    val black: String,
    val error: String
)

@JsExport
class ImageData(
    val home: String,
    val chat: String,
    val location: String,
    val profile: String,
    val users: String
)

@JsExport
object AppResources {
    val colors = ColorData(
        mainBrand = getColorHex(SharedResource.colors.main_brand),
        subBrand = getColorHex(SharedResource.colors.sub_brand),
        white = getColorHex(SharedResource.colors.white),
        black = getColorHex(SharedResource.colors.black),
        error = getColorHex(SharedResource.colors.error)
    )

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
