package reprator.mobiquity.city.domain.usecase

import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.city.domain.repository.ForecastWeatherRepository
import reprator.mobiquity.city.modals.LocationModal
import reprator.mobiquity.city.modals.LocationRequestModal
import javax.inject.Inject

class ForecastWeatherUseCase @Inject constructor(
    private val forecastWeatherRepository: ForecastWeatherRepository
) {
    suspend operator fun invoke(requestModal: LocationRequestModal): Flow<MobiQuityResult<List<LocationModal>>> {
        return forecastWeatherRepository.getForeCastWeatherRepository(requestModal)
    }
}
