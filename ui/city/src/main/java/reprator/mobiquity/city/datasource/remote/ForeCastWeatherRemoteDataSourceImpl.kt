package reprator.mobiquity.city.datasource.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.base.util.safeApiCall
import reprator.mobiquity.base.util.toResult
import reprator.mobiquity.city.data.datasource.ForecastWeatherRemoteDataSource
import reprator.mobiquity.city.datasource.remote.remoteMapper.ForecastWeatherMapper
import reprator.mobiquity.city.modals.LocationModal
import reprator.mobiquity.city.modals.LocationRequestModal
import timber.log.Timber
import javax.inject.Inject

class ForeCastWeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val forecastWeatherMapper: ForecastWeatherMapper
) : ForecastWeatherRemoteDataSource {

    private suspend fun getForecastWeatherApi(requestModal: LocationRequestModal): Flow<MobiQuityResult<List<LocationModal>>> {
       Timber.e("forecast data ${requestModal.longitude}")
        return flow {
            weatherApiService.foreCastWeather(
                requestModal.latitude.toDouble(), requestModal.longitude.toDouble(),
                requestModal.unit, cnt = requestModal.count
            ).toResult {
                emit(Success(forecastWeatherMapper.map(it)))
            }
        }
    }

    override suspend fun getForecastWeather(requestModal: LocationRequestModal): Flow<MobiQuityResult<List<LocationModal>>> =
        safeApiCall(call = { getForecastWeatherApi(requestModal) })
}