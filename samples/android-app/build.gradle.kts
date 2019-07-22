/*
 * Copyright 2019 Louis Cognault Ayeva Derman. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("SpellCheckingInspection")

plugins {
    id("com.android.application")
    kotlin("multiplatform")
}

android {
    compileSdkVersion(ProjectVersions.androidSdk)
    buildToolsVersion(ProjectVersions.androidBuildTools)
    defaultConfig {
        applicationId = "com.louiscad.splittiessample"
        minSdkVersion(14)
        targetSdkVersion(ProjectVersions.androidSdk)
        versionCode = 1
        versionName = ProjectVersions.thisLibrary
        resConfigs("en", "fr")
        proguardFile("../proguard-android-really-optimize.txt")
        proguardFile("proguard-rules.pro")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        getByName("debug") {
            storeFile = file("${System.getProperty("user.home")}/.android/debug.keystore")
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storePassword = "android"
        }
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    sourceSets.getByName("main") {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDir("src/androidMain/res")
    }
    packagingOptions {
        exclude("**/*.kotlin_module")
        exclude("**/*.kotlin_builtins")
        exclude("**/*.kotlin_metadata")
        exclude("META-INF/kotlinx-coroutines-core.kotlin_module")
    }
}

kotlin {
    android()
    sourceSets {
        all {
            languageSettings.apply {
                enableLanguageFeature("NewInference")
                useExperimentalAnnotation("kotlin.Experimental")
                useExperimentalAnnotation("splitties.experimental.ExperimentalSplittiesApi")
                useExperimentalAnnotation("splitties.lifecycle.coroutines.PotentialFutureAndroidXLifecycleKtxApi")
                useExperimentalAnnotation("splitties.lifecycle.coroutines.MainDispatcherPerformanceIssueWorkaround")
            }
        }
        commonMain.dependencies {
            api(kotlin("stdlib-common"))
        }
        androidMain.dependencies {
            implementation(project(":fun-packs:android-material-components-with-views-dsl"))
            arrayOf(
                "arch-lifecycle",
                "arch-room",
                "checkedlazy",
                "exceptions",
                "initprovider",
                "typesaferecyclerview"
            ).forEach { moduleName ->
                implementation(splitties(moduleName))
            }
            with(Libs) {
                arrayOf(
                    kotlin.stdlibJdk7,
                    androidX.appCompat,
                    androidX.coreKtx,
                    androidX.constraintLayout,
                    google.material,
                    timber,
                    kotlinX.coroutines.android
                )
            }.forEach {
                implementation(it)
            }
        }
    }
}

dependencies {
    arrayOf(
        "stetho-init",
        "views-dsl-ide-preview"
    ).forEach { moduleName ->
        debugImplementation(splitties(moduleName))
    }
}
