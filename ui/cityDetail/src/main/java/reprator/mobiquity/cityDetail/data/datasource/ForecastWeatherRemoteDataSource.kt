package reprator.mobiquity.cityDetail.data.datasource

import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.cityDetail.modals.LocationModal
import reprator.mobiquity.cityDetail.modals.LocationRequestModal

interface ForecastWeatherRemoteDataSource {
    suspend fun getForecastWeather(requestModal: LocationRequestModal): MobiQuityResult<List<LocationModal>>
}