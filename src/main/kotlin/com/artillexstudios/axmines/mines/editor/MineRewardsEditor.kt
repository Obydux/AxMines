package com.artillexstudios.axmines.mines.editor

import com.artillexstudios.axapi.utils.ItemBuilder
import com.artillexstudios.axmines.mines.Mine
import dev.triumphteam.gui.components.util.GuiFiller
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.GuiItem
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.event.inventory.ClickType

class MineRewardsEditor(val mine: Mine, val player: Player) {

    fun open() {
        val gui = Gui.paginated()
            .disableAllInteractions()
            .title(MiniMessage.miniMessage().deserialize("<red>Reward editor"))
            .pageSize(21)
            .rows(5)
            .create()

        GuiFiller(gui).fillBorder(GuiItem(ItemStack(Material.GRAY_STAINED_GLASS_PANE)))

        gui.setItem(36, GuiItem(ItemBuilder(Material.TIPPED_ARROW).setName("<gray>Go back").get()) {
            MineEditor(mine, player).open()
        })

        gui.setItem(38, GuiItem(ItemBuilder(Material.ARROW).setName("<gray>Previous page").get()) {
            gui.previous()
        })

        gui.setItem(42, GuiItem(ItemBuilder(Material.ARROW).setName("<gray>Next page").get()) {
            gui.next()
        })

        gui.setItem(40, GuiItem(ItemBuilder(Material.PAPER).setName("<gray>Add new").get()) {
            val map = mutableMapOf<String, Any>()
            map["chance"] = 10.0
            mine.config.RANDOM_REWARDS.add(map)
            mine.config.config.set("random-rewards", mine.config.RANDOM_REWARDS)
            mine.config.config.save()
            mine.reload(false)

            open()
        })

        var index = 0
        mine.config.RANDOM_REWARDS.forEach { map ->
            index++
            val item = ItemBuilder(Material.EMERALD_BLOCK)
                .setName("Reward editor <id>", Placeholder.unparsed("id", index.toString()))
                .setLore(listOf("", "<green>Click to edit", "<red>Drop to remove!"))
                .get()

            gui.addItem(GuiItem(item) { event ->    
                if (event.click == ClickType.DROP) {
                    mine.config.RANDOM_REWARDS.remove(map)
                    mine.config.config.set("random-rewards", mine.config.RANDOM_REWARDS)
                    mine.config.config.save()
                    mine.reload(false)
                    open()
                } else {
                    MineRewardEditor(mine, player, map).open()
                }
            })
        }

        gui.open(player)
    }
}