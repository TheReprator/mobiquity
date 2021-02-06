package reprator.mobiquity.addcity

import android.content.Context
import android.location.LocationManager
import android.provider.Settings
import reprator.mobiquity.base_android.util.isAndroidPOrLater
import javax.inject.Inject

interface IsLocationEnabled {
    fun isLocationEnabled(): Boolean
}

class IsLocationEnabledImpl @Inject constructor(private val context: Context) : IsLocationEnabled {
    override fun isLocationEnabled(): Boolean {

        return if (isAndroidPOrLater) {
            // This is a new method provided in API 28
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else {
            // This was deprecated in API 28
            val mode = Settings.Secure.getInt(
                context.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )

            mode != Settings.Secure.LOCATION_MODE_OFF
        }

    }
}