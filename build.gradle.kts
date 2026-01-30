plugins {
    alias(libs.plugins.convention.android.feature)
    alias(libs.plugins.convention.android.hilt)
}

android {
    namespace = "net.sfelabs.knox.hilt"

    packaging {
        resources {
            excludes += arrayOf(
                "/META-INF/{LICENSE.md,LICENSE-notice.md}"
            )
        }
    }
}

dependencies {
    // Knox core dependencies (policy framework)
    implementation(project(":knox-core:feature"))
    implementation(project(":knox-core:usecase-executor"))
    implementation(project(":knox-core:android"))
    implementation(project(":knox-core:common"))

    // Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk.core)
    testImplementation(libs.kotlinx.coroutines.test)
}
