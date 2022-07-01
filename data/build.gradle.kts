plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        val nasaApiKey: String by project
        val nasaUrl: String by project
        val nasaImagesUrl: String by project
        val countriesUrl: String by project

        buildConfigField("String", "NASA_API_KEY", nasaApiKey)
        buildConfigField("String", "NASA_ENDPOINT", nasaUrl)
        buildConfigField("String", "NASA_IMAGES_ENDPOINT", nasaImagesUrl)
        buildConfigField("String", "COUNTRIES_ENDPOINT", countriesUrl)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":domain"))

    // Core
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)

    // Room
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)

    // Retrofit
    implementation(libs.bundles.retrofit)
    implementation(libs.okhttp)

    // Koin
    implementation(libs.koin)

    //Google maps
    implementation(libs.google.location)

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.androidx.junit)
    androidTestImplementation(libs.test.androidx.espresso)
}
