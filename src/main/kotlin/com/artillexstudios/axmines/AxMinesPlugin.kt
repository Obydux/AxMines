package com.artillexstudios.axmines

import com.artillexstudios.axapi.AxPlugin
import com.artillexstudios.axapi.libs.libby.BukkitLibraryManager
import com.artillexstudios.axapi.libs.libby.Library
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags
import com.artillexstudios.axapi.utils.Version
import com.artillexstudios.axmines.commands.AxMinesCommand
import com.artillexstudios.axmines.config.impl.Config
import com.artillexstudios.axmines.config.impl.Messages
import com.artillexstudios.axmines.integrations.PlaceholderAPIIntegration
import com.artillexstudios.axmines.listener.BlockListener
import com.artillexstudios.axmines.mines.Mine
import com.artillexstudios.axmines.mines.MineTicker
import com.artillexstudios.axmines.mines.Mines
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import revxrsal.commands.bukkit.BukkitCommandHandler

class AxMinesPlugin : AxPlugin() {
    companion object {
        lateinit var INSTANCE: AxMinesPlugin
        lateinit var MESSAGES: Messages
    }

    override fun updateFlags(flags: FeatureFlags) {
        flags.PACKET_ENTITY_TRACKER_ENABLED.set(true)
    }

    override fun enable() {
        if (Version.getServerVersion().isOlderThan(Version.v1_18)) {
            logger.severe("Your server version is not supported! Disabling!")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        Metrics(this, 20058)

        val libraryLoader = BukkitLibraryManager(this)
        libraryLoader.addMavenCentral()
        libraryLoader.addJitPack()
        libraryLoader.loadLibrary(Library.builder().groupId("org.slf4j").artifactId("slf4j-api").version("2.0.9").build())
        libraryLoader.loadLibrary(Library.builder().groupId("org.apache.commons").artifactId("commons-text").version("1.11.0").build())
        libraryLoader.loadLibrary(Library.builder().groupId("org.apache.commons").artifactId("commons-text").version("1.11.0").build())
        libraryLoader.loadLibrary(Library.builder().groupId("commons-io").artifactId("commons-io").version("2.15.0").build())
        libraryLoader.loadLibrary(Library.builder().groupId("org.jetbrains.kotlin").artifactId("kotlin-stdlib").version("1.9.21").build())

        INSTANCE = this

        MESSAGES = Messages("messages.yml")

        val commandHandler = BukkitCommandHandler.create(this)

        commandHandler.registerValueResolver(Mine::class.java) { context ->
            val mine = context.popForParameter()

            return@registerValueResolver Mines.valueOf(mine)
        }

        commandHandler.autoCompleter.registerParameterSuggestions(Mine::class.java) { _, _, _ ->
            return@registerParameterSuggestions Mines.getTypes().keys
        }

        commandHandler.register(AxMinesCommand())
        commandHandler.registerBrigadier()

        Bukkit.getPluginManager().registerEvents(BlockListener(), this)

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PlaceholderAPIIntegration().register()
        }

        MineTicker.schedule()
        reload()
    }

    override fun reload() {
        Config.reload()
        MESSAGES.reload()
        Mines.reload()
    }
}