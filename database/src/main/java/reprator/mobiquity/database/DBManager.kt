package reprator.mobiquity.database

import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.base.util.safeApiCall
import javax.inject.Inject

interface DBManager {

    suspend fun saveLocation(locationEntity: LocationEntity): MobiQuityResult<Long>
    suspend fun getLocationList(): MobiQuityResult<List<LocationEntity>>
    suspend fun deleteLocation(locationEntity: LocationEntity): MobiQuityResult<Int>
    suspend fun clearTable(): MobiQuityResult<Int>
}

class DBManagerImpl @Inject constructor(private val locationDao: LocationDao) : DBManager {

    private suspend fun saveLocationDB(locationEntity: LocationEntity): MobiQuityResult<Long> {
        val data = locationDao.insertLocation(locationEntity)
        return Success(data)
    }

    override suspend fun saveLocation(locationEntity: LocationEntity): MobiQuityResult<Long> =
        safeApiCall(call = { saveLocationDB(locationEntity) })

    private suspend fun getLocationListDB(): MobiQuityResult<List<LocationEntity>> {
        val data = locationDao.getLocationList()
        return if (data.isNullOrEmpty())
            Success(emptyList<LocationEntity>())
        else
            Success(data)
    }

    override suspend fun getLocationList(): MobiQuityResult<List<LocationEntity>> =
        safeApiCall(call = { getLocationListDB() })

    private suspend fun deleteLocationDB(locationEntity: LocationEntity): MobiQuityResult<Int> {
        val longList = locationDao.deleteLocation(locationEntity)
        return Success(longList)
    }

    override suspend fun deleteLocation(locationEntity: LocationEntity): MobiQuityResult<Int> =
        safeApiCall(call = { deleteLocationDB(locationEntity) })

    private suspend fun deleteAllLocationDB(): MobiQuityResult<Int> {
        val longList = locationDao.clearTable()
        return Success(longList)
    }

    override suspend fun clearTable(): MobiQuityResult<Int> =
        safeApiCall(call =
        { deleteAllLocationDB() })
}