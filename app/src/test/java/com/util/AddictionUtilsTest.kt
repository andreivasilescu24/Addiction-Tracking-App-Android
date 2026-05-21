package com.util

import com.data.model.Relapse
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit

class AddictionUtilsTest {

    @Test
    fun `calculateStreak with no relapses returns correct days`() {
        val days = 5
        val currentTime = System.currentTimeMillis()
        val startDate = currentTime - TimeUnit.DAYS.toMillis(days.toLong())
        
        val streak = AddictionUtils.calculateStreak(startDate, emptyList(), currentTime)
        
        assertEquals(days, streak)
    }

    @Test
    fun `calculateStreak with relapses returns days since last relapse`() {
        val currentTime = System.currentTimeMillis()
        val startDate = currentTime - TimeUnit.DAYS.toMillis(10)
        val relapses = listOf(
            Relapse(id = 1, addictionId = 1, timestamp = currentTime - TimeUnit.DAYS.toMillis(3), note = "Relapse 1"),
            Relapse(id = 2, addictionId = 1, timestamp = currentTime - TimeUnit.DAYS.toMillis(7), note = "Relapse 2")
        )
        
        val streak = AddictionUtils.calculateStreak(startDate, relapses, currentTime)
        
        assertEquals(3, streak)
    }

    @Test
    fun `calculateProgress returns correct float`() {
        assertEquals(0.5f, AddictionUtils.calculateProgress(5, 10), 0.001f)
        assertEquals(1.0f, AddictionUtils.calculateProgress(15, 10), 0.001f)
        assertEquals(0.0f, AddictionUtils.calculateProgress(0, 10), 0.001f)
    }

    @Test
    fun `sanitizeInput removes special characters`() {
        val input = "Smoking'; DROP TABLE addictions;--"
        val output = AddictionUtils.sanitizeInput(input)
        
        // Semicolon and quote removed, but dashes kept as per regex
        assertEquals("Smoking DROP TABLE addictions--", output)
    }

    @Test
    fun `getMilestones marks correct achieved status`() {
        val streak = 7
        val milestones = AddictionUtils.getMilestones(streak, "English")
        
        // 1 Day, 3 Days, 1 Week (7 days) should be achieved
        assertEquals(true, milestones.find { it.targetDays == 1 }?.isAchieved)
        assertEquals(true, milestones.find { it.targetDays == 3 }?.isAchieved)
        assertEquals(true, milestones.find { it.targetDays == 7 }?.isAchieved)
        
        // 2 Weeks, 1 Month etc should not be achieved
        assertEquals(false, milestones.find { it.targetDays == 14 }?.isAchieved)
    }

    @Test
    fun `getMilestones returns correct language titles`() {
        val roMilestones = AddictionUtils.getMilestones(0, "Romanian")
        val enMilestones = AddictionUtils.getMilestones(0, "English")
        
        assertEquals("1 Zi", roMilestones.find { it.targetDays == 1 }?.title)
        assertEquals("1 Day", enMilestones.find { it.targetDays == 1 }?.title)
    }
}
