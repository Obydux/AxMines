package com.artillexstudios.axmines.mines.setter

import com.artillexstudios.axapi.nms.NMSHandlers
import com.artillexstudios.axapi.selection.Cuboid
import java.util.function.IntConsumer
import org.apache.commons.math3.distribution.EnumeratedDistribution
import org.bukkit.World
import org.bukkit.block.data.BlockData

class ParallelBlockSetter(world: World, distribution: EnumeratedDistribution<BlockData>) : BlockSetter(world, distribution) {
    private val setter = NMSHandlers.getNmsHandler().newParallelSetter(world)

    override fun fill(cuboid: Cuboid, consumer: IntConsumer) {
        setter.fill(cuboid, distribution as EnumeratedDistribution<BlockData>, consumer)
    }
}