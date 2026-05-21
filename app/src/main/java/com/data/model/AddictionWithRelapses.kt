package com.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class AddictionWithRelapses(
    @Embedded val addiction: Addiction,
    @Relation(
        parentColumn = "id",
        entityColumn = "addictionId"
    )
    val relapses: List<Relapse>
)
