package kr.blugon.crazyarcade.commands

import kr.blugon.crazyarcade.CrazyArcade
import kr.blugon.crazyarcade.CrazyArcade.Companion.arcadePlayers
import kr.blugon.crazyarcade.CrazyArcade.Companion.plugin
import kr.blugon.crazyarcade.CrazyArcade.Companion.waterballAmount
import kr.blugon.crazyarcade.CrazyArcade.Companion.waterballItem
import kr.blugon.crazyarcade.CrazyArcade.Companion.waterballPower
import kr.blugon.crazyarcade.etc.WorldReset.resetWorld
import kr.blugon.pluginhelper.component.bold
import kr.blugon.pluginhelper.component.component
import kr.blugon.pluginhelper.component.italic
import kr.blugon.pluginhelper.etc.Scheduler.schedulerDelayTask
import kr.blugon.pluginhelper.etc.Title.sendTitle
import kr.blugon.pluginhelper.item.ItemObject
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Comand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(command.name != "crazyarcade") return false
        if(args.size != 1) return false
        when(args[0]) {
            "start" -> {
                arcadePlayers.clear()
                for(players in Bukkit.getOnlinePlayers()) arcadePlayers.add(players)
                if(arcadePlayers.size in 2..4) {
                    resetWorld()
                    for(player in arcadePlayers) {
                        player.waterballAmount = 1
                        player.waterballPower = 1
                        player.gameMode = GameMode.ADVENTURE
                        player.inventory.clear()
                        player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue

                        player.sendTitle("".component(), "3".component().color(NamedTextColor.GREEN), 0, 50, 10)
                        player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                        schedulerDelayTask(plugin, 20) {
                            player.sendTitle("".component(), "2".component().color(NamedTextColor.YELLOW), 0, 50, 10)
                            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                        }
                        schedulerDelayTask(plugin, 40) {
                            player.sendTitle("".component(), "1".component().color(NamedTextColor.RED), 0, 50, 10)
                            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                        }
                        schedulerDelayTask(plugin, 60) {
                            player.addPotionEffect(PotionEffect(PotionEffectType.HEAL, 1, 100, false, false))
                            player.sendTitle("".component(), "Start!".component().color(NamedTextColor.YELLOW), 0, 50, 10)
                            when(player) {
                                arcadePlayers[0] -> player.teleport(Location(player.world, 25.5, 0.0, 25.5, 135f, 0f))
                                arcadePlayers[1] -> player.teleport(Location(player.world, -24.5, 0.0, 25.5, -135f, 0f))
                                arcadePlayers[2] -> player.teleport(Location(player.world, 25.5, 0.0, -24.5, 45f, 0f))
                                arcadePlayers[3] -> player.teleport(Location(player.world, -24.5, 0.0, -24.5, -45f, 0f))
                            }
                            player.playSound(player.location, Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f)
                            player.inventory.addItem(waterballItem(1))
                        }
                    }
                } else sender.sendMessage("${ChatColor.RED}플레이어는 2명이하, 4명이상은 불가능합니다")
            }

            "stop" -> {
                for(player in arcadePlayers) {
                    player.waterballAmount = 1
                    player.waterballPower = 1
                    player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue
                    player.gameMode = GameMode.CREATIVE
                    player.teleport(Location(player.world, 0.0, 2.0, 0.0))
                    player.sendTitle("게임 중지".component().color(NamedTextColor.RED), " ".component())
                }
            }

            "waterball" -> {
                if(sender !is Player) {
                    sender.sendMessage("${ChatColor.RED}해당 명령어는 플레이어만 사용할수 있습니다")
                    return false
                }
                sender.inventory.addItem(waterballItem)
            }

            "reset" -> {
                resetWorld()
                sender.sendMessage("월드를 리셋하였습니다")
            }
        }
        return false
    }
}