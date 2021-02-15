package reprator.mobiquity.saveCity.data.repository

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.database.DBManager
import reprator.mobiquity.saveCity.data.repository.mapper.DeleteLocationMapper
import reprator.mobiquity.saveCity.domain.repository.DeleteLocationRepository
import reprator.mobiquity.saveCity.modal.LocationModal
import javax.inject.Inject

@ViewModelScoped
class DeleteLocationRepositoryImpl @Inject constructor(
    private val locationMapper: DeleteLocationMapper,
    private val dbManager: DBManager
) : DeleteLocationRepository {

    override suspend fun deleteLocation(locationModal: LocationModal): Flow<MobiQuityResult<Int>> {
        return flowOf(dbManager.deleteLocation(locationMapper.map(locationModal)))
    }
}

