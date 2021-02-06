package reprator.mobiquity.saveCity.data.repository.mapper

import reprator.mobiquity.base.util.Mapper
import reprator.mobiquity.database.LocationEntity
import reprator.mobiquity.saveCity.modal.LocationModal
import javax.inject.Inject

class LocationMapper @Inject constructor() :
    Mapper<List<LocationEntity>, List<LocationModal>> {
    override suspend fun map(from: List<LocationEntity>): List<LocationModal> {
        return from.map {
            LocationModal(it.latLong, it.address)
        }
    }

}