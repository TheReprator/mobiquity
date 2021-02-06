package reprator.mobiquity.saveCity.domain.usecase

import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.saveCity.modal.LocationModal
import reprator.mobiquity.saveCity.domain.repository.DeleteLocationRepository
import javax.inject.Inject

class DeleteLocationUseCase @Inject constructor(
    private val locationRepository: DeleteLocationRepository
) {
    suspend operator fun invoke(locationModal: LocationModal): Flow<MobiQuityResult<Int>> {
        return locationRepository.deleteLocation(locationModal)
    }
}
