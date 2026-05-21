package com.util

import com.data.model.Milestone
import com.data.model.Relapse
import java.util.concurrent.TimeUnit

object AddictionUtils {

    /**
     * Calculates the current streak in days.
     * If there are relapses, it calculates from the latest relapse.
     * If no relapses, it calculates from the start date.
     */
    fun calculateStreak(
        startDateMillis: Long, 
        relapses: List<Relapse>, 
        currentTime: Long = System.currentTimeMillis()
    ): Int {
        val latestEventTime = if (relapses.isEmpty()) {
            startDateMillis
        } else {
            relapses.maxOf { it.timestamp }
        }

        val diffMillis = currentTime - latestEventTime
        
        return if (diffMillis > 0) {
            TimeUnit.MILLISECONDS.toDays(diffMillis).toInt()
        } else {
            0
        }
    }

    /**
     * Calculates progress as a float between 0.0 and 1.0
     */
    fun calculateProgress(streakDays: Int, targetDays: Int): Float {
        if (targetDays <= 0) return 0f
        return (streakDays.toFloat() / targetDays.toFloat()).coerceIn(0f, 1f)
    }

    /**
     * Returns a list of milestones, marking them as achieved based on the current streak.
     */
    fun getMilestones(streakDays: Int, language: String = "English"): List<Milestone> {
        val standardMilestones = listOf(
            Milestone(if (language == "Romanian") "1 Zi" else "1 Day", 1, false),
            Milestone(if (language == "Romanian") "3 Zile" else "3 Days", 3, false),
            Milestone(if (language == "Romanian") "1 Săptămână" else "1 Week", 7, false),
            Milestone(if (language == "Romanian") "2 Săptămâni" else "2 Weeks", 14, false),
            Milestone(if (language == "Romanian") "1 Lună" else "1 Month", 30, false),
            Milestone(if (language == "Romanian") "3 Luni" else "3 Months", 90, false)
        )

        return standardMilestones.map { milestone ->
            milestone.copy(isAchieved = streakDays >= milestone.targetDays)
        }
    }

    /**
     * Simple input sanitization to prevent basic script injection or weird characters
     * by allowing only alphanumeric characters, spaces, and basic punctuation.
     */
    fun sanitizeInput(input: String): String {
        return input.trim().replace(Regex("[^a-zA-Z0-9\\s,.!?-]"), "")
    }
}
