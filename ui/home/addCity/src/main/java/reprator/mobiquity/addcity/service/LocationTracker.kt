package reprator.mobiquity.addcity.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.location.Location
import android.media.RingtoneManager
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import reprator.mobiquity.addcity.R
import reprator.mobiquity.base.util.isNull
import reprator.mobiquity.base_android.util.isAndroidOOrLater
import timber.log.Timber

class LocationTracker : Service() {

    companion object {
        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 2000

        /**
         * The fastest rate for active location updates. Updates will never be more frequent
         * than this value.
         */
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2

        private const val EXTRA_STARTED_FROM_NOTIFICATION = "started_from_notification"

        /**
         * The identifier for the notification displayed for the foreground service.
         */
        private const val NOTIFICATION_ID = 12345678

        private const val CHANNEL_NAME = "Mobiquity Notifications"

        private const val NOTIFICATION_CHANNEL_ID = "mobiQuityChannelId_0"

    }

    private var locationListener: LocationListener? = null

    /**
     * Provides access to the Fused Location Provider API.
     */
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    /**
     * Callback for changes in location.
     */
    private lateinit var mLocationCallback: LocationCallback

    /**
     * Contains parameters used by [com.google.android.gms.location.FusedLocationProviderApi].
     */
    private lateinit var mLocationRequest: LocationRequest

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private var mChangingConfiguration = false

    private val mBinder = LocalBinder()

    override fun onCreate() {
        super.onCreate()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                locationResult?.let { mLocationResult ->
                    onNewLocation(mLocationResult.lastLocation)
                }
            }
        }

        createLocationRequest()
        getLastLocation()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.e("Location Service started")
        val startedFromNotification =
            intent?.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false)

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification!!) {
            removeLocationUpdates()
            stopSelf()
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mChangingConfiguration = true
    }

    override fun onBind(intent: Intent?): IBinder? {
        Timber.e("in onBind()")
        stopForeground(true)
        mChangingConfiguration = false
        return mBinder
    }

    override fun onRebind(intent: Intent?) {
        Timber.i("in onRebind()")
        stopForeground(true)
        mChangingConfiguration = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Timber.i("Last client unbound from service")

        onDestroy()
        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration) {
            Timber.i("Starting foreground service")

            //startForeground(NOTIFICATION_ID, getNotification())
        }
        return true
    }

    fun onNewLocation(location: Location) {
        if (locationListener.isNull())
            return

        Timber.e("New location: $location")

        locationListener?.locationUpdate(location)
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.fastestInterval =
            FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        Timber.e("createLocationRequest")
    }

    private fun getLastLocation() {

        if (!isFusedLocationInitialized())
            return

        try {
            mFusedLocationClient.lastLocation
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        onNewLocation(task.result)
                    } else {
                        Timber.e("Failed to get location.")
                    }
                }
        } catch (unlikely: SecurityException) {
            Timber.e("Lost location permission.$unlikely")
        }
    }

    fun requestLocationUpdates() {
        Timber.e("Requesting location updates")
      //  startService(Intent(applicationContext, LocationTracker::class.java))
        try {
            if (!isFusedLocationInitialized())
                return

            if (!::mLocationRequest.isInitialized)
                return

            if (!::mLocationCallback.isInitialized)
                return

            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            Timber.e("Lost location permission. Could not request updates. $unlikely")
        }

    }
    private fun removeLocationUpdates() {
        Timber.e("Removing location updates")
        try {
            if (!isFusedLocationInitialized())
                return

            if (!::mLocationCallback.isInitialized)
                return

            mFusedLocationClient.removeLocationUpdates(mLocationCallback)
            stopSelf()
        } catch (unlikely: SecurityException) {
            Timber.e("Lost location permission. Could not remove updates. $unlikely")
        }
    }

    private fun isFusedLocationInitialized() = ::mFusedLocationClient.isInitialized

    inner class LocalBinder : Binder() {
        internal val service: LocationTracker
            get() = this@LocationTracker
    }

    private fun getNotification(): Notification {
        val intent = Intent(this, LocationTracker::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_ONE_SHOT)

        val mSeconds: Int =  resources.getInteger(R.integer.integer_three_seconds)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (isAndroidOOrLater) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            // Configure the notification channel.
            notificationChannel.description = "Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(
            applicationContext, NOTIFICATION_CHANNEL_ID
        )

        notificationBuilder.setAutoCancel(true)
            .setLights(ContextCompat.getColor(this, R.color.color_3b80), mSeconds, mSeconds)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_pin)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setTicker(resources.getString(R.string.app_name_notification))
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentIntent(pendingIntent)
            .setContentTitle("Mobiquity Notification")
            .setContentText("Location update notification")
            .setContentInfo("MobiquityInfo")

        return notificationBuilder.build()
    }

    fun setLocationListener(locationListener: LocationListener?){
        this.locationListener = locationListener
    }
}