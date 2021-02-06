package reprator.mobiquity.addcity.service

import android.location.Location

interface LocationListener {
    fun locationUpdate(location: Location)
}