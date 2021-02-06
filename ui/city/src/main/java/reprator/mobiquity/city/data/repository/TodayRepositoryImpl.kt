package reprator.mobiquity.city.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.util.ConnectionDetector
import reprator.mobiquity.city.data.datasource.TodayRemoteDataSource
import reprator.mobiquity.city.domain.repository.GetTodayWeatherRepository
import reprator.mobiquity.city.modals.LocationModal
import reprator.mobiquity.city.modals.LocationRequestModal
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
