package com.artillexstudios.axmines.mines.setter

import com.artillexstudios.axapi.selection.Cuboid
import java.util.function.IntConsumer
import org.apache.commons.math3.distribution.EnumeratedDistribution
import org.bukkit.World

abstract class BlockSetter(val world: World, val distribution: EnumeratedDistribution<*>) {

    abstract fun fill(cuboid: Cuboid, consumer: IntConsumer)
}