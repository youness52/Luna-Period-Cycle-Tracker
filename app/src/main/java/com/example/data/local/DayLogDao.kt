package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DayLogDao {
    @Query("SELECT * FROM day_logs ORDER BY dateString DESC")
    fun getAllLogs(): Flow<List<DayLogEntity>>

    @Query("SELECT * FROM day_logs WHERE dateString = :dateString LIMIT 1")
    fun getLogForDate(dateString: String): Flow<DayLogEntity?>

    @Query("SELECT * FROM day_logs WHERE isPeriod = 1 ORDER BY dateString ASC")
    fun getPeriodLogs(): Flow<List<DayLogEntity>>

    @Query("SELECT * FROM day_logs WHERE dateString BETWEEN :startDate AND :endDate ORDER BY dateString ASC")
    fun getLogsBetween(startDate: String, endDate: String): Flow<List<DayLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(log: DayLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<DayLogEntity>)

    @Query("DELETE FROM day_logs WHERE dateString = :dateString")
    suspend fun deleteLog(dateString: String)

    @Query("DELETE FROM day_logs")
    suspend fun clearAllLogs()
}
