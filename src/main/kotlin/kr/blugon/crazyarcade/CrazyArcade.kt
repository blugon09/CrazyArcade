package kr.blugon.crazyarcade

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import kr.blugon.crazyarcade.commands.Comand
import kr.blugon.crazyarcade.commands.TapCompleter
import kr.blugon.crazyarcade.events.WaterBall
import kr.blugon.pluginhelper.component.bold
import kr.blugon.pluginhelper.component.component
import kr.blugon.pluginhelper.component.italic
import kr.blugon.pluginhelper.etc.Scheduler
import kr.blugon.pluginhelper.etc.Scheduler.schedulerRepeatingTask
import kr.blugon.pluginhelper.item.ItemObject
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.projecttl.inventory.InventoryGUI.plugin
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class CrazyArcade : JavaPlugin(),Listener {
    companion object {
        var isPlaying = false
        val arcadePlayers = arrayListOf<Player>()

        val playerWaterballPower = HashMap<Player, Int>()
        var Player.waterballPower : Int
            get() {
                if(playerWaterballPower[this] == null) return 1
                else return playerWaterballPower[this]!!
            }
            set(value) {
                playerWaterballPower[this] = value
            }

        val playerWaterballAmount = HashMap<Player, Int>()
        var Player.waterballAmount : Int
            get() {
                if(playerWaterballAmount[this] == null) return 1
                else return playerWaterballAmount[this]!!
            }
            set(value) {
                playerWaterballAmount[this] = value
            }

        val damage = 2.0
        val maxamount = 5
        val maxpower = 10


        val waterballItem = ItemObject(Material.LIGHT_BLUE_DYE, 64).apply {
            this.displayName = "물풍선".component().color(TextColor.color(0, 127, 255)).bold(true).italic(false)
            this.customModelData = 1
        }.build()

        fun waterballItem(amount : Int) : ItemStack {
            val item = waterballItem.clone()
            item.amount = amount
            return item
        }

        lateinit var plugin : JavaPlugin
    }

    override fun onEnable() {
        logger.info("Plugin Enable")
        Bukkit.getPluginManager().registerEvents(this, this)
        Bukkit.getPluginManager().registerEvents(WaterBall(), this)

        schedulerRepeatingTask(this, 0, 1) {
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendActionBar("물풍선 개수: ".component().color(NamedTextColor.GREEN).append(
                    "${player.waterballAmount}/${maxamount}".component().color(NamedTextColor.YELLOW).append(

                    " | ".component().color(NamedTextColor.WHITE)).append(

                    "물풍선 사거리: ".component().color(NamedTextColor.GREEN).append(
                    "${player.waterballPower}/${maxpower}".component().color(NamedTextColor.YELLOW)))
                ))

                if(player.gameMode == GameMode.ADVENTURE) {
                    player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 1000000, 128, false, false))
                } else player.removePotionEffect(PotionEffectType.JUMP)
            }
        }

        getCommand("crazyarcade")!!.apply {
            setExecutor(Comand())
            tabCompleter = TapCompleter()
        }
        plugin = this
    }

    override fun onDisable() {
        logger.info("Plugin Disable")
    }
}