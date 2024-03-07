package com.artillexstudios.axmines.mines.editor

import com.artillexstudios.axapi.utils.ItemBuilder
import com.artillexstudios.axapi.utils.StringUtils
import com.artillexstudios.axmines.AxMinesPlugin
import com.artillexstudios.axmines.mines.Mine
import com.artillexstudios.axmines.utils.TimeUtils
import dev.triumphteam.gui.components.util.GuiFiller
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.GuiItem
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Material
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class MineEditor(val mine: Mine, val player: Player) {

    fun open() {
        val gui = Gui.gui()
            .disableAllInteractions()
            .title(MiniMessage.miniMessage().deserialize("<red>Mines editor"))
            .rows(5)
            .create()

        GuiFiller(gui).fillBorder(GuiItem(ItemStack(Material.GRAY_STAINED_GLASS_PANE)))

        gui.setItem(36, GuiItem(ItemBuilder(Material.TIPPED_ARROW).setName("<gray>Go back").get()) {
            MinesEditor(player).open()
        })

        val displayName = ItemBuilder(ItemStack(Material.NAME_TAG))
            .setName("<green>Display name")
            .setLore(
                listOf("", " <gray>Current name: <white><name>", "", " <gray>- <green>Click to edit!"),
                Placeholder.parsed("name", mine.config.DISPLAY_NAME)
            )
            .get()

        val contents = ItemBuilder(ItemStack(Material.EMERALD_BLOCK))
            .setName("<green>Contents")
            .setLore(listOf("", " <gray>- <green>Click to edit!"))
            .get()

        val teleportOnReset = ItemBuilder(ItemStack(Material.ENDER_PEARL))
            .setName("<green>Teleport on reset")
            .setLore(
                listOf(
                    "",
                    "<gray>Current value: <white><value>",
                    "",
                    " <gray>- <white>0:</white> Top block at player's location",
                    " <gray>- <white>1:</white> Teleport location",
                    " <gray>- <white>Other:</white> No teleport",
                    "",
                    "<green>Left click to increase!",
                    "<green>Right click to decrease!"
                ), Placeholder.unparsed("value", mine.config.TELEPORT_ON_RESET.toString())
            )
            .get()

        val actionBarEnabled = ItemBuilder(ItemStack(Material.STONE_BUTTON))
            .setName("<green>Action bar enabled")
            .setLore(
                listOf(
                    "",
                    "<gray>Current value: <white><value>",
                    "",
                    "<green>Click to toggle!"
                ), Placeholder.unparsed("value", mine.config.ACTION_BAR_ENABLED.toString())
            )
            .get()

        val actionBarRange = ItemBuilder(ItemStack(Material.LEAD))
            .setName("<green>Action bar range")
            .setLore(
                listOf(
                    "",
                    "<gray>Current value: <white><value>",
                    "",
                    "<green>Left click to increase! (Shift for +10)",
                    "<green>Right click to decrease! (Shift for -10)"
                ), Placeholder.unparsed("value", mine.config.ACTION_BAR_RANGE.toString())
            )
            .get()

        val broadcastReset = ItemBuilder(ItemStack(Material.OAK_BUTTON))
            .setName("<green>Broadcast reset")
            .setLore(
                listOf(
                    "",
                    "<gray>Current value: <white><value>",
                    "",
                    " <gray>- <white>0:</white> Whole world",
                    " <gray>- <white>-1:</white> All worlds",
                    " <gray>- <white>-2:</white> Silent",
                    " <gray>- <white>< -2:</white> Same as not negative",
                    " <gray>- <white>>= 1:</white> broadcast in range",
                    "",
                    "<green>Left click to increase! (Shift for +10)",
                    "<green>Right click to decrease! (Shift for -10)"
                ), Placeholder.unparsed("value", mine.config.BROADCAST_RESET.toString())
            )
            .get()

        val resetTicks = ItemBuilder(ItemStack(Material.CHEST))
            .setName("<green>Reset ticks")
            .setLore(
                listOf(
                    "",
                    "<gray>Current value: <white><value> ticks (<time> formatted)",
                    "",
                    "<w>20 ticks = 1 second",
                    "",
                    "<green>Click to edit"
                ), Placeholder.unparsed("value", mine.config.RESET_TICKS.toString()),
                Placeholder.unparsed("time", TimeUtils.format(mine.config.RESET_TICKS / 20 * 1000, mine))
            )
            .get()

        val resetPercent = ItemBuilder(ItemStack(Material.ANVIL))
            .setName("<green>Reset percent")
            .setLore(
                listOf(
                    "",
                    "<gray>Current value: <white><value>%",
                    "",
                    "<green>Left click to increase! (Shift for +10)",
                    "<green>Right click to decrease! (Shift for -10)"
                ), Placeholder.unparsed("value", mine.config.RESET_PERCENT.toString())
            )
            .get()

        val rewards = ItemBuilder(ItemStack(Material.CHEST))
            .setName("<green>Rewards")
            .setLore(
                listOf(
                    "",
                    "<green>Click to edit!",
                )
            )
            .get()

        gui.addItem(GuiItem(displayName) { event ->
            event.whoClicked.closeInventory()

            val factory = ConversationFactory(AxMinesPlugin.INSTANCE)
            val prompt = object : StringPrompt() {
                override fun getPromptText(context: ConversationContext): String {
                    return StringUtils.formatToString("<green>Please type the new displayname of this mine!")
                }

                override fun acceptInput(context: ConversationContext, input: String?): Prompt? {
                    if (input == null) return END_OF_CONVERSATION

                    mine.config.DISPLAY_NAME = input
                    mine.config.config.set("display-name", mine.config.DISPLAY_NAME)
                    open()
                    return END_OF_CONVERSATION
                }
            }

            factory.withFirstPrompt(prompt)
            factory.buildConversation(event.whoClicked as Player).begin()
        })

        gui.addItem(GuiItem(contents) { _ ->
            MineContentsEditor(mine, player).open()
        })

        gui.addItem(GuiItem(teleportOnReset) { event ->
            if (event.isLeftClick) {
                mine.config.TELEPORT_ON_RESET++
            } else {
                mine.config.TELEPORT_ON_RESET--
            }

            mine.config.config.set("teleport-on-reset", mine.config.TELEPORT_ON_RESET)
            open()
        })

        gui.addItem(GuiItem(actionBarEnabled) { _ ->
            mine.config.ACTION_BAR_ENABLED = !mine.config.ACTION_BAR_ENABLED
            mine.config.config.set("actionbar.enabled", mine.config.ACTION_BAR_ENABLED)

            open()
        })

        gui.addItem(GuiItem(actionBarRange) { event ->
            if (event.isLeftClick) {
                if (event.isShiftClick) {
                    mine.config.ACTION_BAR_RANGE += 10
                } else {
                    mine.config.ACTION_BAR_RANGE++
                }
            } else {
                if (event.isShiftClick) {
                    mine.config.ACTION_BAR_RANGE -= 10
                } else {
                    mine.config.ACTION_BAR_RANGE--
                }
            }

            mine.config.config.set("actionbar.range", mine.config.ACTION_BAR_RANGE)

            open()
        })

        gui.addItem(GuiItem(broadcastReset) { event ->
            if (event.isLeftClick) {
                if (event.isShiftClick) {
                    mine.config.BROADCAST_RESET += 10
                } else {
                    mine.config.BROADCAST_RESET++
                }
            } else {
                if (event.isShiftClick) {
                    mine.config.BROADCAST_RESET -= 10
                } else {
                    mine.config.BROADCAST_RESET--
                }
            }

            mine.config.config.set("broadcast-reset", mine.config.BROADCAST_RESET)

            open()
        })

        gui.addItem(GuiItem(resetTicks) { event ->
            event.whoClicked.closeInventory()

            val factory = ConversationFactory(AxMinesPlugin.INSTANCE)
            val prompt = object : StringPrompt() {
                override fun getPromptText(context: ConversationContext): String {
                    return StringUtils.formatToString("<green>Please type how often the mine should reset in ticks! (1 second = 20 ticks)")
                }

                override fun acceptInput(context: ConversationContext, input: String?): Prompt? {
                    if (input == null) return END_OF_CONVERSATION

                    mine.config.RESET_TICKS = input.toLong()
                    mine.config.config.set("reset.ticks", mine.config.RESET_TICKS)
                    open()
                    return END_OF_CONVERSATION
                }
            }

            factory.withFirstPrompt(prompt)
            factory.buildConversation(event.whoClicked as Player).begin()
        })

        gui.addItem(GuiItem(resetPercent) { event ->
            if (event.isLeftClick) {
                if (event.isShiftClick) {
                    mine.config.RESET_PERCENT += 10
                } else {
                    mine.config.RESET_PERCENT++
                }
            } else {
                if (event.isShiftClick) {
                    mine.config.RESET_PERCENT -= 10
                } else {
                    mine.config.RESET_PERCENT--
                }
            }

            mine.config.config.set("reset.percent", mine.config.RESET_PERCENT)

            open()
        })

        gui.addItem(GuiItem(rewards) {
            MineRewardsEditor(mine, player).open()
        })

        gui.setCloseGuiAction {
            mine.config.config.save()
            mine.reload(false)
        }

        gui.open(player)
    }
}