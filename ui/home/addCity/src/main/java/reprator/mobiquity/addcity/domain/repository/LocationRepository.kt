package reprator.mobiquity.addcity.domain.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult

interface LocationRepository {
    suspend fun saveLocation(locationModal: Location): Flow<MobiQuityResult<Long>>
}