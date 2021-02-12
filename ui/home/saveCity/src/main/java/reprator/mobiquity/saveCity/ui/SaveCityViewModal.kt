package reprator.mobiquity.saveCity.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import reprator.mobiquity.base.extensions.computationalBlock
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.base.util.AppCoroutineDispatchers
import reprator.mobiquity.saveCity.domain.usecase.DeleteLocationUseCase
import reprator.mobiquity.saveCity.domain.usecase.GetLocationUseCase
import reprator.mobiquity.saveCity.modal.LocationModal

class SaveCityViewModal @ViewModelInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val locationUseCase: GetLocationUseCase,
    private val deleteLocationUseCase: DeleteLocationUseCase
) : ViewModel(), DeleteSwipeItem {

    private val _bookMarkList: MutableLiveData<List<LocationModal>> = MutableLiveData(emptyList())
    val bookMarkList: LiveData<List<LocationModal>>
        get() = _bookMarkList

    val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val _isLoadingProcess: MutableLiveData<Boolean> = MutableLiveData(false)

    val _isError: MutableLiveData<String> = MutableLiveData("")

    fun getSavedLocationList() {

        if (bookMarkList.value!!.isNotEmpty()) {
            _bookMarkList.value = bookMarkList.value
            return
        }

        computationalBlock {
            locationUseCase().catch {
                _isError.value = it.localizedMessage
            }.onStart {
                _isLoading.value = true
            }.onCompletion {
                _isLoading.value = false
            }.flowOn(appCoroutineDispatchers.main).collect {

                withContext(appCoroutineDispatchers.main) {
                    when (it) {
                        is Success -> {
                            _bookMarkList.value = it.data
                        }
                        is ErrorResult -> {
                            _isError.value = it.message ?: "Error Occurrred"
                        }
                    }
                }
            }
        }
    }

    override fun deletedItem(selectedPosition: Int) {
        computationalBlock {
            val item = bookMarkList.value!![selectedPosition]
            deleteLocationUseCase(item).catch {
                _isError.value = it.localizedMessage
            }.onStart {
                _isLoadingProcess.value = true
            }.onCompletion {
                _isLoadingProcess.value = false
            }.flowOn(appCoroutineDispatchers.main).collect {
                withContext(appCoroutineDispatchers.main) {
                    when (it) {
                        is Success -> {
                            val newList = _bookMarkList.value!!.filterIndexed { index, _ ->
                                index != selectedPosition
                            }
                            _bookMarkList.value = newList
                        }
                        is ErrorResult -> {
                            _bookMarkList.value = _bookMarkList.value!!.toList()
                            _isError.value = it.message ?: "Error Occurrred"
                        }
                    }
                }

            }
        }
    }

    fun retryGetBookMarkLocations() {
        _isError.value = ""
        getSavedLocationList()
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

interface DeleteSwipeItem {
    fun deletedItem(position: Int)
}