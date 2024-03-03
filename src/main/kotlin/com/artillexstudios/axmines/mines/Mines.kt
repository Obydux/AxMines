package com.artillexstudios.axmines.mines

import com.artillexstudios.axmines.utils.FileUtils
import java.util.Locale
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Mines {
    private val TYPES = HashMap<String, Mine>()
    private val MINES_FOLDER = FileUtils.PLUGIN_DIRECTORY.resolve("mines").toFile()
    private val LOGGER: Logger = LoggerFactory.getLogger(Mines::class.java)

    fun reload() {
        if (MINES_FOLDER.mkdirs()) {
            FileUtils.copyFromResource("mines")
        }

        val files = org.apache.commons.io.FileUtils.listFiles(MINES_FOLDER, arrayOf("yaml", "yml"), true)

        files.forEach { file ->
            if (file.name.contains("_example")) return@forEach
            val mine = TYPES[file.nameWithoutExtension]

            if (mine == null) {
                Mine(file)
            } else {
                mine.reload()
            }
        }

        TYPES.entries.removeIf { entry ->
            return@removeIf !files.contains(entry.value.file)
        }
    }

    fun unregister(mine: Mine) {
        TYPES.remove(mine.name.lowercase(Locale.ENGLISH))
    }

    fun register(mine: Mine) {
        if (TYPES.containsKey(mine.name.lowercase(Locale.ENGLISH))) {
            LOGGER.warn("A mine named {} has already been registered! Skipping!", mine.name.lowercase(Locale.ENGLISH))
            return
        }

        TYPES[mine.name.lowercase(Locale.ENGLISH)] = mine
    }

    fun valueOf(name: String): Mine {
        return TYPES[name.lowercase(Locale.ENGLISH)] ?: throw IllegalArgumentException("Unknown mine type: $name!")
    }

    fun getTypes(): Map<String, Mine> {
        return TYPES
    }
}