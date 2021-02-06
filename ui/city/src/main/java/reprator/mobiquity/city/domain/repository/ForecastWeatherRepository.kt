package reprator.mobiquity.city.domain.repository

import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.city.modals.LocationModal
import reprator.mobiquity.city.modals.LocationRequestModal

interface ForecastWeatherRepository {
    suspend fun getForeCastWeatherRepository(requestModal: LocationRequestModal):
            Flow<MobiQuityResult<List<LocationModal>>>
}