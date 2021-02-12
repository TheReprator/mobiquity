package reprator.mobiquity.addcity

import android.location.Location
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import reprator.mobiquity.addcity.domain.usecase.LocationUseCase
import reprator.mobiquity.base.extensions.computationalBlock
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.base.util.AppCoroutineDispatchers
import reprator.mobiquity.base_android.util.event.Event

class AddLocationViewModal @ViewModelInject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val reverseGeoCoding: ReverseGeoCoding,
    private val locationUseCase: LocationUseCase
) : ViewModel() {

    private val _isError: MutableLiveData<Event<String>> = MutableLiveData()
    val isError: LiveData<Event<String>>
        get() = _isError

    private val _isSuccess: MutableLiveData<Event<Unit>> = MutableLiveData()
    val isSuccess: LiveData<Event<Unit>>
        get() = _isSuccess

    fun decodeLocation(latLng: LatLng) {
        computationalBlock {
            val output = reverseGeoCoding.getLocations(latLng)
            saveLocation(output)
        }
    }

    private suspend fun saveLocation(location: Location) {
        locationUseCase(location).catch {
            _isError.value = Event(it.localizedMessage!!)
        }.flowOn(appCoroutineDispatchers.main).collect {
            withContext(appCoroutineDispatchers.main) {
                when (it) {
                    is Success -> {
                        _isSuccess.value = Event(Unit)
                    }
                    is ErrorResult -> {
                        _isError.value = Event(it.message ?: "")
                    }
                    else -> throw IllegalArgumentException()
                }
            }
        }
    }

    private fun computationalBlock(
        coroutineExceptionHandler: CoroutineExceptionHandler? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.computationalBlock(
            appCoroutineDispatchers,
            coroutineExceptionHandler,
            block
        )
    }
}