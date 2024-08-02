import java.net.URI

plugins {
    id("application")
    kotlin("jvm") version "2.0.0"
}

group = "org.forfree.sonar"
version = "1.0-SNAPSHOT"

repositories {
    maven { url = URI("https://plugins.gradle.org/m2/") }
    maven { url = URI("https://maven.aliyun.com/repository/public") }
    maven { url = URI("https://maven.aliyun.com/repository/google") }
    maven { url = URI("https://maven.aliyun.com/repository/central") }
    maven { url = URI("https://maven.aliyun.com/repository/jcenter") }
    mavenCentral()
    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to "*.jar"))
    implementation("com.github.ajalt.clikt:clikt:4.4.0")
    testImplementation(kotlin("test"))
}


tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "org.forfree.sonar.App"
}