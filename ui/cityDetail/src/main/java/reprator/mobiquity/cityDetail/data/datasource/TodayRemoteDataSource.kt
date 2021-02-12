package reprator.mobiquity.cityDetail.data.datasource

import kotlinx.coroutines.flow.Flow
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.cityDetail.modals.LocationModal
import reprator.mobiquity.cityDetail.modals.LocationRequestModal

interface TodayRemoteDataSource {
    suspend fun getTodayWeather(requestModal: LocationRequestModal): Flow<MobiQuityResult<LocationModal>>
}