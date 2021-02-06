plugins {
    id(Libs.Plugins.androidLibrary)
    kotlin(Libs.Plugins.kotlinAndroid)
    kotlin(Libs.Plugins.kotlinKapt)
    id(Libs.Plugins.kaptDagger)
    id(Libs.Plugins.kotlinNavigation)
}

android {
    compileSdkVersion(AndroidSdk.compile)

    defaultConfig {
        minSdkVersion(AndroidSdk.min)
        multiDexEnabled = true

        consumerProguardFiles(
            file("proguard-rules.pro")
        )

        resConfigs(AndroidSdk.locales)

        val mapKey = propOrDef("API_KEY_MAP", "")
        manifestPlaceholders += mapOf("API_KEY_MAP" to mapKey )
    }

    buildFeatures.dataBinding = true
    buildFeatures.viewBinding = true

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    sourceSets {
        map { it.java.srcDirs("src/${it.name}/kotlin") }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
    }

    buildTypes {

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                file("proguard-rules.pro")
            )
        }
    }

    packagingOptions {
        exclude ("META-INF/atomicfu.kotlin_module")
        pickFirst ("META-INF/*")
    }
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
    generateStubs = true
    javacOptions {
        option("-Xmaxerrs", 500)
    }
}

dependencies {
    implementation(project(AppModules.moduleBase))
    implementation(project(AppModules.moduleBaseAndroid))
    implementation(project(AppModules.moduleNavigation))
    implementation(project(AppModules.moduleDatabase))

    implementation(Libs.AndroidX.cardView)
    implementation(Libs.AndroidX.constraintlayout)

    implementation(Libs.AndroidX.Lifecycle.livedata)

    implementation(Libs.AndroidX.Navigation.fragmentKtx)

    implementation(Libs.AndroidX.Fragment.fragment)
    implementation(Libs.AndroidX.Fragment.fragmentKtx)

    implementation(Libs.AndroidX.MapV3.map)
    implementation(Libs.AndroidX.MapV3.location)

    //Hilt
    implementation(Libs.DaggerHilt.hilt)
    kapt(Libs.DaggerHilt.hiltCompilerAndroid)

    //ViewModal
    implementation(Libs.DaggerHilt.viewModel)
    kapt(Libs.DaggerHilt.hiltCompiler)
}

fun propOrDef(propertyName: String, defaultValue: Any): Any {
    val propertyValue = project.properties[propertyName]
    return propertyValue ?: defaultValue
}