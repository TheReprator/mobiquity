package reprator.mobiquity.saveCity.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import reprator.mobiquity.base.SaveSettingPreferenceManager
import reprator.mobiquity.base.SettingPreferenceManager
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.database.DBManager
import reprator.mobiquity.saveCity.data.repository.mapper.LocationMapper
import reprator.mobiquity.saveCity.domain.repository.GetLocationRepository
import reprator.mobiquity.saveCity.modal.LocationModal
import timber.log.Timber
import javax.inject.Inject

class GetLocationRepositoryImpl @Inject constructor(
    private val locationMapper: LocationMapper,
    private val dbManager: DBManager,
    private val coroutineScope: CoroutineScope,
    private val settingPreferenceManager: SettingPreferenceManager,
    private val saveSettingPreferenceManager: SaveSettingPreferenceManager,
) : GetLocationRepository {

    override suspend fun getLocationList(): Flow<MobiQuityResult<List<LocationModal>>> {
        if (settingPreferenceManager.shouldClearSavedLocation)
            clearTable()

        return when (val data = dbManager.getLocationList().single()) {
            is Success -> {
                flowOf(Success(locationMapper.map(data.data)))
            }

            is ErrorResult -> {
                flowOf(ErrorResult(throwable = data.throwable, message = data.message))
            }

            else -> throw IllegalArgumentException()
        }
    }

    private suspend fun clearTable() = suspendCancellableCoroutine<Unit> { cont ->
        if (settingPreferenceManager.shouldClearSavedLocation)
            coroutineScope.launch {
                dbManager.clearTable().catch { error ->
                    Timber.e("delete exception is ${error.printStackTrace()}")
                    cont.resume(Unit) {}
                }.collect {
                    saveSettingPreferenceManager.saveClearSavedLocation = false
                    cont.resume(Unit) {}
                }
            }
    }
}

