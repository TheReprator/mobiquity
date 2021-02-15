package reprator.mobiquity.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import reprator.mobiquity.database.DBManager
import reprator.mobiquity.database.DBManagerImpl
import reprator.mobiquity.database.LocationDao
import reprator.mobiquity.implementation.MobiQuityRoomDb
import timber.log.Timber
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DBModule {

    companion object {
        private const val DATABASE_NAME = "currency_database"
    }

    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Timber.d("RoomDatabaseModule onCreate")
        }
    }

    @Provides
    fun providesRoomDatabase(@ApplicationContext context: Context): MobiQuityRoomDb {
        return Room.databaseBuilder(context, MobiQuityRoomDb::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .addCallback(databaseCallback)
            .build()
    }

    @Singleton
    @Provides
    fun providesCategoryDAO(mobiQuityRoomDb: MobiQuityRoomDb): LocationDao =
        mobiQuityRoomDb.locationDao()

    @Singleton
    @Provides
    fun providesDBManager(locationDao: LocationDao): DBManager =
        DBManagerImpl(locationDao)
}