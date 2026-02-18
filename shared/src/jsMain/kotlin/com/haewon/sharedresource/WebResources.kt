package com.haewon.sharedresource

import kotlin.js.JsExport
import kotlin.js.JsName
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.ImageResource

@JsExport
object AppResources {
    // 1. 컬러 내보내기
    val colors = object {
        val mainBrand = getColorHex(SharedResource.colors.main_brand)
        val subBrand = getColorHex(SharedResource.colors.sub_brand)
        val white = getColorHex(SharedResource.colors.white)
        val black = getColorHex(SharedResource.colors.black)
        val error = getColorHex(SharedResource.colors.error)
    }

    // 2. 실제 이미지 리소스 (현재 폴더에 있는 파일들)
    val images = object {
        val home = getImagePath(SharedResource.images.home)
        val chat = getImagePath(SharedResource.images.chat)
        val location = getImagePath(SharedResource.images.location)
        val profile = getImagePath(SharedResource.images.profile)
        val users = getImagePath(SharedResource.images.users)
    }

    private fun getImagePath(imageResource: ImageResource): String {
        // JS 타겟에서 ImageResource는 fileUrl 속성을 가지고 있어 
        // 빌드된 결과물 내의 실제 경로를 반환합니다.
        return imageResource.fileUrl
    }

    /**
     * MOKO ColorResource를 HEX(#RRGGBB) 문자열로 변환하는 헬퍼 함수
     * 라이브러리 내부의 컬러 값을 직접 추출합니다.
     */
    private fun getColorHex(colorResource: ColorResource): String {
        // MOKO Resources JS 타겟의 ColorResource는 lightColor와 darkColor를 가집니다.
        val color = colorResource.lightColor
        val r = color.red.toString(16).padStart(2, '0')
        val g = color.green.toString(16).padStart(2, '0')
        val b = color.blue.toString(16).padStart(2, '0')

        return "#$r$g$b".uppercase()
    }
}
