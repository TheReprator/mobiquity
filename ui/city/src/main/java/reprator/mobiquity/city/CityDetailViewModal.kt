package reprator.mobiquity.city

import androidx.annotation.VisibleForTesting
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import reprator.mobiquity.base.SettingPreferenceManager
import reprator.mobiquity.base.extensions.computationalBlock
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.base.util.AppCoroutineDispatchers
import reprator.mobiquity.city.domain.usecase.ForecastWeatherUseCase
import reprator.mobiquity.city.domain.usecase.GetTodayWeatherUseCase
import reprator.mobiquity.city.modals.LocationModal
import reprator.mobiquity.city.modals.LocationRequestModal

class CityDetailViewModal @ViewModelInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val coroutineDispatcherProvider: AppCoroutineDispatchers,
    private val forecastWeatherUseCase: ForecastWeatherUseCase,
    private val getTodayWeatherUseCase: GetTodayWeatherUseCase,
    private val settingPreferenceManager: SettingPreferenceManager
) : ViewModel() {

    private val _isLoadingForeCast = MutableLiveData(true)
    val isLoadingForeCast: LiveData<Boolean> = _isLoadingForeCast

    private val _errorMsgForeCast = MutableLiveData("")
    val errorMsgForeCast: LiveData<String> = _errorMsgForeCast

    private val _isLoadingTodayWeather = MutableLiveData(true)
    val isLoadingTodayWeather: LiveData<Boolean> = _isLoadingTodayWeather

    private val _errorMsgToday = MutableLiveData("")
    val errorMsgToday: LiveData<String> = _errorMsgToday

    private val _todayWeatherItem = MutableLiveData<LocationModal?>()
    val todayWeatherItem: LiveData<LocationModal?> = _todayWeatherItem

    @VisibleForTesting
    val _foreCastWeatherList = MutableLiveData(emptyList<LocationModal>())

    init {
        getTodayWeatherUseCase()
        getForeCastWeatherUse()
    }

    fun getTodayWeatherUseCase() {
        computationalBlock {
            getTodayWeatherUseCase(createRequestModal()).flowOn(coroutineDispatcherProvider.io).catch { e ->
                _errorMsgToday.value = e.localizedMessage
            }.onStart {
                _isLoadingTodayWeather.value = true
            }.onCompletion {
                _isLoadingTodayWeather.value = false
            }.flowOn(coroutineDispatcherProvider.main)
                .collect {
                    withContext(coroutineDispatcherProvider.main) {
                        when (it) {
                            is Success -> {
                                _todayWeatherItem.value = it.get()
                            }
                            is ErrorResult -> {
                                _errorMsgToday.value = it.message!!
                            }
                        }
                    }
                }
        }
    }

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
                                _foreCastWeatherList.value = it.data
                            }
                            is ErrorResult -> {
                                _errorMsgForeCast.value = it.message!!
                            }
                        }
                    }
                }
        }
    }

    fun retryTodayWeather() {
        _errorMsgToday.value = ""
        getTodayWeatherUseCase()
    }

    fun retryForeCastWeather() {
        _errorMsgForeCast.value = ""
        getForeCastWeatherUse()
    }

    private fun createRequestModal(): LocationRequestModal {
        val latLng = savedStateHandle.get<String>("latLng")!!.split(",")
        return LocationRequestModal(latLng[0], latLng[1], settingPreferenceManager.measureMentUnitType.name)
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