plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("com.google.devtools.ksp")
  id("dagger.hilt.android.plugin")
  alias(libs.plugins.compose.compiler)
}

android {
  namespace = "com.minimalist.template.randomuser"
  compileSdk = 35

  defaultConfig {
    minSdk = 26
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
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_17.toString()
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  implementation(project(":designSystem"))
  implementation(project(":spine"))

  // Jetpack Compose UI
  implementation(libs.compose.ui)
  implementation(libs.compose.material3.android)

  // Lifecycle Libraries
  implementation(libs.lifecycle.runtime.ktx)
  implementation(libs.lifecycle.viewmodel.ktx)

  // Networking
  implementation(libs.retrofit)
  implementation(libs.retrofit.converter.gson)
  implementation(libs.logging.interceptor)
  implementation(libs.retrofit2.kotlinx.serialization.converter)

  // Image Loading
  implementation(libs.coil.compose)

  // Hilt for DI
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}