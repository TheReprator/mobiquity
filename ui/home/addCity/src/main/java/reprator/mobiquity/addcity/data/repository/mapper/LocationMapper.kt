package reprator.mobiquity.addcity.data.repository.mapper

import android.location.Location
import reprator.mobiquity.base.util.Mapper
import reprator.mobiquity.database.LocationEntity
import javax.inject.Inject

class LocationMapper @Inject constructor() :
    Mapper<Location, LocationEntity> {

    override suspend fun map(from: Location): LocationEntity {
        val latLng = "${from.latitude}, ${from.longitude}"
        return LocationEntity(latLng, from.provider)
    }
}