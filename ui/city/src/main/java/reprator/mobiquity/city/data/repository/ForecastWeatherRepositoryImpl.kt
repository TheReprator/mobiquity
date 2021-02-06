package reprator.mobiquity.city.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.util.ConnectionDetector
import reprator.mobiquity.city.data.datasource.ForecastWeatherRemoteDataSource
import reprator.mobiquity.city.domain.repository.ForecastWeatherRepository
import reprator.mobiquity.city.modals.LocationModal
import reprator.mobiquity.city.modals.LocationRequestModal
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