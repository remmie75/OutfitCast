package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("SELECT * FROM saved_locations ORDER BY isDefault DESC, addedTimestamp DESC")
    fun getAllSavedLocations(): Flow<List<SavedLocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: SavedLocationEntity)

    @Query("DELETE FROM saved_locations WHERE id = :id")
    suspend fun deleteLocationById(id: Long)

    @Query("UPDATE saved_locations SET isDefault = (id = :id)")
    suspend fun setDefaultLocation(id: Long)

    @Query("SELECT * FROM saved_locations WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultLocation(): SavedLocationEntity?
}
