package kr.blugon.crazyarcade.etc

import kr.blugon.crazyarcade.CrazyArcade.Companion.waterballItem
import kr.blugon.pluginhelper.component.component
import kr.blugon.pluginhelper.component.italic
import kr.blugon.pluginhelper.item.ItemObject
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

object DropItem {
    val itemList = arrayListOf(
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemStack(Material.AIR),
        ItemObject(Material.APPLE).apply {
            this.displayName = "체력회복".component().color(NamedTextColor.RED).italic(false)
        }.build(),
        waterballItem(1),
        ItemStack(Material.NETHER_STAR)
    )

    fun Location.dropWaterball() {
        val item = itemList[(Math.random()* itemList.size).toInt()]
        if(item.type == Material.AIR) return
        if(item.type == Material.LIGHT_BLUE_DYE) {
            val spawn = this.world.spawn(this, Item::class.java)
            spawn.itemStack = item
        }
    }

    fun Location.dropWaterballPower() {
        val item = itemList[(Math.random()* itemList.size).toInt()]
        if(item.type == Material.AIR) return
        if(item.type == Material.NETHER_STAR) {
            val spawn = this.world.spawn(this, Item::class.java)
            spawn.itemStack = item
        }
    }

    fun Location.dropHeal() {
        val item = itemList[(Math.random()* itemList.size).toInt()]
        if(item.type == Material.AIR) return
        if(item.type == Material.APPLE) {
            val spawn = this.world.spawn(this, Item::class.java)
            spawn.itemStack = item
        }
    }
}