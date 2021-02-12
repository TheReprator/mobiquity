plugins {
    id(Libs.Plugins.javaLibrary)
    kotlin(Libs.Plugins.kotlinJVM)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(Libs.Kotlin.stdlib)
    implementation(project(AppModules.moduleBase))

    // Testing
    api(Libs.TestDependencies.truth)
    api(Libs.TestDependencies.core)
    api(Libs.OkHttp.mockWebServer)
    api(Libs.TestDependencies.jUnit)
    api(Libs.TestDependencies.Mockk.unitTest)
    api(Libs.TestDependencies.Mockk.unitTest)

    api(Libs.Coroutines.coroutineTest) {
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-debug")
    }
}
