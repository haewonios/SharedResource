package com.haewon.sharedresource

import kotlin.js.JsExport
import kotlin.js.JsName

@JsExport
object AppResources {
    // dynamic 타입을 사용하여 컴파일러의 간섭을 원천 차단합니다.
    val colors: dynamic = js("{}")
    val images: dynamic = js("{}")

    init {
        // [필독] 따옴표 안의 이름("home", "mainBrand")은 절대로 난독화되지 않습니다.
        
        // 1. 컬러 설정
        colors["mainBrand"] = getColorHex(SharedResource.colors.main_brand)
        colors["subBrand"] = getColorHex(SharedResource.colors.sub_brand)
        colors["white"] = getColorHex(SharedResource.colors.white)
        colors["black"] = getColorHex(SharedResource.colors.black)
        colors["error"] = getColorHex(SharedResource.colors.error)

        // 2. 이미지 설정
        images["home"] = SharedResource.images.home.fileUrl
        images["chat"] = SharedResource.images.chat.fileUrl
        images["location"] = SharedResource.images.location.fileUrl
        images["profile"] = SharedResource.images.profile.fileUrl
        images["users"] = SharedResource.images.users.fileUrl
        
        console.log("KMP: 리소스가 성공적으로 맵핑되었습니다!", images)
    }

    private fun getColorHex(colorResource: dev.icerock.moko.resources.ColorResource): String {
        val color = colorResource.lightColor
        val r = color.red.toString(16).padStart(2, '0')
        val g = color.green.toString(16).padStart(2, '0')
        val b = color.blue.toString(16).padStart(2, '0')
        return "#$r$g$b".uppercase()
    }
}
