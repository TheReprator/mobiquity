package reprator.mobiquity.cityDetail.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base_android.util.wrapEspressoIdlingResource
import reprator.mobiquity.cityDetail.domain.repository.ForecastWeatherRepository
import reprator.mobiquity.cityDetail.modals.LocationModal
import reprator.mobiquity.cityDetail.modals.LocationRequestModal
import javax.inject.Inject

@ViewModelScoped
class ForecastWeatherUseCase @Inject constructor(
    private val forecastWeatherRepository: ForecastWeatherRepository
) {
    suspend operator fun invoke(requestModal: LocationRequestModal): Flow<MobiQuityResult<List<LocationModal>>> {

        wrapEspressoIdlingResource {
            return forecastWeatherRepository.getForeCastWeatherRepository(requestModal)
        }
    }
}
