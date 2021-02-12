package reprator.mobiquity.cityDetail.domain.repository

import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.cityDetail.modals.LocationModal
import reprator.mobiquity.cityDetail.modals.LocationRequestModal

interface ForecastWeatherRepository {
    suspend fun getForeCastWeatherRepository(requestModal: LocationRequestModal):
            Flow<MobiQuityResult<List<LocationModal>>>
}