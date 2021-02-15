package reprator.mobiquity.saveCity.data.repository.mapper

import dagger.hilt.android.scopes.ViewModelScoped
import reprator.mobiquity.base.util.Mapper
import reprator.mobiquity.database.LocationEntity
import reprator.mobiquity.saveCity.modal.LocationModal
import javax.inject.Inject

@ViewModelScoped
class DeleteLocationMapper @Inject constructor() :
    Mapper<LocationModal, LocationEntity> {

    override suspend fun map(from: LocationModal): LocationEntity {
        return LocationEntity(from.latLong, from.address)
    }
}