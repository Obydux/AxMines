package com.artillexstudios.axmines.integrations

import com.artillexstudios.axmines.AxMinesPlugin
import com.artillexstudios.axmines.mines.Mines
import com.artillexstudios.axmines.utils.TimeUtils
import java.util.Locale
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class PlaceholderAPIIntegration : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "axmines"
    }

    override fun getAuthor(): String {
        return "Artillex-Studios"
    }

    override fun getVersion(): String {
        return AxMinesPlugin.INSTANCE.description.version
    }

    // axmines_blocksbroken_<mine>
    // axmines_percent_mine
    override fun onPlaceholderRequest(player: Player?, params: String): String {
        val args = params.split("_")
        val mine = Mines.getTypes()[args[1].lowercase(Locale.ENGLISH)] ?: return ""

        return when (args[0]) {
            "blocksbroken" -> {
                String.format("%.2f", (mine.volume - mine.blocks))
            }
            "percent" -> {
                String.format("%.2f", (mine.blocks / mine.volume * 100.0))
            }
            "notbroken" -> {
                mine.blocks.toString()
            }
            "total" -> {
                mine.volume.toString()
            }
            "resettime" -> {
                TimeUtils.format((mine.config.RESET_TICKS - mine.tick) / 20 * 1000, mine)
            }
            else -> {
                ""
            }
        }
    }
}