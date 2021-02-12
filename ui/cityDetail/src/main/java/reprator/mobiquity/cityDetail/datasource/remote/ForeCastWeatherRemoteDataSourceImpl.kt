package reprator.mobiquity.cityDetail.datasource.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.base.util.safeApiCall
import reprator.mobiquity.base.util.toResult
import reprator.mobiquity.cityDetail.data.datasource.ForecastWeatherRemoteDataSource
import reprator.mobiquity.cityDetail.datasource.remote.remoteMapper.ForecastWeatherMapper
import reprator.mobiquity.cityDetail.modals.LocationModal
import reprator.mobiquity.cityDetail.modals.LocationRequestModal
import timber.log.Timber
import javax.inject.Inject

class ForeCastWeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val forecastWeatherMapper: ForecastWeatherMapper
) : ForecastWeatherRemoteDataSource {

    private suspend fun getForecastWeatherApi(requestModal: LocationRequestModal): Flow<MobiQuityResult<List<LocationModal>>> {
        return flow {
            val data = weatherApiService.foreCastWeather(
                requestModal.latitude.toDouble(), requestModal.longitude.toDouble(),
                requestModal.unit, cnt = requestModal.count
            ).toResult()

            when (data) {
                is Success -> {
                    emit(Success(forecastWeatherMapper.map(data.data)))
                }
                is ErrorResult -> {
                    emit(ErrorResult(message = data.message, throwable = data.throwable))
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

    override suspend fun getForecastWeather(requestModal: LocationRequestModal): Flow<MobiQuityResult<List<LocationModal>>> =
        safeApiCall(call = { getForecastWeatherApi(requestModal) })
}