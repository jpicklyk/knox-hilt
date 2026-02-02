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
    implementation(projects.knoxCore.feature)
    implementation(projects.knoxCore.usecaseExecutor)
    implementation(projects.knoxCore.android)
    implementation(projects.knoxCore.common)

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
