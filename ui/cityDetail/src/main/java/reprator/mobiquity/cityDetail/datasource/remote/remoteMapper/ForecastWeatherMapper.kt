package reprator.mobiquity.cityDetail.datasource.remote.remoteMapper

import dagger.hilt.android.scopes.ViewModelScoped
import reprator.mobiquity.base.util.DateUtils
import reprator.mobiquity.base.util.DateUtils.Companion.DD_MMM_YYYY
import reprator.mobiquity.base.util.DateUtils.Companion.HOUR_MINUTE
import reprator.mobiquity.base.util.Mapper
import reprator.mobiquity.cityDetail.datasource.remote.modal.ForecastLocationEntity
import reprator.mobiquity.cityDetail.modals.LocationModal
import javax.inject.Inject

@ViewModelScoped
class ForecastWeatherMapper @Inject constructor(private val dateUtils: DateUtils) :
    Mapper<ForecastLocationEntity, List<LocationModal>> {

    override suspend fun map(from: ForecastLocationEntity): List<LocationModal> {
        val timeZone = dateUtils.convertToEpoch(from.city.timezone.toLong())
        val timeZoneEpoch = dateUtils.getTimeZone(timeZone.toInt())

        return from.list.map {
            LocationModal(
                "${from.city.name}, ${from.city.country}",
                it.weather[0].description,

                dateUtils.format(
                    dateUtils.convertToEpoch(it.dt),
                    DD_MMM_YYYY,
                    timeZoneEpoch
                ),

                it.temp.min.toString(),
                it.temp.max.toString(),

                it.pressure.toString(),
                it.humidity.toString(),

                it.speed.toString(),
                it.deg.toString(),

                dateUtils.format(
                    dateUtils.convertToEpoch(it.sunrise),
                    HOUR_MINUTE,
                    timeZoneEpoch
                ),
                dateUtils.format(
                    dateUtils.convertToEpoch(it.sunset),
                    HOUR_MINUTE,
                    timeZoneEpoch
                )
            )
        }
    }
}
