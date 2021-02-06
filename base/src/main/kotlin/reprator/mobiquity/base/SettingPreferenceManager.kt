package reprator.mobiquity.base

interface SaveSettingPreferenceManager {
    var saveClearSavedLocation: Boolean
    var saveMeasureMentUnitType: MeasureMentUnitType
}


interface SettingPreferenceManager {
    val shouldClearSavedLocation: Boolean
    val measureMentUnitType: MeasureMentUnitType
}

enum class MeasureMentUnitType {
    STANDARD,
    METRIC,
    IMPERIAL
}