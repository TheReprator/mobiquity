package reprator.mobiquity.implementation

import android.content.SharedPreferences
import reprator.mobiquity.base.MeasureMentUnitType
import reprator.mobiquity.base.SaveSettingPreferenceManager
import reprator.mobiquity.base.SettingPreferenceManager
import javax.inject.Singleton

@Singleton
class SettingPreferenceManagerImpl(
    private val sharedPreferences: SharedPreferences
) : SettingPreferenceManager, SaveSettingPreferenceManager {

    private val mEditor: SharedPreferences.Editor = sharedPreferences.edit()

    override var saveClearSavedLocation: Boolean
        get() {
            return sharedPreferences.getBoolean(PREF_CLEAR, false)
        }
        set(isClear) {
            mEditor.putBoolean(PREF_CLEAR, isClear)
            mEditor.commit()
        }

    override var saveMeasureMentUnitType: MeasureMentUnitType
        get() {
            return MeasureMentUnitType.valueOf(
                sharedPreferences.getString(
                    PREF_UNIT_MEASUREMENT,
                    MeasureMentUnitType.STANDARD.name
                )!!
            )
        }
        set(uniType) {
            mEditor.putString(PREF_UNIT_MEASUREMENT, uniType.name)
            mEditor.commit()
        }

    override val shouldClearSavedLocation = saveClearSavedLocation
    override val measureMentUnitType = saveMeasureMentUnitType

    companion object {
        private const val PREF_CLEAR = "pref_clear"
        private const val PREF_UNIT_MEASUREMENT = "pref_unit"
    }
}

//val value = MyEnum.values().firstOrNull {it.name == "Foo"} // results to MyEnum.Foo
