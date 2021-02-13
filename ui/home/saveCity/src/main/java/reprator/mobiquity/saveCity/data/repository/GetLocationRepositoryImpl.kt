package reprator.mobiquity.saveCity.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlin.coroutines.resumeWithException

class GetLocationRepositoryImpl @Inject constructor(
    private val locationMapper: LocationMapper,
    private val dbManager: DBManager,
    private val coroutineScope: CoroutineScope,
    private val settingPreferenceManager: SettingPreferenceManager,
    private val saveSettingPreferenceManager: SaveSettingPreferenceManager,
) : GetLocationRepository {

    override suspend fun getLocationList(): Flow<MobiQuityResult<List<LocationModal>>> {
        return if (settingPreferenceManager.shouldClearSavedLocation) {
            clearTable()
            saveSettingPreferenceManager.saveClearSavedLocation = false
            flowOf(Success(emptyList<LocationModal>()))
        } else
            when (val data = dbManager.getLocationList().single()) {
                is Success -> {
                    flowOf(Success(locationMapper.map(data.data)))
                }

                is ErrorResult -> {
                    flowOf(ErrorResult(throwable = data.throwable, message = data.message))
                }

                else -> throw IllegalArgumentException()
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun clearTable() = suspendCancellableCoroutine<Unit> { cont ->
        coroutineScope.launch {
            dbManager.clearTable().catch { error ->
                cont.resumeWithException(error)
            }.collect {
                cont.resume(Unit) {}
            }
        }
    }
}
