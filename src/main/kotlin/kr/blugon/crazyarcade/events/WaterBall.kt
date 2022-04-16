package kr.blugon.crazyarcade.events

import com.github.kimcore.josa.Josa.이가
import kr.blugon.crazyarcade.CrazyArcade.Companion.arcadePlayers
import kr.blugon.crazyarcade.CrazyArcade.Companion.damage
import kr.blugon.crazyarcade.CrazyArcade.Companion.maxamount
import kr.blugon.crazyarcade.CrazyArcade.Companion.maxpower
import kr.blugon.crazyarcade.CrazyArcade.Companion.plugin
import kr.blugon.crazyarcade.CrazyArcade.Companion.waterballAmount
import kr.blugon.crazyarcade.CrazyArcade.Companion.waterballItem
import kr.blugon.crazyarcade.CrazyArcade.Companion.waterballPower
import kr.blugon.crazyarcade.etc.DropItem.dropHeal
import kr.blugon.crazyarcade.etc.DropItem.dropWaterball
import kr.blugon.crazyarcade.etc.DropItem.dropWaterballPower
import kr.blugon.pluginhelper.component.component
import kr.blugon.pluginhelper.component.italic
import kr.blugon.pluginhelper.etc.Scheduler.schedulerDelayTask
import kr.blugon.pluginhelper.etc.Title.sendTitle
import kr.blugon.pluginhelper.inventory.contains
import kr.blugon.pluginhelper.item.ItemObject
import kr.blugon.pluginhelper.particle.ParticleObject
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.*
import org.bukkit.Particle.DustOptions
import org.bukkit.attribute.Attribute
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerAttemptPickupItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class WaterBall : Listener {

    @EventHandler
    fun waterball(event : PlayerInteractEvent) {
        val player = event.player
        val eventItem = player.inventory.itemInMainHand

        if(event.action != Action.RIGHT_CLICK_BLOCK) return
        if(eventItem.type != Material.LIGHT_BLUE_DYE || eventItem.itemMeta.customModelData != 1) return
//        if(player.getTargetBlock(120)!!.type != Material.BEDROCK) return
        val block = player.getTargetBlock(120)!!
        val location = block.location.add(.5, 1.0, .5)
        val world = location.world
        if(location.y-1 != -1.0) return
        location.block.type = Material.BARRIER
        world.playSound(location, Sound.ENTITY_SLIME_JUMP_SMALL, 1f, 2f)
        player.inventory.itemInMainHand.amount--

        val waterballStand = world.spawn(location, ArmorStand::class.java)
        waterballStand.isMarker = true
        waterballStand.isVisible = false
        waterballStand.setItem(EquipmentSlot.HEAD, waterballItem)

        schedulerDelayTask(plugin, 10) {
            waterballStand.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 1000000, 100, false, false))
        }
        schedulerDelayTask(plugin, 20) {
            waterballStand.removePotionEffect(PotionEffectType.GLOWING)
        }
        schedulerDelayTask(plugin, 30) {
            waterballStand.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 1000000, 100, false, false))
        }
        schedulerDelayTask(plugin, 40) {
            waterballStand.removePotionEffect(PotionEffectType.GLOWING)
        }
        schedulerDelayTask(plugin, 50) {
            waterballStand.remove()
            world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 2f)
            location.block.type = Material.AIR
            ParticleObject(Particle.WATER_SPLASH,
                location,
                50,
                true,
                null,
                0.0,
                0.4, 0.4, 0.4
            ).spawn()
            player.inventory.addItem(waterballItem(1))
            for(i in 0..3) {
                var added = 0
                for(power in 1..player.waterballPower) {
                    ParticleObject(
                        Particle.REDSTONE, location.add(location.direction.multiply(1)),
                        20,
                        true,
                        DustOptions(Color.fromRGB(0, 63, 255), 1f),
                        0.0,
                        0.2, 0.2, 0.2
                    ).spawn()
                    ParticleObject(
                        Particle.REDSTONE, location,
                        20,
                        true,
                        DustOptions(Color.fromRGB(0, 140, 255), 1f),
                        0.0,
                        0.2, 0.2, 0.2
                    ).spawn()
                    ParticleObject(
                        Particle.REDSTONE, location,
                        10,
                        true,
                        DustOptions(Color.WHITE, 1f),
                        0.0,
                        0.2, 0.3, 0.2
                    ).spawn()
                    added++

                    //맞았을때
                    for(entity in location.getNearbyEntities(0.3, 0.1, 0.3)) {
                        if(entity !is Player) continue
                        if(entity.health-damage < 1) {
                            entity.gameMode = GameMode.SPECTATOR
                            for (players in arcadePlayers) {
                                players.sendTitle(
                                    "".component(),
                                    "${entity.name.이가} 탈락했습니다!".component().color(NamedTextColor.RED)
                                )
                                players.playSound(players.location, Sound.ENTITY_BLAZE_DEATH, 1f, 2f)
                            }
                            val survival = arrayListOf<Player>()
                            for (players in arcadePlayers) {
                                if (players.gameMode == GameMode.ADVENTURE) survival.add(players)
                            }
                            if (survival.size == 1) {
                                for (players in arcadePlayers) {
                                    players.waterballAmount = 1
                                    players.waterballPower = 1
                                    player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue

                                    players.sendTitle(
                                        "".component(),
                                        "${survival[0].name.이가} 승리했습니다".component().color(NamedTextColor.YELLOW)
                                    )
                                    players.playSound(players.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f)
                                    players.gameMode = GameMode.CREATIVE
                                    players.teleport(Location(world, 0.0, 2.0, 0.0))
                                }
                            }
                        } else {
                            entity.damage(damage, player)
                            entity.noDamageTicks = 0
                            entity.teleport(entity)
                        }
                    }
                    if(location.block.type != Material.AIR) {
                        if(location.block.type == Material.BEDROCK) break
                        if(location.block.type == Material.BARRIER) continue
                        location.block.breakNaturally(true)
                        location.clone().add(.0, 1.0, .0).block.breakNaturally(true)
                        if(player.waterballAmount < maxamount) location.dropWaterball()
                        if(player.waterballPower < maxpower) location.dropWaterballPower()
                        if(!player.contains(Material.APPLE, 3)) location.dropHeal()
                        break
                    }
                }
                location.subtract(location.direction.multiply(added))
                location.yaw+=90
            }
        }
    }

    @EventHandler
    fun getItem(event : PlayerAttemptPickupItemEvent) {
        val player = event.player
        val item = event.item.itemStack

        if(item.type == Material.NETHER_STAR) {
            if(maxpower <= player.waterballPower) {
                event.isCancelled = true
                return
            }
            for(i in 1..item.amount) {
                player.waterballPower++
                if(maxpower <= player.waterballPower) {
                    event.isCancelled = true
                    event.item.remove()
                    break
                }
            }
            player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, 1f, 1f)
            event.isCancelled = true
            event.item.remove()
        } else if(item.type == Material.LIGHT_BLUE_DYE && item.itemMeta.customModelData == 1) {
            if(maxamount <= player.waterballAmount) {
                event.isCancelled = true
            } else {
                for(i in 1..item.amount) {
                    player.waterballAmount++
                    player.inventory.addItem(waterballItem(1))
                    if(maxamount <= player.waterballAmount) {
                        event.isCancelled = true
                        event.item.remove()
                        break
                    }
                }
                player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, 1f, 1f)
                event.isCancelled = true
                event.item.remove()
            }
        } else if(item.type == Material.APPLE) {
            if(player.contains(Material.APPLE, 3)) {
                event.isCancelled = true
            } else {
                for(i in 1..item.amount) {
                    player.inventory.addItem(ItemObject(Material.APPLE).apply {
                        this.displayName = "체력회복".component().color(NamedTextColor.RED).italic(false)
                    }.build())
                    if(player.contains(Material.APPLE, 3)) {
                        event.isCancelled = true
                        event.item.remove()
                        break
                    }
                }
                player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, 1f, 1f)
                event.isCancelled = true
                event.item.remove()
            }
        }
    }

    @EventHandler
    fun exitWaterball(event : PlayerInteractEvent) {
        val player = event.player
        val eventItem = player.inventory.itemInMainHand

        if(event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return
        if(player.health.toInt() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue.toInt()) return
        if(eventItem.type != Material.APPLE) return
        eventItem.amount--
        player.playSound(player.location, Sound.ENTITY_PLAYER_BURP, 1f, 1f)
        if(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue < player.health+2) {
            player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue
        } else player.health+=2
    }
}