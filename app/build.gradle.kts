plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.chaquo.python")
}

val releaseKeystorePath = System.getenv("ANDROID_KEYSTORE_PATH")
val releaseKeystorePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
val releaseKeyAlias = System.getenv("ANDROID_KEY_ALIAS")
val releaseKeyPassword = System.getenv("ANDROID_KEY_PASSWORD")
val configuredVersionCode = System.getenv("ANDROID_VERSION_CODE")
val androidVersionCode = configuredVersionCode?.toIntOrNull()
    ?: if (configuredVersionCode == null) 6
    else throw GradleException("ANDROID_VERSION_CODE must be a positive integer.")

if (androidVersionCode <= 0) {
    throw GradleException("ANDROID_VERSION_CODE must be a positive integer.")
}

val releaseApkBuildRequested = gradle.startParameter.taskNames.any {
    it.contains("assembleRelease", ignoreCase = true) ||
        it.contains("bundleRelease", ignoreCase = true)
}
if (releaseApkBuildRequested && listOf(
        releaseKeystorePath,
        releaseKeystorePassword,
        releaseKeyAlias,
        releaseKeyPassword
    ).any { it.isNullOrBlank() }
) {
    throw GradleException(
        "APK signing is not configured. Provide all ANDROID_KEYSTORE_* environment variables."
    )
}

android {
    namespace = "com.zeroday.cyberprotocol"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.zeroday.cyberprotocol"
        minSdk = 24
        targetSdk = 35
        versionCode = androidVersionCode
        versionName = "0.6.0"
        ndk { abiFilters += listOf("arm64-v8a", "x86_64") }
    }
    signingConfigs {
        create("release") {
            storeFile = file(releaseKeystorePath ?: "missing-release-key.jks")
            storePassword = releaseKeystorePassword ?: ""
            keyAlias = releaseKeyAlias ?: ""
            keyPassword = releaseKeyPassword ?: ""
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
        }
    }
    buildFeatures { compose = true; buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
chaquopy { defaultConfig { version = "3.13" } }
dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation(platform("androidx.compose:compose-bom:2025.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")
    testImplementation("junit:junit:4.13.2")
}
