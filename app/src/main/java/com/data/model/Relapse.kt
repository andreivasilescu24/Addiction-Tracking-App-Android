package com.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "relapses",
    foreignKeys = [
        ForeignKey(
            entity = Addiction::class,
            parentColumns = ["id"],
            childColumns = ["addictionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Relapse(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val addictionId: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String
)
