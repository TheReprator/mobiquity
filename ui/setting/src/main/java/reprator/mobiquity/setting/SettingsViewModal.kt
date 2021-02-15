package reprator.mobiquity.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import reprator.mobiquity.base.SaveSettingPreferenceManager
import javax.inject.Inject

@HiltViewModel
class SettingsViewModal @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
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