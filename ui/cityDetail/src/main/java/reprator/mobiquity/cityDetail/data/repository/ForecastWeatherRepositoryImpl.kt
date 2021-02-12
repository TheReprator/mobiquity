package reprator.mobiquity.cityDetail.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.util.ConnectionDetector
import reprator.mobiquity.cityDetail.data.datasource.ForecastWeatherRemoteDataSource
import reprator.mobiquity.cityDetail.domain.repository.ForecastWeatherRepository
import reprator.mobiquity.cityDetail.modals.LocationModal
import reprator.mobiquity.cityDetail.modals.LocationRequestModal
import javax.inject.Inject

class ForecastWeatherRepositoryImpl @Inject constructor(
    private val forecastWeatherRemoteDataSource: ForecastWeatherRemoteDataSource,
    private val connectionDetector: ConnectionDetector,
) : ForecastWeatherRepository {
    override suspend fun getForeCastWeatherRepository(requestModal: LocationRequestModal): Flow<MobiQuityResult<List<LocationModal>>> {
        return if (connectionDetector.isInternetAvailable) {
            forecastWeatherRemoteDataSource.getForecastWeather(requestModal)
        } else {
            flowOf(ErrorResult(message = "No internet connection."))
        }
    }
}