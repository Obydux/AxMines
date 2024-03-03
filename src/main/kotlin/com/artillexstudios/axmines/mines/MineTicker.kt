package com.artillexstudios.axmines.mines

import com.artillexstudios.axapi.scheduler.Scheduler

object MineTicker {

    private fun tickAll() {
        Mines.getTypes().forEach { (_, mine) ->
            mine.tick()
        }
    }

    fun schedule() {
        Scheduler.get().runTimer({ _ ->
            tickAll()
        }, 1L, 1L)
    }
}