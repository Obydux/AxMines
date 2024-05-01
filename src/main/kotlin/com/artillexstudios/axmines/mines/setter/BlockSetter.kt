package com.artillexstudios.axmines.mines.setter

import com.artillexstudios.axapi.selection.Cuboid
import java.util.function.IntConsumer
import org.apache.commons.math3.distribution.EnumeratedDistribution
import org.bukkit.World
import org.bukkit.block.data.BlockData

abstract class BlockSetter(val world: World) {

    abstract fun fill(cuboid: Cuboid, distribution: EnumeratedDistribution<BlockData>, consumer: IntConsumer)
}