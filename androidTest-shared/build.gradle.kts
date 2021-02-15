plugins {
    id(Libs.Plugins.androidLibrary)
    kotlin(Libs.Plugins.kotlinAndroid)
    kotlin(Libs.Plugins.kotlinKapt)
}

android {
    compileSdkVersion(AndroidSdk.compile)

    defaultConfig {
        minSdkVersion(AndroidSdk.min)
        resConfigs(AndroidSdk.locales)
    }

    sourceSets {
        map { it.java.srcDirs("src/${it.name}/kotlin") }
    }
}

dependencies {
    implementation(Libs.Kotlin.stdlib)

    api(Libs.AndroidX.Room.test)

    // Android Testing
    api(Libs.TestDependencies.AndroidXTest.truth)
    api(Libs.TestDependencies.AndroidXTest.junit)
    api(Libs.TestDependencies.AndroidXTest.core)
    api(Libs.TestDependencies.AndroidXTest.runner)
    api(Libs.TestDependencies.AndroidXTest.rules)
    api(Libs.TestDependencies.Mockk.instrumentedTest)

    //Hilt
    api(Libs.DaggerHilt.hiltAndroidTest)
    kapt(Libs.DaggerHilt.hiltCompilerAndroid)

    //workmanager
    api(Libs.AndroidX.Work.test)

    // Architecture components testing
    api(Libs.TestDependencies.core)
    api(Libs.Coroutines.coroutineTest) {
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-debug")
    }
}
