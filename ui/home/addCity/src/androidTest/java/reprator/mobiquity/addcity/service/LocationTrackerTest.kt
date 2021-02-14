package reprator.mobiquity.addcity.service

import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ServiceTestRule
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeoutException

@MediumTest
@RunWith(AndroidJUnit4::class)
class LocationTrackerTest {

    @Rule
    @JvmField
    val serviceRule = ServiceTestRule()

    private lateinit var locationBinderService: LocationTracker

    @Before
    @Throws(TimeoutException::class)
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        val binder = serviceRule.bindService(
            Intent(
                getApplicationContext(),
                LocationTracker::class.java
            )
        )
        locationBinderService = (binder as LocationTracker.LocationBinder).service
    }

    @After
    fun tearDown() {
        locationBinderService.stopService(
            Intent(
                getApplicationContext(),
                LocationTracker::class.java
            )
        )
        serviceRule.unbindService()
    }

    @Test
    fun testWithStartedService() {
        val location: Location = mockk()
        every { location.latitude } returns 34.78
        every { location.longitude } returns 74.80

        val latch = CountDownLatch(1)

        locationBinderService.setLocationListener(object : LocationListener {
            override fun locationUpdate(newLocation: Location) {
                Truth.assertThat(location).isEqualTo(newLocation)
                latch.countDown()
            }
        })

        locationBinderService.onNewLocation(location)
        latch.await()
    }
}