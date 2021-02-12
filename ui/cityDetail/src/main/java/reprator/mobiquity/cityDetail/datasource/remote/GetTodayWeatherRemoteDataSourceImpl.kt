package reprator.mobiquity.cityDetail.datasource.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.base.util.safeApiCall
import reprator.mobiquity.base.util.toResult
import reprator.mobiquity.cityDetail.data.datasource.TodayRemoteDataSource
import reprator.mobiquity.cityDetail.datasource.remote.remoteMapper.TodayWeatherMapper
import reprator.mobiquity.cityDetail.modals.LocationModal
import reprator.mobiquity.cityDetail.modals.LocationRequestModal
import timber.log.Timber
import javax.inject.Inject

class GetTodayWeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val todayWeatherMapper: TodayWeatherMapper
) : TodayRemoteDataSource {

    private suspend fun getTodayWeatherApi(requestModal: LocationRequestModal): Flow<MobiQuityResult<LocationModal>> {
        Timber.e("today data ${requestModal.longitude}")
        return flow {
            val result = weatherApiService.todayWeather(
                requestModal.latitude.toDouble(), requestModal.longitude.toDouble(),
                requestModal.unit
            ).toResult()

            when (result) {
                is Success -> {
                    emit(Success(todayWeatherMapper.map(result.data)))
                }
                is ErrorResult -> {
                    emit(ErrorResult(message = result.message, throwable = result.throwable))
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

    override suspend fun getTodayWeather(requestModal: LocationRequestModal): Flow<MobiQuityResult<LocationModal>> =
        safeApiCall(call = { getTodayWeatherApi(requestModal) })
}