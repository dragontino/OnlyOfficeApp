import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.android.library) apply false
}

ext {
    properties["datastore_keyalias"] = getDataStoreKeyAlias()
}

fun getDataStoreKeyAlias(): String {
    val properties = Properties()
    project.file("local.properties").inputStream().let(properties::load)
    return properties.getProperty("datastore-keyalias", "")
}