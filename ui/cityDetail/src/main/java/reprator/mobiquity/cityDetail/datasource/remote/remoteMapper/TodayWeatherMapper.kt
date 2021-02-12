package reprator.mobiquity.cityDetail.datasource.remote.remoteMapper

import reprator.mobiquity.base.util.DateUtils
import reprator.mobiquity.base.util.Mapper
import reprator.mobiquity.cityDetail.datasource.remote.modal.TodayLocationEntity
import reprator.mobiquity.cityDetail.modals.LocationModal
import javax.inject.Inject

class TodayWeatherMapper @Inject constructor(private val dateUtils: DateUtils) :
    Mapper<TodayLocationEntity, LocationModal> {

    override suspend fun map(from: TodayLocationEntity): LocationModal {
        val timeZone = dateUtils.convertToEpoch(from.timezone)
        val timeZoneEpoch = dateUtils.getTimeZone(timeZone.toInt())

        return LocationModal(
            from.name,
            from.weather[0].description,

            dateUtils.format(
                dateUtils.convertToEpoch(from.dt),
                DateUtils.DD_MMM_YYYY,
                timeZoneEpoch
            ),

            from.main.tempMin.toString(),
            from.main.tempMax.toString(),

            from.main.pressure.toString(),
            from.main.humidity.toString(),

            from.wind.speed.toString(),
            from.wind.deg.toString(),

            dateUtils.format(
                dateUtils.convertToEpoch(from.sys.sunrise),
                DateUtils.HOUR_MINUTE,
                timeZoneEpoch
            ),
            dateUtils.format(
                dateUtils.convertToEpoch(from.sys.sunset),
                DateUtils.HOUR_MINUTE,
                timeZoneEpoch
            )
        )
    }
}