plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.devtools.ksp")
  id("dagger.hilt.android.plugin")
  alias(libs.plugins.compose.compiler)
}

android {
  compileSdk = 35

  defaultConfig {
    applicationId = "com.minimalist.template"
    minSdk = 26
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
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
  namespace = "com.template"
}

dependencies {
  // Project dependencies
  // These are modules that the project depends on.
  implementation(project(":designSystem")) // UI, color, themes, shared across modules
  implementation(project(":randomuser")) // Sample module for randomuser.me API
  implementation(project(":spine")) // Communication between screens and modules

  implementation(libs.core.ktx)
  implementation(libs.appcompat)
  implementation(libs.compose.material3.android)
  implementation(libs.compose.ui.tooling)
  implementation(libs.lifecycle.runtime.ktx)
  implementation(libs.activity.compose)
  implementation(libs.navigation.compose)

  // Networking
  implementation(libs.retrofit)
  implementation(libs.retrofit.converter.gson)

  // Hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
  implementation(libs.hilt.navigation.compose)

  // Testing
  testImplementation(libs.junit)
  androidTestImplementation(libs.ext.junit)
  androidTestImplementation(libs.espresso.core)

  debugImplementation(libs.compose.ui.tooling)
}