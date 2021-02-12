package reprator.mobiquity.cityDetail.domain.usecase

import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.cityDetail.domain.repository.ForecastWeatherRepository
import reprator.mobiquity.cityDetail.modals.LocationModal
import reprator.mobiquity.cityDetail.modals.LocationRequestModal
import javax.inject.Inject

class ForecastWeatherUseCase @Inject constructor(
    private val forecastWeatherRepository: ForecastWeatherRepository
) {
    suspend operator fun invoke(requestModal: LocationRequestModal): Flow<MobiQuityResult<List<LocationModal>>> {
        return forecastWeatherRepository.getForeCastWeatherRepository(requestModal)
    }
}
