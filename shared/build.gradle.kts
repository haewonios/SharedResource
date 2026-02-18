import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

// 1. 배포 버전 설정 
version = "1.0.2"

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    // MOKO 리소스 플러그인
    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    androidTarget()
    
    val xcf = XCFramework("SharedResource")
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "SharedResource"
            isStatic = false
            // iOS에서의 리소스 접근 위해 추가
            export("dev.icerock.moko:resources:0.23.0")
            xcf.add(this)
        }
    }

    // // 웹(Wasm) 타겟 추가
    // @OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl::class)
    // wasmJs {
    //     browser()
    //     binaries.executable()
    // }

// 1. JS 타겟 설정
    js(IR) {
        moduleName = "shared-resource"
        browser {
            // 라이브러리 형태로 빌드하도록 설정
            binaries.library()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // MOKO 리소스 라이브러리 추가
                api("dev.icerock.moko:resources:0.23.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting {}
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                api("dev.icerock.moko:resources:0.23.0")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
        // 2. JS 소스셋 설정
        val jsMain by getting {
            dependsOn(commonMain)
        }
    }
}



android {
    namespace = "com.haewon.sharedresource"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

// MOKO 리소스 설정
multiplatformResources {
    multiplatformResourcesPackage = "com.haewon.sharedresource"
    multiplatformResourcesClassName = "SharedResource"
//    multiplatformResourcesSourceSet = "commonMain"
}

tasks.register("preparePublish") {
    dependsOn("jsBrowserProductionLibraryDistribution")
    doLast {
        val distDir = layout.buildDirectory.dir("dist/js/productionLibrary").get().asFile
        val imgSource = layout.buildDirectory.dir("generated/moko/jsMain/comhaewonsharedresource/res/images").get().asFile
        
        // 이미지 폴더를 패키지 내부로 복사
        if (imgSource.exists()) {
            val imgDest = File(distDir, "images")
            imgSource.copyRecursively(imgDest, true)
        }

        val pkgJson = File(distDir, "package.json")
        if (pkgJson.exists()) {
            val content = pkgJson.readText().replace(
                "\"name\": \"shared-resource\"", 
                "\"name\": \"@haewonios/shared-resource\""
            )
            pkgJson.writeText(content)
        }
        
        // JS 코드 내의 이미지 경로 에러 자동 수정
        distDir.walkTopDown().filter { it.name.endsWith(".js") }.forEach { file ->
            val content = file.readText()
                .replace("require(\"images/", "require(\"./images/")
                .replace("require('images/", "require('./images/")
            file.writeText(content)
        }
    }
}