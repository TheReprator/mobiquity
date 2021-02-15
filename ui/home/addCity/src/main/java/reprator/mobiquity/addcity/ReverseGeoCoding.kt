package reprator.mobiquity.addcity

import android.location.Geocoder
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.scopes.ViewModelScoped
import timber.log.Timber
import javax.inject.Inject

@ViewModelScoped
class ReverseGeoCoding @Inject constructor(private val geocode: Geocoder) {

    fun getLocations(latLng: LatLng): Location {

        var fullAddress : String

        try {
            val addressList = geocode.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (addressList.isNullOrEmpty())
                throw Exception("address is empty")

            val firstElement = addressList[0]
            val addressLine = if (0 < firstElement.maxAddressLineIndex)
                firstElement.getAddressLine(0)
            else
                ""
            fullAddress = "$addressLine, ${firstElement.locality}, ${firstElement.countryName}"
        } catch (e: Exception) {
            Timber.e("${e.printStackTrace()}")
            fullAddress = "${latLng.latitude}, ${latLng.longitude}"
        }

        val locationDetail = Location(fullAddress)
        locationDetail.latitude = latLng.latitude
        locationDetail.longitude = latLng.longitude

        return locationDetail
    }
}