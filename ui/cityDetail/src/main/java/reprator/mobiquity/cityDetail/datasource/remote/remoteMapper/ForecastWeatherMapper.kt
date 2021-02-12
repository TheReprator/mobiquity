package reprator.mobiquity.cityDetail.datasource.remote.remoteMapper

import reprator.mobiquity.base.util.Mapper
import reprator.mobiquity.cityDetail.datasource.remote.modal.ForecastLocationEntity
import reprator.mobiquity.cityDetail.modals.LocationModal
import javax.inject.Inject

class ForecastWeatherMapper @Inject constructor() :
    Mapper<ForecastLocationEntity, List<LocationModal>> {

    override suspend fun map(from: ForecastLocationEntity): List<LocationModal> {
        return from.list.map {
            LocationModal(
                it.weather[0].description,
                it.main.temp.toString(),
                it.main.tempMin.toString(),
                it.main.tempMax.toString(),

                it.main.pressure.toString(),
                it.main.humidity.toString(),

                it.wind.speed.toString(),
                it.wind.deg.toString(),

                it.sys.sunrise.toString(),
                it.sys.sunset.toString()
            )
        }
    }
}
