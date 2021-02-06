import com.android.builder.signing.DefaultSigningConfig
import java.io.FileInputStream
import java.util.*

plugins {
    id(Libs.Plugins.androidApplication)
    kotlin(Libs.Plugins.kotlinAndroid)
    kotlin(Libs.Plugins.kotlinKapt)
    id(Libs.Plugins.kotlinNavigation)
    id(Libs.Plugins.kaptDagger)
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
    javacOptions {
        option("-Xmaxerrs", 500)
    }
}


fun getKeyStoreConfig(defaultSigningConfig: DefaultSigningConfig, propertyFileName: String) {
    val properties = Properties()
    val propFile = File("./signingconfig/$propertyFileName")
    if (propFile.canRead() && propFile.exists()) {
        properties.load(FileInputStream(propFile))
        if (properties.containsKey("storeFile") && properties.containsKey("storePassword") &&
            properties.containsKey("keyAlias") && properties.containsKey("keyPassword")
        ) {
            defaultSigningConfig.storeFile = file("../${properties.getProperty("storeFile")}")
            defaultSigningConfig.storePassword = properties.getProperty("storePassword")
            defaultSigningConfig.keyAlias = properties.getProperty("keyAlias")
            defaultSigningConfig.keyPassword = properties.getProperty("keyPassword")
            defaultSigningConfig.isV2SigningEnabled = true
        }
    }
}

android {
    compileSdkVersion(AndroidSdk.compile)

    defaultConfig {
        applicationId = AppConstant.applicationPackage

        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)

        versionCode = AppVersion.versionCode
        versionName = AppVersion.versionName

        testInstrumentationRunner = Libs.TestDependencies.testRunner

        multiDexEnabled = true

        resConfigs(AndroidSdk.locales)

        buildConfigField("String", AppConstant.hostConstant, "\"${AppConstant.host}\"")
    }

    signingConfigs {
        getByName("debug") {
            getKeyStoreConfig(this, "signing-debug.properties")
        }
    }

    buildTypes {

        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
    }

    flavorDimensions(AppConstant.flavourDimension)

    buildFeatures.viewBinding = true
    buildFeatures.dataBinding = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

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

    packagingOptions {
        pickFirst("META-INF/*")
    }

}

dependencies {
    implementation(project(AppModules.moduleBase))
    implementation(project(AppModules.moduleBaseAndroid))
    implementation(project(AppModules.moduleNavigation))
    implementation(project(AppModules.moduleDatabase))

    implementation(project(AppModules.moduleHome))
    implementation(project(AppModules.moduleHomeAddCity))
    implementation(project(AppModules.moduleHomeSaveCity))
    implementation(project(AppModules.moduleCity))
    implementation(project(AppModules.moduleHelp))
    implementation(project(AppModules.moduleSetting))

    implementation(Libs.AndroidX.constraintlayout)

    kapt(Libs.AndroidX.Lifecycle.compiler)
    implementation(Libs.AndroidX.Lifecycle.extensions)

    implementation(Libs.AndroidX.Navigation.fragmentKtx)
    implementation(Libs.AndroidX.Navigation.uiKtx)

    implementation(Libs.AndroidX.multidex)

    implementation(Libs.OkHttp.loggingInterceptor)

    implementation(Libs.DaggerHilt.hilt)
    kapt(Libs.DaggerHilt.hiltCompiler)
    kapt(Libs.DaggerHilt.hiltCompilerAndroid)

    implementation(Libs.DaggerHilt.viewModel)

    implementation(Libs.AndroidX.Room.runtime)
    implementation(Libs.AndroidX.Room.ktx)
    kapt(Libs.AndroidX.Room.compiler)

    testImplementation(Libs.TestDependencies.Mockk.unitTest)
    testImplementation(Libs.TestDependencies.truth)
}