package reprator.mobiquity.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.base.util.safeApiCall
import javax.inject.Inject

interface DBManager {

    suspend fun saveLocation(locationEntity: LocationEntity): Flow<MobiQuityResult<Long>>
    suspend fun getLocationList(): Flow<MobiQuityResult<List<LocationEntity>>>
    suspend fun deleteLocation(locationEntity: LocationEntity): Flow<MobiQuityResult<Int>>
    suspend fun clearTable(): Flow<MobiQuityResult<Int>>
}

class DBManagerImpl @Inject constructor(private val locationDao: LocationDao) : DBManager {

    private suspend fun saveLocationDB(locationEntity: LocationEntity): Flow<MobiQuityResult<Long>> {
        val data = locationDao.insertLocation(locationEntity)
        return flowOf(Success(data))
    }

    override suspend fun saveLocation(locationEntity: LocationEntity): Flow<MobiQuityResult<Long>> =
        safeApiCall(call = { saveLocationDB(locationEntity) })

    private suspend fun getLocationListDB(): Flow<MobiQuityResult<List<LocationEntity>>> {
        return flow {
            val data = locationDao.getLocationList()
            if (data.isNullOrEmpty())
                emit(Success(emptyList<LocationEntity>()))
            else
                emit(Success(data))
        }
    }

    override suspend fun getLocationList(): Flow<MobiQuityResult<List<LocationEntity>>> =
        safeApiCall(call = { getLocationListDB() })

    private suspend fun deleteLocationDB(locationEntity: LocationEntity): Flow<MobiQuityResult<Int>> {
        val longList = locationDao.deleteLocation(locationEntity)
        return flowOf(Success(longList))
    }

    override suspend fun deleteLocation(locationEntity: LocationEntity): Flow<MobiQuityResult<Int>> =
        safeApiCall(call = { deleteLocationDB(locationEntity) })

    private suspend fun deleteAllLocationDB(): Flow<MobiQuityResult<Int>> {
        val longList = locationDao.clearTable()
        return flowOf(Success(longList))
    }

    override suspend fun clearTable(): Flow<MobiQuityResult<Int>> =
        safeApiCall(call = { deleteAllLocationDB() })
}