package com.artillexstudios.axmines.mines.setter

import com.artillexstudios.axapi.nms.NMSHandlers
import com.artillexstudios.axapi.selection.Cuboid
import java.util.function.IntConsumer
import kotlin.math.max
import kotlin.math.min
import org.apache.commons.math3.distribution.EnumeratedDistribution
import org.bukkit.World
import org.bukkit.block.data.BlockData

class FastBlockSetter(world: World) : BlockSetter(world) {
    private val setter = NMSHandlers.getNmsHandler().newSetter(world)

    override fun fill(cuboid: Cuboid, distribution: EnumeratedDistribution<BlockData>, consumer: IntConsumer) {
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
                            setter.setBlock(x, y, z, distribution.sample())
                        }
                    }
                }
            }
        }

        setter.finalise()

        consumer.accept(blockCount)
    }
}