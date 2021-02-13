package reprator.mobiquity.saveCity.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import reprator.mobiquity.base.SaveSettingPreferenceManager
import reprator.mobiquity.base.SettingPreferenceManager
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.database.DBManager
import reprator.mobiquity.saveCity.data.repository.mapper.LocationMapper
import reprator.mobiquity.saveCity.domain.repository.GetLocationRepository
import reprator.mobiquity.saveCity.modal.LocationModal
import javax.inject.Inject

class GetLocationRepositoryImpl @Inject constructor(
    private val locationMapper: LocationMapper,
    private val dbManager: DBManager,
    private val settingPreferenceManager: SettingPreferenceManager,
    private val saveSettingPreferenceManager: SaveSettingPreferenceManager,
) : GetLocationRepository {

    override suspend fun getLocationList(): Flow<MobiQuityResult<List<LocationModal>>> {
        return if (settingPreferenceManager.shouldClearSavedLocation) {
            dbManager.clearTable()
            saveSettingPreferenceManager.saveClearSavedLocation = false
            flowOf(Success(emptyList()))
        } else
            when (val data = dbManager.getLocationList()) {
                is Success -> {
                    flowOf(Success(locationMapper.map(data.data)))
                }

                is ErrorResult -> {
                    flowOf(ErrorResult(throwable = data.throwable, message = data.message))
                }

                else -> throw IllegalArgumentException()
            }
    }
}
