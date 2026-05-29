plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
}

android {
    namespace = "edu.guigu.accountbook"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "edu.guigu.accountbook"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity.ktx)
    implementation(libs.constraintlayout)
    implementation(libs.fragment.ktx)
    implementation(libs.viewpager2)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.coroutines.android)
    implementation(libs.mpandroidchart)
    
    // Task7: 文件解析和PDF生成
    implementation("org.apache.poi:poi:5.2.5")              // Excel解析
    implementation("org.apache.poi:poi-ooxml:5.2.5")        // Excel (xlsx)解析
    implementation("com.opencsv:opencsv:5.7.1")             // CSV解析
    implementation("com.itextpdf:itext7-core:7.2.5")        // PDF生成
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // AI API调用
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // JSON转换
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // 网络日志
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
