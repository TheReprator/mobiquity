package reprator.mobiquity.cityDetail.data.datasource

import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.cityDetail.modals.LocationModal
import reprator.mobiquity.cityDetail.modals.LocationRequestModal

interface ForecastWeatherRemoteDataSource {
    suspend fun getForecastWeather(requestModal: LocationRequestModal): Flow<MobiQuityResult<List<LocationModal>>>
}