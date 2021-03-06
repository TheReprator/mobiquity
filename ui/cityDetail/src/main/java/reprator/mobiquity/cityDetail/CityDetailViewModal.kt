package reprator.mobiquity.cityDetail

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import reprator.mobiquity.base.SettingPreferenceManager
import reprator.mobiquity.base.extensions.computationalBlock
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.base.util.AppCoroutineDispatchers
import reprator.mobiquity.cityDetail.domain.usecase.ForecastWeatherUseCase
import reprator.mobiquity.cityDetail.modals.LocationModal
import reprator.mobiquity.cityDetail.modals.LocationRequestModal
import javax.inject.Inject

@HiltViewModel
class CityDetailViewModal @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val coroutineDispatcherProvider: AppCoroutineDispatchers,
    private val forecastWeatherUseCase: ForecastWeatherUseCase,
    private val settingPreferenceManager: SettingPreferenceManager
) : ViewModel() {

    private val _isLoadingForeCast = MutableLiveData(true)
    val isLoadingForeCast: LiveData<Boolean> = _isLoadingForeCast

    private val _errorMsgForeCast = MutableLiveData("")
    val errorMsgForeCast: LiveData<String> = _errorMsgForeCast

    private val _todayWeatherItem = MutableLiveData<LocationModal?>()
    val todayWeatherItem: LiveData<LocationModal?> = _todayWeatherItem

    @VisibleForTesting
    val _foreCastWeatherList = MutableLiveData(emptyList<LocationModal>())

    fun getForeCastWeatherUse() {
        computationalBlock {
            forecastWeatherUseCase(createRequestModal()).flowOn(coroutineDispatcherProvider.io)
                .catch { e ->
                    _errorMsgForeCast.value = e.localizedMessage
                }.onStart {
                    _isLoadingForeCast.value = true
                }.onCompletion {
                    _isLoadingForeCast.value = false
                }.flowOn(coroutineDispatcherProvider.main)
                .collect {
                    withContext(coroutineDispatcherProvider.main) {
                        when (it) {
                            is Success -> {
                                if (it.data.isEmpty()) {
                                    _foreCastWeatherList.value = it.data
                                    return@withContext
                                }
                                _todayWeatherItem.value = it.data[0]
                                _foreCastWeatherList.value = it.data.drop(1)
                            }
                            is ErrorResult -> {
                                _errorMsgForeCast.value = it.message ?: it.throwable!!.message
                            }
                            else -> throw IllegalArgumentException()
                        }
                    }
                }
        }
    }

    fun retryForeCastWeather() {
        _isLoadingForeCast.value = true
        _errorMsgForeCast.value = ""
        getForeCastWeatherUse()
    }

    private fun createRequestModal(): LocationRequestModal {
        val latLng = savedStateHandle.get<String>("latLng")!!.split(",")
        return LocationRequestModal(
            latLng[0],
            latLng[1],
            settingPreferenceManager.measureMentUnitType.name
        )
    }

    private fun computationalBlock(
        coroutineExceptionHandler: CoroutineExceptionHandler? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.computationalBlock(
            coroutineDispatcherProvider,
            coroutineExceptionHandler,
            block
        )
    }
}