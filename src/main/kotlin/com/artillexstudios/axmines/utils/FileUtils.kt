package com.artillexstudios.axmines.utils

import com.artillexstudios.axapi.libs.yamlassist.YamlAssist
import com.artillexstudios.axmines.AxMinesPlugin
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object FileUtils {
    private val LOGGER: Logger = LoggerFactory.getLogger(FileUtils::class.java)

    @JvmField
    val PLUGIN_DIRECTORY: Path = AxMinesPlugin.INSTANCE.dataFolder.toPath()

    @JvmStatic
    fun getSuggestions(file: File, logger: Logger): Boolean {
        val suggestions = YamlAssist.getSuggestions(file)
        if (suggestions.isEmpty()) return true

        logger.error("Can't load yaml file: {}", file.toPath())
        logger.error("Possible solutions:")
        for (suggestion in suggestions) {
            logger.error(" - {}", suggestion)
        }

        return false
    }

    fun extractFile(clazz: Class<*>, filename: String, copiedName: String, outDir: Path, replace: Boolean): File {
        try {
            clazz.getResourceAsStream("/$filename").use { `in` ->
                if (`in` == null) {
                    val exception: Exception =
                        RuntimeException("Could not read file from jar! ($filename)")
                    LOGGER.error("Could not find file {} in the plugin's assets!", filename, exception)
                    throw RuntimeException(exception)
                }
                val path = outDir.resolve(copiedName)
                if (!Files.exists(path) || replace) {
                    Files.createDirectories(path.parent)
                    Files.copy(`in`, path, StandardCopyOption.REPLACE_EXISTING)
                    return path.toFile()
                }
                return path.toFile()
            }
        } catch (exception: IOException) {
            LOGGER.error(
                "An unexpected error occurred while extracting file {} from plugin's assets!",
                filename,
                exception
            )
            throw RuntimeException()
        }
    }

    fun copyFromResource(path: String) {
        try {
            ZipFile(
                AxMinesPlugin.INSTANCE.javaClass.getProtectionDomain().getCodeSource().getLocation().getPath()
            ).use { zip ->
                val it: Iterator<ZipEntry> = zip.entries().asIterator()
                while (it.hasNext()) {
                    val entry = it.next()
                    if (entry.name.startsWith("$path/")) {
                        if (!entry.name.endsWith(".yaml") && !entry.name.endsWith(".yml")) {
                            continue
                        }
                        val resource = AxMinesPlugin.INSTANCE.getResource(entry.name)
                        if (resource == null) {
                            LOGGER.error("Could not find file {} in plugin's assets!", entry.name)
                            continue
                        }

                        Files.copy(resource, PLUGIN_DIRECTORY.resolve(entry.name))
                    }
                }
            }
        } catch (exception: IOException) {
            LOGGER.error(
                "An unexpected error occurred while extracting directory {} from plugin's assets!",
                path,
                exception
            )
        }
    }
}