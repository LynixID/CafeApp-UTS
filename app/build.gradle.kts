plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("com.google.devtools.ksp")

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.cafeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cafeapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments ["clearPackageData"] = "true"
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
    dataBinding{
        enable = true
    }
    buildFeatures{
        viewBinding = true
    }
    viewBinding{
        enable = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.recyclerview:recyclerview:1.2.1") //show data List
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.github.bumptech.glide:glide:4.15.0") // for load image
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.0") //for load image
    implementation ("de.hdodenhof:circleimageview:3.1.0")
//  Untuk ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")
    implementation ("androidx.room:room-runtime:2.5.0")
    ksp("androidx.room:room-compiler:2.5.0")
    // Kotlin Extensions and Coroutines support for Room
    implementation ("androidx.room:room-ktx:2.5.0")
    // Test helpers
    testImplementation ("androidx.room:room-testing:2.5.0")
//   untuk ActivityResultLauncher
    implementation ("androidx.activity:activity-ktx:1.7.0" )// Versi terbaru dapat bervariasi, pastikan memeriksanya.
    implementation(kotlin("script-runtime"))

    testImplementation ("junit:junit:4.12")
    androidTestImplementation ("androidx.test.ext:junit:1.1.0")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.1.1")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.github.bumptech.glide:annotations:4.15.1")
    ksp("com.github.bumptech.glide:ksp:4.15.1")

//    Untuk Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")

}