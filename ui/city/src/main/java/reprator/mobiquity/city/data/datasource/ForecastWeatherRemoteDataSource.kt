package reprator.mobiquity.city.data.datasource

import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.city.modals.LocationModal
import reprator.mobiquity.city.modals.LocationRequestModal

interface ForecastWeatherRemoteDataSource {
    suspend fun getForecastWeather(requestModal: LocationRequestModal): Flow<MobiQuityResult<List<LocationModal>>>
}