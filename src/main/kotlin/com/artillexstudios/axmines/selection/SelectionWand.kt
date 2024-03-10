package com.artillexstudios.axmines.selection

import com.artillexstudios.axapi.serializers.Serializers
import com.artillexstudios.axapi.utils.StringUtils
import com.artillexstudios.axmines.AxMinesPlugin
import java.util.WeakHashMap
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object SelectionWand {
    private val key = NamespacedKey(AxMinesPlugin.INSTANCE, "selection_wand")
    private val selections = WeakHashMap<Player, Selection>()

    fun isWand(itemStack: ItemStack): Boolean {
        if (itemStack.type.isAir) return false
        val meta = itemStack.itemMeta ?: return false

        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }

    fun getWand(): ItemStack {
        val item = ItemStack(Material.GOLDEN_AXE)
        val meta = item.itemMeta ?: return item

        meta.setDisplayName(StringUtils.formatToString("<#7700FF><b>Selection wand"))
        meta.lore = listOf("", StringUtils.formatToString("<#7700FF><b>Left click"), StringUtils.formatToString(" <gray>- Select position #1"), "", StringUtils.formatToString("<#7700FF><b>Right click"), StringUtils.formatToString(" <gray>- Select position #2"))
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 0)
        item.itemMeta = meta
        return item
    }

    fun select(player: Player, location: Location, first: Boolean) {
        val selection = selections[player] ?: Selection()
        if (first) {
            if (location == selection.position1) return
            player.setCooldown(player.inventory.itemInMainHand.type, 5)
            selection.position1 = location
            player.sendMessage(StringUtils.formatToString(AxMinesPlugin.MESSAGES.PREFIX + AxMinesPlugin.MESSAGES.SELECTION_POS1, Placeholder.unparsed("location", Serializers.LOCATION.serialize(location))))
        } else {
            if (location == selection.position2) return
            player.setCooldown(player.inventory.itemInMainHand.type, 5)
            selection.position2 = location
            player.sendMessage(StringUtils.formatToString(AxMinesPlugin.MESSAGES.PREFIX + AxMinesPlugin.MESSAGES.SELECTION_POS2, Placeholder.unparsed("location", Serializers.LOCATION.serialize(location))))
        }

        selections[player] = selection
    }

    fun getSelection(player: Player): Selection? {
        return selections[player]
    }
}