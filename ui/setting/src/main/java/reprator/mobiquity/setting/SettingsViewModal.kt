package reprator.mobiquity.setting

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import reprator.mobiquity.base.SaveSettingPreferenceManager
import reprator.mobiquity.base.extensions.computationalBlock
import reprator.mobiquity.base.util.AppCoroutineDispatchers

class SettingsViewModal @ViewModelInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val settingPreferenceManager: SaveSettingPreferenceManager,
) : ViewModel() {


    val isSuccess = MutableLiveData<String>()

    val measureMentType = MutableLiveData(settingPreferenceManager.saveMeasureMentUnitType)
    val isChecked = MutableLiveData(settingPreferenceManager.saveClearSavedLocation)

    fun saveConfiguration() {
        settingPreferenceManager.saveMeasureMentUnitType = measureMentType.value!!
        settingPreferenceManager.saveClearSavedLocation = isChecked.value!!

        isSuccess.value = ""
    }

}