package reprator.mobiquity.addcity

import android.location.Location
import reprator.mobiquity.database.LocationEntity

object TestFakeData {

    fun getLocationModal(): Location {
        val location = Location("London, UK")
        location.latitude = 32.21
        location.longitude = 76.34

        return location
    }

    fun getLocationModalList(): List<Location> {
        return listOf(getLocationModal())
    }

    fun getLocationEntity(): LocationEntity {
        return LocationEntity("20.21,76.35", "London, UK")
    }

    fun getLocationEntityList(): List<LocationEntity> {
        return listOf(LocationEntity("20.21,76.35", "London, UK"))
    }
}