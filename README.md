# SharedResource
> MOKO Resources를 사용하여 공통 모듈(KMP)에서 이미지, 색상, 문자열 등을 관리하고
> 이를 독립된 **iOS 레포지토리**에서 사용할 수 있도록 구성되었습니다.
> (**Android, Web 테스트 필요)



## 1. KMP 프로젝트 설정

### [추가된 환경 요구 사항]

- **JDK:** 17 (Android Studio 설정에서 Gradle JDK를 17로 지정 필수)
- **Android Studio:** Ladybug (2024.2.1) 이상 권장 (AGP 8.2+ 대응)
- **Compile SDK:** 34 (Android 14)



### 📂 폴더 구조 및 리소스 추가

리소스 파일은 반드시 `shared` 모듈의 아래 경로에 위치해야 합니다.

- 경로: `shared/src/commonMain/resources/MR/colors/colors.xml`

  - 이미지의 경우, `/images/chat.svg` 

- 코드 예시 (`colors.xml`)

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <resources>
      <color name="main_brand">
          <light>#FFCCBC</light>
          <dark>#E64A19</dark>
      </color>
          <color name="sub_brand">#00BCD4</color>
  
          <color name="white">#FFFFFF</color>
          <color name="black">#222222</color>
  </resources>
  ```

  - `light` / `dark` 모드 별 색상 지원 (✅ iOS 테스트 완료)



### ⚙️ Gradle 설정 

#### 1. 루트 빌드 설정 (`build.gradle.kts`)

프로젝트 최상위에서 플러그인 버전과 Kotlin 버전을 최신 Xcode/Android 환경에 맞춰 동기화합니다.

```kotlin
// Root build.gradle.kts
plugins {
    // 1. Xcode 26 호환을 위해 Kotlin 1.9.24 이상 사용
    kotlin("multiplatform") version "1.9.24" apply false
    
    // 2. Kotlin 버전에 맞춰 AGP(Android Gradle Plugin)도 업그레이드
    id("com.android.library") version "8.2.0" apply false
    
    // 3. MOKO Resources 플러그인 (최신 버전 사용 권장)
    id("dev.icerock.mobile.multiplatform-resources") version "0.23.0" apply false
}

buildscript {
    dependencies {
        classpath("dev.icerock.moko:resources-generator:0.23.0")
    }
}
```



#### 2. Gradle 버전 설정 (`gradle-wrapper.properties`)

AGP 8.2+와 Java 17을 감당하기 위해 Gradle 엔진 자체의 버전도 올렸습니다.

```properties
# gradle/wrapper/gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-bin.zip
```



#### 3. shared 빌드 설정 `shared/build.gradle.kts`

최신 Xcode(15/26) 및 Kotlin 1.9.x 호환성을 위해 아래 설정을 적용했습니다. 

(0.22.3 -> 0.23.0 버전 확인 완료 - 최신 버전 확인 필요)

```kotlin
plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    // 1. XCFramework 이름 정의 💡 
    val xcf = apple.XCFramework("SharedResource")

    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "SharedResource" // Framework 이름 통일
            xcf.add(this)
            isStatic = false // dynamic framework
            export("dev.icerock.moko:resources:0.23.0") // iOS에서 리소스 접근 허용
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            api("dev.icerock.moko:resources:0.23.0")
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.yourname.shared" // 본인 패키지명
    multiplatformResourcesClassName = "SharedResource" // 생성될 클래스 이름
}
```



## 2. 배포

### 🍏 iOS - XCFramework 생성

#### 1. 터미널 명령어로 빌드하기

터미널에서 아래 명령어를 입력합니다.

```bash
./gradlew :shared:assembleSharedReleaseXCFramework
```



#### 2. 생성된 파일 위치 확인

빌드가 끝나면 아래 경로에 `SharedResource.xcframework` 폴더가 생깁니다.

- **경로:** `shared/build/XCFrameworks/release/shared.xcframework` 



#### 3. Xcode에 추가하기

이제 Xcode를 열고 방금 만든 프레임워크를 프로젝트에 넣어줘야 합니다.

- 기본 

1. **Xcode 프로젝트**를 엽니다.
2. 프로젝트 설정의 **General** 탭으로 갑니다.
3. **Frameworks, Libraries, and Embedded Content** 섹션을 찾습니다.
4. `SharedResource.xcframework` 폴더를 여기에 **드래그 앤 드롭**합니다.
5. **Embed** 옵션이 `Embed & Sign`으로 되어 있는지 확인하세요.



- ⭐️ `tuist` 사용 프로젝트 

1. 프로젝트 내 `Frameworks` 폴더 만들어 `SharedResource.xcframework` 폴더 복사
2. `Project.swift`에 XCFramework 의존성 추가
   - ✅ 이 한 줄이면 **링킹 + 임베딩 설정까지 자동 처리**됩니다.

```swift
import ProjectDescription

let project = Project(
    name: "App",
    targets: [
        Target(
            name: "App",
            platform: .iOS,
            product: .app,
            bundleId: "com.example.app",
            deploymentTarget: .iOS(targetVersion: "15.0", devices: [.iphone]),
            infoPlist: .default,
            sources: ["Sources/**"],
            resources: ["Resources/**"],
            dependencies: [
                .xcframework(
                    path: .relativeToRoot("Frameworks/SharedResources.xcframework")
                )
            ]
        )
    ]
)
```



## 3. 사용

### 🍏 iOS

#### SwiftUI 사용

- 공통함수 적용 필요

```swift
import SwiftUI
import SharedResource

struct ContentView: View {
    var body: some View {
        HStack {
            Image(uiImage: SharedResource.images().users.toUIImage()!)
            Image(uiImage: SharedResource.images().location.toUIImage()!)
                .background(Color(SharedResource.colors().info.getUIColor()))
            Image(uiImage: SharedResource.images().home.toUIImage()!)
                .background(Color(SharedResource.colors().main_brand.getUIColor()))
            Image(uiImage: SharedResource.images().chat.toUIImage()!)
                .background(Color(SharedResource.colors().error.getUIColor()))
            Image(uiImage: SharedResource.images().profile.toUIImage()!)
        }
    }
}
```

