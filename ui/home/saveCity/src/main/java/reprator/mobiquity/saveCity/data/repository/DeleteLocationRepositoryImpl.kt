package reprator.mobiquity.saveCity.data.repository

import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.database.DBManager
import reprator.mobiquity.saveCity.modal.LocationModal
import reprator.mobiquity.saveCity.data.repository.mapper.DeleteLocationMapper
import reprator.mobiquity.saveCity.domain.repository.DeleteLocationRepository
import javax.inject.Inject

class DeleteLocationRepositoryImpl @Inject constructor(
    private val locationMapper: DeleteLocationMapper,
    private val dbManager: DBManager
) : DeleteLocationRepository {

    override suspend fun deleteLocation(locationModal: LocationModal): Flow<MobiQuityResult<Int>> {
        return dbManager.deleteLocation(locationMapper.map(locationModal))
    }
}

