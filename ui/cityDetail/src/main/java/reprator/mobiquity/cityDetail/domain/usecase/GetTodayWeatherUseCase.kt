package reprator.mobiquity.cityDetail.domain.usecase

import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.cityDetail.domain.repository.GetTodayWeatherRepository
import reprator.mobiquity.cityDetail.modals.LocationModal
import reprator.mobiquity.cityDetail.modals.LocationRequestModal
import javax.inject.Inject

class GetTodayWeatherUseCase @Inject constructor(private val getTodayWeatherRepository: GetTodayWeatherRepository) {

    suspend operator fun invoke(requestModal: LocationRequestModal): Flow<MobiQuityResult<LocationModal>> {
        return getTodayWeatherRepository.getTodayWeather(requestModal)
    }
}
