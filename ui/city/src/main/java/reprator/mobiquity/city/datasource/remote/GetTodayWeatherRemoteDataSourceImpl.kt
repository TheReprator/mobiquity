package reprator.mobiquity.city.datasource.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.base.util.safeApiCall
import reprator.mobiquity.base.util.toResult
import reprator.mobiquity.city.data.datasource.TodayRemoteDataSource
import reprator.mobiquity.city.datasource.remote.remoteMapper.TodayWeatherMapper
import reprator.mobiquity.city.modals.LocationModal
import reprator.mobiquity.city.modals.LocationRequestModal
import timber.log.Timber
import javax.inject.Inject

class GetTodayWeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val todayWeatherMapper: TodayWeatherMapper
) : TodayRemoteDataSource {

    private suspend fun getTodayWeatherApi(requestModal: LocationRequestModal): Flow<MobiQuityResult<LocationModal>> {
        Timber.e("today data ${requestModal.longitude}")
        return flow {
            weatherApiService.todayWeather(
                requestModal.latitude.toDouble(), requestModal.longitude.toDouble(),
                requestModal.unit
            ).toResult {
                emit(Success(todayWeatherMapper.map(it)))
            }
        }
    }

    override suspend fun getTodayWeather(requestModal: LocationRequestModal): Flow<MobiQuityResult<LocationModal>> =
        safeApiCall(call = { getTodayWeatherApi(requestModal) })
}