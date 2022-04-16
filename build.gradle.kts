plugins {
    kotlin("jvm") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.1"
}

group = "kr.blugon"
version = "1.0.0-SNAPSHOT"


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.projecttl.net/repository/maven-public/")
    maven("https://jitpack.io/")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")

    implementation("net.projecttl:InventoryGUI-api:4.3.1")
    implementation("im.kimcore:Josa.kt:1.6")
//    implementation("kr.blugon:PluginHelper:1.0.7-SNAPSHOT")
}


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }

    shadowJar {
        archiveVersion.set(project.version.toString())
        archiveBaseName.set(project.name)
        archiveFileName.set("${project.name}.jar")
        from(sourceSets["main"].output)

        doLast {
            copy {
                from(archiveFile)

                //Build Location
                val plugins = File("C:/Files/Minecraft/Servers/CrazyArcade/plugins")
                into(plugins)
            }
        }
    }
}