package reprator.mobiquity.addcity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import reprator.mobiquity.addcity.TestFakeData.getLocationModal
import reprator.mobiquity.addcity.domain.usecase.LocationUseCase
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.base_android.util.event.Event
import reprator.mobiquity.testUtils.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AddLocationViewModalTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutinesTestRule = MainCoroutineRule()

    @MockK
    lateinit var reverseGeoCoding: ReverseGeoCoding

    @MockK
    lateinit var locationUseCase: LocationUseCase

    lateinit var addLocationViewModal: AddLocationViewModal

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        addLocationViewModal = AddLocationViewModal(
            coroutinesTestRule.testDispatcherProvider,
            reverseGeoCoding, locationUseCase
        )
    }

    @Test
    fun `decode Location & save it in db repository`() = coroutinesTestRule.runBlockingTest {

        //create mockk object
        val observerError = mockk<Observer<Event<String>>>()
        val observerSuccess = mockk<Observer<Event<Unit>>>()

        //create slot
        val slotError = slot<Event<String>>()
        val slotSuccess = slot<Event<Unit>>()

        //create list to store values
        val listError = arrayListOf<Event<String>>()
        val listSuccess = arrayListOf<Event<Unit>>()

        //start observing
        addLocationViewModal.isSuccess.observeForever(observerSuccess)
        addLocationViewModal.isError.observeForever(observerError)

        every {
            observerSuccess.onChanged(capture(slotSuccess))
        } answers {
            listSuccess.add(slotSuccess.captured)
        }
        every {
            observerError.onChanged(capture(slotError))
        } answers {
            listError.add(slotError.captured)
        }

        val output = Success(1L)
        val geoCodeOutput = getLocationModal()

        coEvery {
            reverseGeoCoding.getLocations(any())
        } returns geoCodeOutput

        coEvery {
            locationUseCase(any())
        } returns flowOf(output)

        val input = LatLng(20.21, 76.35)
        addLocationViewModal.decodeLocation(input)

        verify(atMost = 1) {
            observerSuccess.onChanged(any())
        }

        verify(atMost = 1) {
            observerSuccess.onChanged(any())
        }
        verifySequence {
            observerSuccess.onChanged(Event(Unit))
        }

        Truth.assertThat(listSuccess).isNotEmpty()
        Truth.assertThat(listSuccess).hasSize(1)
    }

    @Test
    fun `failed to save in db repository after decode Location`() = coroutinesTestRule.runBlockingTest {

        //create mockk object
        val observerError = mockk<Observer<Event<String>>>()

        //create slot
        val slotError = slot<Event<String>>()

        //create list to store values
        val listError = arrayListOf<Event<String>>()

        //start observing
        addLocationViewModal.isError.observeForever(observerError)

        every {
            observerError.onChanged(capture(slotError))
        } answers {
            listError.add(slotError.captured)
        }

        val geoCodeOutput = getLocationModal()
        val errorResult = ErrorResult(message = "failed to save")

        coEvery {
            reverseGeoCoding.getLocations(any())
        } returns geoCodeOutput

        coEvery {
            locationUseCase(any())
        } returns flowOf(errorResult)

        val input = LatLng(20.21, 76.35)
        addLocationViewModal.decodeLocation(input)

        verify(atMost = 1) {
            observerError.onChanged(any())
        }

        verify(atMost = 1) {
            observerError.onChanged(any())
        }
        verifySequence {
            observerError.onChanged(any())
        }

        Truth.assertThat(listError).isNotEmpty()
        Truth.assertThat(listError).hasSize(1)
        Truth.assertThat(listError[0].peek()).isEqualTo(errorResult.message)
    }
}