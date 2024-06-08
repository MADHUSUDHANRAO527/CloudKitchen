pluginManagement {
    repositories {
        google()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        mavenLocal()
       /* maven{ url 'https://jitpack.io' }
        maven{ url 'https://maven.google.com' }*/
    }
}

rootProject.name = "CloudKitchen"
include(":app")
