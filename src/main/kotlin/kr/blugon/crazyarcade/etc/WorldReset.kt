package kr.blugon.crazyarcade.etc

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Item

object WorldReset {
    fun resetWorld() {
        val world = Bukkit.getWorld("world")!!
        
        for(y in 0..1) {
            for(z in -26..26) {
                for(x in -26..26) {
                    val block = Location(world, x.toDouble(), y.toDouble(), z.toDouble()).block
                    val blockList = arrayListOf(
                        Material.STONE,
                        Material.GRASS_BLOCK,
                        Material.DIRT,
                        Material.COBBLESTONE,
                        Material.OAK_PLANKS,
                        Material.STONE,
                        Material.GRASS_BLOCK,
                        Material.DIRT,
                        Material.COBBLESTONE,
                        Material.OAK_PLANKS,
                        Material.BEDROCK
                    )
                    if(y == 0) {
                        block.type = blockList[(Math.random()*blockList.size).toInt()]
                    } else {
                        block.type = block.location.apply { this.y-- }.block.type
                    }
                }
            }
        }
        for(y in 0..1) {
            for(z in 24..26) {
                for(x in 24..26) {
                    val block = Location(world, x.toDouble(), y.toDouble(), z.toDouble()).block
                    block.type = Material.AIR
                }
            }
        }
        for(y in 0..1) {
            for(z in -26..-24) {
                for(x in -26..-24) {
                    val block = Location(world, x.toDouble(), y.toDouble(), z.toDouble()).block
                    block.type = Material.AIR
                }
            }
        }
        for(y in 0..1) {
            for(z in -26..-24) {
                for(x in 24..26) {
                    val block = Location(world, x.toDouble(), y.toDouble(), z.toDouble()).block
                    block.type = Material.AIR
                }
            }
        }
        for(y in 0..1) {
            for(z in 24..26) {
                for(x in -26..-24) {
                    val block = Location(world, x.toDouble(), y.toDouble(), z.toDouble()).block
                    block.type = Material.AIR
                }
            }
        }

        for(entity in world.entities) {
            if(entity is ArmorStand) entity.remove()
            if(entity is Item) entity.remove()
        }
    }
}