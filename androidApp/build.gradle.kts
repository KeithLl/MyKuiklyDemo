plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    buildFeatures {
        buildConfig = true // 确保启用BuildConfig生成
    }
    namespace = "com.android.mykuikly"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.android.mykuikly"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    lint {
        targetSdk = 34 // 设置Lint检查的目标SDK
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared"))

    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.appcompat:appcompat:1.3.1")

    implementation("com.squareup.picasso:picasso:2.71828")

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.dynamicanimation:dynamicanimation:1.0.0")
}
