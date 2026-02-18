import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

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