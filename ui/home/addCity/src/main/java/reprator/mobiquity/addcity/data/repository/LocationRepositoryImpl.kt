package reprator.mobiquity.addcity.data.repository

import android.location.Location
import kotlinx.coroutines.flow.*
import reprator.mobiquity.addcity.data.repository.mapper.LocationMapper
import reprator.mobiquity.addcity.domain.repository.LocationRepository
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.database.DBManager
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationMapper: LocationMapper,
    private val dbManager: DBManager
) : LocationRepository {

    override suspend fun saveLocation(locationModal: Location): Flow<MobiQuityResult<Long>> {
        return dbManager.saveLocation(locationMapper.map(locationModal))
    }
}

