import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.mavenPublish)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlinPoet)
    implementation(libs.symbolProcessingApi)
}

mavenPublishing {
    configure(KotlinJvm(JavadocJar.Javadoc(), true))
    coordinates("com.piroworkz", "versions-catalog", "1.0.0")
    pom {
        name.set("Versions Catalog")
        description.set("A library to generate a catalog of versions on build modules")
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

        }

    }
    signAllPublications()
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, true)
}