plugins {
    kotlin("jvm") version "2.1.20"
    id("com.gradleup.shadow") version "9.0.0-beta12"
}

group = "com.artillexstudios.axmines"
version = "1.3.1"

repositories {
    mavenCentral()

    maven("https://repo.artillex-studios.com/releases/")
    maven("https://repo.alessiodp.com/releases/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.oraxen.com/releases/")
    maven("https://maven.devs.beer/")
}

dependencies {
    implementation("com.artillexstudios.axapi:axapi:1.4.519:all")
    implementation("org.bstats:bstats-bukkit:3.1.0")
    implementation("dev.triumphteam:triumph-gui:3.1.11")
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("org.slf4j:slf4j-api:2.0.17")
    compileOnly("org.apache.commons:commons-text:1.13.0")
    compileOnly("commons-io:commons-io:2.18.0")
    compileOnly("org.apache.commons:commons-math3:3.6.1")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:2.1.20")
    compileOnly("io.th0rgal:oraxen:1.189.0")
    compileOnly("dev.lone:api-itemsadder:4.0.9")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    shadowJar {
        relocate("com.artillexstudios.axapi", "com.artillexstudios.axmines.libs.axapi")
        relocate("org.bstats", "com.artillexstudios.axmines.libs.bstats")
        relocate("org.apache.commons.math3", "com.artillexstudios.axmines.libs.axapi.libs.math3")
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()

        filesMatching("plugin.yml") {
            expand(mapOf("version" to project.version))
        }
    }
}

kotlin {
    jvmToolchain(17)
}