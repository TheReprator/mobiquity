package reprator.mobiquity.city.datasource.remote.remoteMapper

import reprator.mobiquity.base.util.Mapper
import reprator.mobiquity.city.datasource.remote.modal.TodayLocationEntity
import reprator.mobiquity.city.modals.LocationModal
import javax.inject.Inject

class TodayWeatherMapper @Inject constructor() :
    Mapper<TodayLocationEntity, LocationModal> {

    override suspend fun map(from: TodayLocationEntity): LocationModal {
        return LocationModal(
            from.weather[0].description,
            from.main.temp.toString(),
            from.main.tempMin.toString(),
            from.main.tempMax.toString(),

            from.main.pressure.toString(),
            from.main.humidity.toString(),

            from.wind.speed.toString(),
            from.wind.deg.toString(),

            from.sys.sunrise.toString(),
            from.sys.sunset.toString())
    }
}