// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    val kotlin_version by extra("1.4.21")
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(Libs.androidGradlePlugin)
        classpath(Libs.Kotlin.gradlePlugin)
        classpath(Libs.DaggerHilt.classPath)
        classpath(Libs.AndroidX.Navigation.navigationPlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}


tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}