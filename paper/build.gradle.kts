plugins {
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("net.minecrell.plugin-yml.bukkit") version("0.5.3")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":common"))
    compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
    implementation("net.kyori:adventure-text-serializer-plain:4.14.0")
}


bukkit {
    name = "RPShare"
    load = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder.STARTUP
    main = "com.plasmoverse.rpshare.RPSharePlugin"
    apiVersion = "1.19"
    authors = listOf("KPidS")
    depend = listOf("MCKotlin-Paper")
    description = "RPShare Plugin"
}


tasks {
    runServer {
        minecraftVersion("1.20.1")
    }
}