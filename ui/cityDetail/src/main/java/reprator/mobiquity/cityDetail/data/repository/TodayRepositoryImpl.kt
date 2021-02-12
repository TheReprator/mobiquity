package reprator.mobiquity.cityDetail.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.util.ConnectionDetector
import reprator.mobiquity.cityDetail.data.datasource.TodayRemoteDataSource
import reprator.mobiquity.cityDetail.domain.repository.GetTodayWeatherRepository
import reprator.mobiquity.cityDetail.modals.LocationModal
import reprator.mobiquity.cityDetail.modals.LocationRequestModal
import javax.inject.Inject

class TodayRepositoryImpl @Inject constructor(
    private val todayRemoteDataSource: TodayRemoteDataSource,
    private val connectionDetector: ConnectionDetector
) : GetTodayWeatherRepository {

    override suspend fun getTodayWeather(requestModal: LocationRequestModal): Flow<MobiQuityResult<LocationModal>> {
        return if (connectionDetector.isInternetAvailable) {
            todayRemoteDataSource.getTodayWeather(requestModal)
        } else {
            flowOf(ErrorResult(message = "No internet connection."))
        }
    }
}
