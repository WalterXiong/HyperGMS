plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.hypergms"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.hypergms"
        minSdk = 34
        targetSdk = 37
        versionCode = 1
        versionName = "2.3.3"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    signingConfigs {
        create("release") {
            storeFile = rootProject.file("hypergms.keystore")
            storePassword = "hypergms"
            keyAlias = "hypergms"
            keyPassword = "hypergms"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        buildConfig = false
        resValues = false
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
