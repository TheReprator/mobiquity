package reprator.mobiquity.saveCity.domain.repository

import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.saveCity.modal.LocationModal

interface GetLocationRepository {
    suspend fun getLocationList(): Flow<MobiQuityResult<List<LocationModal>>>
}