package reprator.mobiquity.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(locationEntity: LocationEntity): Long

    @Query("SELECT * FROM locations")
    fun getLocationList(): List<LocationEntity>

    @Delete
    suspend fun deleteLocation(locationEntity: LocationEntity): Int

    @Query("DELETE FROM locations")
    suspend fun clearTable(): Int
}
