plugins {
    kotlin("jvm") version "1.9.21"
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "com.artillexstudios.axmines"
version = "1.1.0"

repositories {
    mavenCentral()

    maven("https://repo.artillex-studios.com/releases/")
    maven("https://repo.alessiodp.com/releases/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    implementation("com.artillexstudios.axapi:axapi:1.4.185:all")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("dev.triumphteam:triumph-gui:3.1.7")
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("me.carleslc.Simple-YAML:Simple-Yaml:1.8.4")
    compileOnly("org.slf4j:slf4j-api:2.0.9")
    compileOnly("org.apache.commons:commons-text:1.11.0")
    compileOnly("commons-io:commons-io:2.15.0")
    compileOnly("org.apache.commons:commons-math3:3.6.1")
    compileOnly("me.clip:placeholderapi:2.11.5")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.9.21")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    shadowJar {
        relocate("org.bstats", "com.artillexstudios.axmines.libs.bstats")
        relocate("com.artillexstudios.axapi", "com.artillexstudios.axmines.libs.axapi")
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()

        filesMatching("plugin.yml") {
            expand(mapOf("version" to project.version,))
        }
    }
}

kotlin {
    jvmToolchain(17)
}