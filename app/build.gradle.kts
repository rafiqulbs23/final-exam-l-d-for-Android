plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinAndroidKsp)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.rafiqulislam.projecttemplate"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.rafiqulislam.projecttemplate"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.material3.material3)
    implementation(libs.androidx.navigation.common.android)

    //noinspection UseTomlInstead
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    implementation (libs.androidx.material.icons.extended) // Check for latest version if needed

    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(project(":core"))
    // Dagger - Hilt
    implementation(libs.hilt.android)
    ksp (libs.hilt.androidx.compiler)
    implementation( libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation (libs.androidx.material3)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    // Coil
    implementation(libs.coil.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp.logging.interceptor)
    implementation (libs.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    // Room components
    implementation (libs.room.runtime)
    ksp(libs.room.compiler) // Changed from annotationProcessor
    // Room with Kotlin extensions
    implementation (libs.androidx.room.ktx)


    implementation(libs.kotlinx.serialization.json)

    //Desuger
    coreLibraryDesugaring(libs.desugar)
    implementation (libs.foundation)

    //Splash Api
    implementation (libs.splash.screen)
}