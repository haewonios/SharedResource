plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.library").version("8.2.0").apply(false)
    kotlin("multiplatform").version("1.9.24").apply(false)

    // MOKO 리소스 플러그인 등록
    id("dev.icerock.mobile.multiplatform-resources").version("0.23.0").apply(false)
}

//tasks.register("clean", Delete::class) {
//    delete(rootProject.buildDir)
//}

buildscript {
    dependencies {
        classpath("dev.icerock.moko:resources-generator:0.23.0")
    }
}