package com.artillexstudios.axmines.mines.setter

import com.artillexstudios.axapi.nms.NMSHandlers
import com.artillexstudios.axapi.selection.Cuboid
import dev.lone.itemsadder.api.CustomBlock
import java.util.Locale
import java.util.function.IntConsumer
import kotlin.math.max
import kotlin.math.min
import org.apache.commons.math3.distribution.EnumeratedDistribution
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World

class ItemsAdderFastBlockSetter(world: World, distribution: EnumeratedDistribution<String>) : BlockSetter(world, distribution) {
    private val setter = NMSHandlers.getNmsHandler().newSetter(world)

    override fun fill(cuboid: Cuboid, consumer: IntConsumer) {
        var blockCount = 0
        val chunkMinX: Int = cuboid.minX shr 4
        val chunkMaxX: Int = cuboid.maxX shr 4
        val chunkMinZ: Int = cuboid.minZ shr 4
        val chunkMaxZ: Int = cuboid.maxZ shr 4

        for (chunkX in chunkMinX..chunkMaxX) {
            val minX =
                max(cuboid.minX.toDouble(), (chunkX shl 4).toDouble()).toInt()
            val maxX = min(cuboid.maxX.toDouble(), ((chunkX shl 4) + 15).toDouble())
                .toInt()

            for (chunkZ in chunkMinZ..chunkMaxZ) {
                val minZ =
                    max(cuboid.minZ.toDouble(), (chunkZ shl 4).toDouble()).toInt()
                val maxZ = min(cuboid.maxZ.toDouble(), ((chunkZ shl 4) + 15).toDouble())
                    .toInt()

                for (y in cuboid.minY..cuboid.getMaxY()) {
                    for (x in minX..maxX) {
                        for (z in minZ..maxZ) {
                            ++blockCount
                            val sample = distribution.sample() as String
                            if (sample.contains("itemsadder:")) {
                                CustomBlock.place(sample.substring("itemsadder:".length), Location(world, x.toDouble(), y.toDouble(), z.toDouble()))
                            } else {
                                setter.setBlock(x, y, z, Material.matchMaterial(sample.uppercase(Locale.ENGLISH))?.createBlockData() ?: continue)
                            }
                        }
                    }
                }
            }
        }

        setter.finalise()

        consumer.accept(blockCount)
    }
}