package reprator.mobiquity.addcity.domain.usecase

import android.location.Location
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.addcity.domain.repository.LocationRepository
import reprator.mobiquity.base.useCases.MobiQuityResult
import javax.inject.Inject

@ViewModelScoped
class LocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(locationModal: Location): Flow<MobiQuityResult<Long>> {
        return locationRepository.saveLocation(locationModal)
    }
}
