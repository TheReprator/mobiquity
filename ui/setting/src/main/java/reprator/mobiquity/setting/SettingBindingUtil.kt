package reprator.mobiquity.setting

import androidx.databinding.InverseMethod
import reprator.mobiquity.base.MeasureMentUnitType

@InverseMethod("buttonIdToMeasurementType")
fun measurementTypeToButtonId(measureMentUnitType: MeasureMentUnitType?): Int {
    var selectedButtonId = -1

    measureMentUnitType?.run {
        selectedButtonId = when (this) {
            MeasureMentUnitType.STANDARD -> R.id.setting_measureUnit_standard
            MeasureMentUnitType.METRIC -> R.id.setting_measureUnit_metric
            MeasureMentUnitType.IMPERIAL -> R.id.setting_measureUnit_imperial
        }
    }

    return selectedButtonId
}

fun buttonIdToMeasurementType(selectedButtonId: Int): MeasureMentUnitType? {
    return when (selectedButtonId) {
        R.id.setting_measureUnit_standard -> MeasureMentUnitType.STANDARD
        R.id.setting_measureUnit_metric -> MeasureMentUnitType.METRIC
        R.id.setting_measureUnit_imperial -> MeasureMentUnitType.IMPERIAL
        else -> null
    }
}


