package reprator.mobiquity.saveCity.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
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
import reprator.mobiquity.androidTest.util.getOrAwaitValue
import reprator.mobiquity.androidTest.util.observeForTesting
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.saveCity.TestFakeData.getLocationModalList
import reprator.mobiquity.saveCity.domain.usecase.DeleteLocationUseCase
import reprator.mobiquity.saveCity.domain.usecase.GetLocationUseCase
import reprator.mobiquity.saveCity.domain.usecase.SearchItemUseCase
import reprator.mobiquity.saveCity.modal.LocationModal
import reprator.mobiquity.testUtils.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SaveCityViewModalTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutinesTestRule = MainCoroutineRule()

    @MockK
    lateinit var savedStateHandle: SavedStateHandle

    @MockK
    lateinit var getLocationUseCase: GetLocationUseCase

    @MockK
    lateinit var deleteLocationUseCase: DeleteLocationUseCase

    @MockK
    lateinit var searchItemUseCase: SearchItemUseCase

    lateinit var saveCityViewModal: SaveCityViewModal

    //create mockk object
    val observerLoad = mockk<Observer<Boolean>>()
    val observerError = mockk<Observer<String>>()
    val observerSuccessList = mockk<Observer<List<LocationModal>>>()

    //create slot
    val slotLoad = slot<Boolean>()
    val slotError = slot<String>()
    val slotSuccess = slot<List<LocationModal>>()

    //create list to store values
    val listError = arrayListOf<String>()
    val listSuccess = arrayListOf<List<LocationModal>>()
    val listLoader = arrayListOf<Boolean>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        saveCityViewModal =
            SaveCityViewModal(
                savedStateHandle, coroutinesTestRule.testDispatcherProvider,
                getLocationUseCase, deleteLocationUseCase, searchItemUseCase
            )

        saveCityViewModal._isLoading.observeForever(observerLoad)
        saveCityViewModal._isError.observeForever(observerError)
        saveCityViewModal.bookMarkListManipulated.observeForever(observerSuccessList)

        every {
            observerLoad.onChanged(capture(slotLoad))
        } answers {
            listLoader.add(slotLoad.captured)
        }

        every {
            observerError.onChanged(capture(slotError))
        } answers {
            listError.add(slotError.captured)
        }

        every {
            observerSuccessList.onChanged(capture(slotSuccess))
        } answers {
            listSuccess.add(slotSuccess.captured)
        }

        coEvery {
            savedStateHandle.get<String>(any())
        } returns "London"
    }

    @Test
    fun `get savedLocationList`() = coroutinesTestRule.runBlockingTest {

        val output = getLocationModalList()

        coEvery {
            getLocationUseCase()
        } returns flowOf(Success(output))

        coEvery {
            searchItemUseCase(any(), any())
        } returns Success(output)

        saveCityViewModal.getSavedLocationList()

        verifySequence {
            observerLoad.onChanged(any())
            observerSuccessList.onChanged(any())
            observerLoad.onChanged(any())
        }

        Truth.assertThat(listSuccess).isNotEmpty()
        Truth.assertThat(listSuccess).hasSize(output.size)
        Truth.assertThat(listSuccess[0]).isEqualTo(output)

        Truth.assertThat(listLoader).isNotEmpty()
        Truth.assertThat(listLoader).hasSize(2)
        Truth.assertThat(listLoader).isEqualTo(listOf(true, false))

        Truth.assertThat(listError).isEmpty()
    }

    @Test
    fun `get savedLocationList fetch failed`() = coroutinesTestRule.runBlockingTest {

        val output = ErrorResult(message = "errorOccurred")

        coEvery {
            getLocationUseCase()
        } returns flowOf(output)

        coEvery {
            searchItemUseCase(any(), any())
        } returns Success(emptyList())

        saveCityViewModal.getSavedLocationList()

        verifySequence {
            observerLoad.onChanged(any())
            observerError.onChanged(any())
            observerLoad.onChanged(any())
        }

        Truth.assertThat(listLoader).isNotEmpty()
        Truth.assertThat(listLoader).hasSize(2)
        Truth.assertThat(listLoader).isEqualTo(listOf(true, false))

        Truth.assertThat(listError).isNotEmpty()
        Truth.assertThat(listError).hasSize(1)
        Truth.assertThat(listError[0]).isEqualTo(output.message)

        Truth.assertThat(listSuccess).isEmpty()
    }

    @Test
    fun `search item from given savedLocationList`() = coroutinesTestRule.runBlockingTest {

        val output = getLocationModalList()
        val searchString = "UK"

        coEvery {
            searchItemUseCase(any(), any())
        } returns Success(output)

        saveCityViewModal.searchServer(searchString)

        verifySequence {
            observerSuccessList.onChanged(any())
        }

        Truth.assertThat(listSuccess).isNotEmpty()
        Truth.assertThat(listSuccess).hasSize(output.size)
        Truth.assertThat(listSuccess[0]).isEqualTo(output)


        Truth.assertThat(listError).isEmpty()
        Truth.assertThat(listLoader).isEmpty()
    }
}