package reprator.mobiquity.addcity.data.repository

import android.location.Location
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import reprator.mobiquity.addcity.data.repository.mapper.LocationMapper
import reprator.mobiquity.addcity.domain.repository.LocationRepository
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.database.DBManager
import javax.inject.Inject

@ViewModelScoped
class LocationRepositoryImpl @Inject constructor(
    private val locationMapper: LocationMapper,
    private val dbManager: DBManager
) : LocationRepository {

    override suspend fun saveLocation(locationModal: Location): Flow<MobiQuityResult<Long>> {
        return flowOf(dbManager.saveLocation(locationMapper.map(locationModal)))
    }
}

