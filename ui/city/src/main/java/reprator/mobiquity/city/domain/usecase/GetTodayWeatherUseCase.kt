package reprator.mobiquity.city.domain.usecase

import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.city.domain.repository.GetTodayWeatherRepository
import reprator.mobiquity.city.modals.LocationModal
import reprator.mobiquity.city.modals.LocationRequestModal
import timber.log.Timber
import javax.inject.Inject

class GetTodayWeatherUseCase @Inject constructor(private val getTodayWeatherRepository: GetTodayWeatherRepository) {

    suspend operator fun invoke(requestModal: LocationRequestModal): Flow<MobiQuityResult<LocationModal>> {
        return getTodayWeatherRepository.getTodayWeather(requestModal)
    }
}
