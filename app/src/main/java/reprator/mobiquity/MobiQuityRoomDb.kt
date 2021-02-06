package reprator.mobiquity

import androidx.room.Database
import androidx.room.RoomDatabase
import reprator.mobiquity.database.LocationDao
import reprator.mobiquity.database.LocationEntity

@Database(
    entities = [LocationEntity::class],
    version = 1, exportSchema = true
)
abstract class MobiQuityRoomDb : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}

