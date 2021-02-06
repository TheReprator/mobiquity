package reprator.mobiquity.addcity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import reprator.mobiquity.addcity.databinding.FragmentAddcityBinding
import reprator.mobiquity.addcity.service.LocationListener
import reprator.mobiquity.addcity.service.LocationTracker
import reprator.mobiquity.base_android.PermissionHelper
import reprator.mobiquity.base_android.extensions.shortToast
import reprator.mobiquity.base_android.extensions.twoButtonDialog
import reprator.mobiquity.base_android.util.event.EventObserver
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AddCityFragment : Fragment(R.layout.fragment_addcity), OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener, ServiceConnection,
    LocationListener {

    companion object {
        private const val ZOOM_LEVEL = 12.0f
        private const val PERMISSION_LOCATION = ACCESS_FINE_LOCATION
    }

    private val askLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                Timber.e("Location permnission granted")
                initializeService()
                checkWhetherLocationIsEnabled()
            } else {
                Timber.e("Location permnission denied")
            }
        }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    @Inject
    lateinit var permissionHelper: PermissionHelper

    @Inject
    lateinit var isLocationEnabled: IsLocationEnabled

    private var locationBinder: LocationTracker? = null
    private var locationBound = false

    private var _binding: FragmentAddcityBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap

    private val viewModel: AddLocationViewModal by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAddcityBinding.bind(view)

        val mapFragment: SupportMapFragment? =
            childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        if (!permissionHelper.hasPermissions(requireContext(), PERMISSION_LOCATION))
            askLocationPermission.launch(ACCESS_FINE_LOCATION)
        else {
            initializeService()
            checkWhetherLocationIsEnabled()
        }

        initializeObserver()

        binding.mapSubmit.setOnClickListener {
            toggleSaveButton(false)
            viewModel.decodeLocation(map.cameraPosition.target)
        }
    }

    private fun toggleSaveButton(isShow: Boolean) {
        binding.mapSubmit.visibility = if (isShow)
            View.VISIBLE
        else
            View.GONE
    }

    private fun initializeObserver() {
        viewModel.isError.observe(viewLifecycleOwner, EventObserver {
            requireContext().shortToast(it)
            toggleSaveButton(false)
        })

        viewModel.isSuccess.observe(viewLifecycleOwner, EventObserver {
            toggleSaveButton(false)
        })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap ?: return

        with(googleMap) {
            map = this
            map.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL))
            map.setOnCameraMoveListener(this@AddCityFragment)
            map.setOnCameraIdleListener(this@AddCityFragment)
        }
    }

    override fun onCameraIdle() {
        toggleSaveButton(true)
    }

    override fun onCameraMove() {
        toggleSaveButton(false)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder: LocationTracker.LocalBinder = service as LocationTracker.LocalBinder
        locationBinder = binder.service
        locationBinder!!.setLocationListener(this)

        locationBound = true
        locationBinder?.requestLocationUpdates()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        locationBinder = null
        locationBound = false
    }

    override fun locationUpdate(location: Location) {
        if (!::map.isInitialized)
            return
        unBindServer()

        val latLng = LatLng(location.latitude, location.longitude)
        val cameraPosition = CameraPosition.Builder()
            .target(latLng) // Sets the center of the map to Mountain View
            .zoom(ZOOM_LEVEL) // Sets the zoom
            .build() // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun checkWhetherLocationIsEnabled() {
        if (!isLocationEnabled.isLocationEnabled())
            requireContext().twoButtonDialog(
                getString(R.string.alert_location),
                positiveButtonCallBack = {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                })
    }

    override fun onStop() {
        unBindServer()
        super.onStop()
    }

    private fun unBindServer() {
        if (locationBound) {
            locationBinder?.setLocationListener(null)
            requireActivity().unbindService(this)
            locationBound = false
        }
    }

    private fun initializeService() {
        requireActivity().bindService(
            Intent(requireActivity(), LocationTracker::class.java),
            this, Context.BIND_AUTO_CREATE
        )
    }
}