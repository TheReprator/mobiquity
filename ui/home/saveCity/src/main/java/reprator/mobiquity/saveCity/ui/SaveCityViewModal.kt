package reprator.mobiquity.saveCity.ui

import androidx.annotation.VisibleForTesting
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
import reprator.mobiquity.saveCity.domain.usecase.SearchItemUseCase
import reprator.mobiquity.saveCity.modal.LocationModal

class SaveCityViewModal @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val locationUseCase: GetLocationUseCase,
    private val deleteLocationUseCase: DeleteLocationUseCase,
    private val searchItemUseCase: SearchItemUseCase
) : ViewModel(), DeleteSwipeItem {

    companion object{
        private const val KEY_SEARCH = "searchQuery"
        private const val DEBOUNCE_TIME = 250L
    }
    private val _searchQuery = MutableStateFlow("")
    val searchQuery:StateFlow<String> =_searchQuery

    @VisibleForTesting
    val _bookMarkListManipulated: MutableLiveData<List<LocationModal>> =
        MutableLiveData()
    val bookMarkListManipulated: LiveData<List<LocationModal>>
        get() = _bookMarkListManipulated

    @VisibleForTesting
    val _bookMarkList: MutableLiveData<List<LocationModal>> = MutableLiveData()

    val _isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val _isError: MutableLiveData<String> = MutableLiveData()

    val _isLoadingDelete: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    init {
        setListenerForMutableState()
    }

    fun getSavedLocationList() {
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
                            _bookMarkListManipulated.value = _bookMarkList.value
                        }
                        is ErrorResult -> {
                            _isError.value = it.message ?: "Error Occurred"
                        }
                        else -> throw  IllegalArgumentException()
                    }
                }
            }
        }
    }

    override fun deletedItem(position: Int) {
        computationalBlock {
            val item = bookMarkListManipulated.value!![position]
            deleteLocationUseCase(item).catch {
                _isError.value = it.localizedMessage
            }.onStart {
                _isLoadingDelete.value = true
            }.onCompletion {
                _isLoadingDelete.value = false
            }.flowOn(appCoroutineDispatchers.main).collect {
                withContext(appCoroutineDispatchers.main) {
                    when (it) {
                        is Success -> {
                            _bookMarkList.value =
                                _bookMarkList.value!!.filterIndexed { index, _ ->
                                    index != position
                                }
                            _bookMarkListManipulated.value = _bookMarkList.value
                        }
                        is ErrorResult -> {
                            _bookMarkListManipulated.value =
                                _bookMarkListManipulated.value!!.toList()
                            _isError.value = it.message ?: "Error Occurrred"
                        }
                        else -> throw IllegalArgumentException()
                    }
                }
            }
        }
    }

    fun retryGetBookMarkLocations() {
        _isError.value = ""
        getSavedLocationList()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        savedStateHandle.set(KEY_SEARCH, query)
    }

    private fun setListenerForMutableState() {
        computationalBlock {
            _searchQuery.debounce(DEBOUNCE_TIME)
                .collectLatest { query ->
                    searchServer(query)
                }
        }
    }

    @VisibleForTesting
    fun searchServer(query: String) {
        computationalBlock {
            val data = searchItemUseCase(_bookMarkList.value, query)
            withContext(appCoroutineDispatchers.main) {
                when (data) {
                    is Success ->
                        _bookMarkListManipulated.value = data.data
                    is ErrorResult ->
                        _bookMarkListManipulated.value = emptyList()
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

interface DeleteSwipeItem {
    fun deletedItem(position: Int)
}