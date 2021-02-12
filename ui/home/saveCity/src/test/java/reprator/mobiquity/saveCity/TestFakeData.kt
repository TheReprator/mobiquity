package reprator.mobiquity.saveCity

import reprator.mobiquity.database.LocationEntity
import reprator.mobiquity.saveCity.modal.LocationModal

object TestFakeData {

    fun getLocationModal(): LocationModal {
        return LocationModal("20.21,76.35", "London, UK")
    }

    fun getLocationModalList(): List<LocationModal> {
        return listOf(LocationModal("20.21,76.35", "London, UK"))
    }

    fun getLocationEntity(): LocationEntity {
        return LocationEntity("20.21,76.35", "London, UK")
    }

    fun getLocationEntityList(): List<LocationEntity> {
        return listOf(LocationEntity("20.21,76.35", "London, UK"))
    }
}