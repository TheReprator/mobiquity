package reprator.mobiquity.saveCity.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.saveCity.modal.LocationModal
import reprator.mobiquity.saveCity.domain.repository.GetLocationRepository
import javax.inject.Inject

@ViewModelScoped
class GetLocationUseCase @Inject constructor(
    private val locationRepository: GetLocationRepository
) {
    suspend operator fun invoke(): Flow<MobiQuityResult<List<LocationModal>>> {
        return locationRepository.getLocationList()
    }
}
