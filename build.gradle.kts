import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlin.poet)
    implementation(libs.symbolProcessingApi)
}

mavenPublishing {
    configure(KotlinJvm(JavadocJar.Javadoc(), true))
    coordinates("com.piroworkz", "versions-catalog", "1.0.2")
    pom {
        name.set("Versions Catalog")
        description.set("A library to generate a catalog of versions on build modules")
        url.set("https://github.com/piroworkz/KspCatalogGenerator")
        licenses {
            license {
                name = "Apache-2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }

        developers {
            developer {
                id = "davidluna"
                name = "David Luna"
                email = "piroworkz@me.com"
            }
        }

        scm {
            connection =
                "scm:git:git://github.com/piroworkz/KspCatalogGenerator.git"
            developerConnection =
                "scm:git:ssh://github.com/piroworkz/KspCatalogGenerator.git"
            url = "https://github.com/piroworkz/KspCatalogGenerator"
        }

    }
    signAllPublications()
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, true)
}