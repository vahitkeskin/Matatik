import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    jvm()
    
    androidLibrary {
       namespace = "com.vahitkeskin.matatik.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            // Sistem barı ikon kontrastı (WindowCompat / WindowInsetsControllerCompat) için
            implementation(libs.androidx.core.ktx)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            // CAS motoru veri kontratları için serileştirme
            implementation(libs.kotlinx.serializationJson)
            // Asenkron çözüm akışı ve ViewModel scope'u için coroutines
            implementation(libs.kotlinx.coroutinesCore)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutinesTest)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

// --- Dinamik sürüm bilgisi: Gradle property'sinden Kotlin sabiti üretilir ---
// CLAUDE.md kuralı gereği UI katmanında hardcoded sürüm metni yasaktır.
val matatikVersionName: String =
    providers.gradleProperty("matatik.versionName").getOrElse("1.0.0")

val generateAppVersion = tasks.register("generateAppVersion") {
    description = "Sürüm bilgisini Gradle property'sinden Kotlin sabitine üretir."
    group = "build"
    val outputDir = layout.buildDirectory.dir("generated/appversion/kotlin")
    val versionValue = matatikVersionName
    outputs.dir(outputDir)
    doLast {
        val file = outputDir.get()
            .file("com/vahitkeskin/matatik/core/AppVersion.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
            package com.vahitkeskin.matatik.core

            /** Gradle tarafından üretilmiştir — elle düzenlemeyin. */
            object AppVersion {
                const val NAME: String = "$versionValue"
            }
            """.trimIndent() + "\n"
        )
    }
}

kotlin.sourceSets.named("commonMain") {
    kotlin.srcDir(generateAppVersion)
}