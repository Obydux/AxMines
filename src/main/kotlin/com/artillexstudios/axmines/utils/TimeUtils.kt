package com.artillexstudios.axmines.utils

import com.artillexstudios.axmines.AxMinesPlugin
import com.artillexstudios.axmines.mines.Mine
import java.time.Duration

object TimeUtils {

    fun format(time: Long, mine: Mine): String {
        if (time < 0) return "---"

        val remainingTime: Duration = Duration.ofMillis(time)
        val total: Long = remainingTime.seconds
        val days = total / 86400
        val hours = (total % 86400) / 3600
        val minutes = (total % 3600) / 60
        val seconds = total % 60

        when (mine.config.TIMER_FORMAT) {
            1 -> {
                if (days > 0) return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds)
                if (hours > 0) return String.format("%02d:%02d:%02d", hours, minutes, seconds)
                return String.format("%02d:%02d", minutes, seconds)
            }

            2 -> {
                if (days > 0) return days.toString() + AxMinesPlugin.MESSAGES.DAY
                if (hours > 0) return hours.toString() + AxMinesPlugin.MESSAGES.HOUR
                if (minutes > 0) return minutes.toString() + AxMinesPlugin.MESSAGES.MINUTE
                return seconds.toString() + AxMinesPlugin.MESSAGES.SECOND
            }

            else -> {
                if (days > 0) return java.lang.String.format(
                    ((("%02d" + AxMinesPlugin.MESSAGES.DAY) + " %02d" + AxMinesPlugin.MESSAGES.HOUR) + " %02d" + AxMinesPlugin.MESSAGES.MINUTE) + " %02d" + AxMinesPlugin.MESSAGES.SECOND,
                    days,
                    hours,
                    minutes,
                    seconds
                )
                if (hours > 0) return java.lang.String.format(
                    (("%02d" + AxMinesPlugin.MESSAGES.HOUR) + " %02d" + AxMinesPlugin.MESSAGES.MINUTE) + " %02d" + AxMinesPlugin.MESSAGES.SECOND,
                    hours,
                    minutes,
                    seconds
                )
                return java.lang.String.format(
                    ("%02d" + AxMinesPlugin.MESSAGES.MINUTE) + " %02d" + AxMinesPlugin.MESSAGES.SECOND,
                    minutes,
                    seconds
                )
            }
        }
    }
}
