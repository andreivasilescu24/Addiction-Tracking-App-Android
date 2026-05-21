package com.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addictions")
data class Addiction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: AddictionCategory,
    val targetDays: Int,
    val startDateMillis: Long = System.currentTimeMillis()
)
