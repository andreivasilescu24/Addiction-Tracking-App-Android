package com.data.dao

import androidx.room.*
import com.data.model.Addiction
import com.data.model.AddictionWithRelapses
import com.data.model.Relapse
import kotlinx.coroutines.flow.Flow

@Dao
interface AddictionDao {

    @Upsert
    suspend fun upsertAddiction(addiction: Addiction)

    @Delete
    suspend fun deleteAddiction(addiction: Addiction)

    @Transaction
    @Query("SELECT * FROM addictions")
    fun getAllAddictionsWithRelapses(): Flow<List<AddictionWithRelapses>>

    @Transaction
    @Query("SELECT * FROM addictions WHERE id = :addictionId")
    fun getAddictionWithRelapsesById(addictionId: Long): Flow<AddictionWithRelapses?>

    @Insert
    suspend fun insertRelapse(relapse: Relapse)

    @Query("SELECT * FROM relapses WHERE addictionId = :addictionId ORDER BY timestamp DESC")
    fun getRelapsesForAddiction(addictionId: Long): Flow<List<Relapse>>

    @Query("DELETE FROM relapses WHERE addictionId = :addictionId")
    suspend fun deleteRelapsesForAddiction(addictionId: Long)
}
