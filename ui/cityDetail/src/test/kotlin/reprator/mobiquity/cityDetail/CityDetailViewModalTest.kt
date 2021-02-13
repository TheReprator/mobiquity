package reprator.mobiquity.cityDetail

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
import reprator.mobiquity.base.MeasureMentUnitType
import reprator.mobiquity.base.SettingPreferenceManager
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.cityDetail.TestFakeData.getFakeLocationModalDataList
import reprator.mobiquity.cityDetail.domain.usecase.ForecastWeatherUseCase
import reprator.mobiquity.cityDetail.modals.LocationModal
import reprator.mobiquity.testUtils.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CityDetailViewModalTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutinesTestRule = MainCoroutineRule()

    @MockK
    lateinit var forecastWeatherUseCase: ForecastWeatherUseCase

    @MockK
    lateinit var savedStateHandle: SavedStateHandle

    @MockK
    lateinit var settingPreferenceManager: SettingPreferenceManager


    lateinit var cityListViewModal: CityDetailViewModal

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

        cityListViewModal =
            CityDetailViewModal(
                savedStateHandle, coroutinesTestRule.testDispatcherProvider,
                forecastWeatherUseCase, settingPreferenceManager
            )

        coEvery {
            savedStateHandle.get<String>(any())
        } returns "20.71,40.43"

        coEvery {
            settingPreferenceManager.measureMentUnitType
        } returns MeasureMentUnitType.STANDARD

        //start observing
        cityListViewModal.isLoadingForeCast.observeForever(observerLoad)
        cityListViewModal.errorMsgForeCast.observeForever(observerError)
        cityListViewModal._foreCastWeatherList.observeForever(observerSuccessList)

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
    }

    @Test
    fun `get forecast for next 2 days`() = coroutinesTestRule.runBlockingTest {

        val output = getFakeLocationModalDataList()

        coEvery {
            forecastWeatherUseCase(any())
        } returns flowOf(Success(output))

        cityListViewModal.getForeCastWeatherUse()

        verifySequence {
            observerLoad.onChanged(any())
            observerSuccessList.onChanged(any())
            observerLoad.onChanged(any())
        }

        Truth.assertThat(listSuccess).isNotEmpty()
        Truth.assertThat(listSuccess).hasSize(output.size - 1)
        Truth.assertThat(listSuccess[0]).isEqualTo(output.drop(1))
    }

    @Test
    fun `error on fetching forecast for next 2 days`() = coroutinesTestRule.runBlockingTest {

        val output = "An error occurred"

        coEvery {
            forecastWeatherUseCase(any())
        } returns flowOf(ErrorResult(message = output))

        cityListViewModal.getForeCastWeatherUse()

        verifySequence {
            observerLoad.onChanged(any())
            observerError.onChanged(any())
            observerLoad.onChanged(any())
        }

        Truth.assertThat(listSuccess).isEmpty()
        Truth.assertThat(listLoader).isNotEmpty()
        Truth.assertThat(listLoader).hasSize(2)
        Truth.assertThat(listError[0]).isEqualTo(output)
    }

    @Test
    fun `retry, after error occurred on fetching forecast for next 2 days`() =
        coroutinesTestRule.runBlockingTest {

            val output = getFakeLocationModalDataList()

            coEvery {
                forecastWeatherUseCase(any())
            } returns flowOf(Success(output))

            cityListViewModal.retryForeCastWeather()

            verifySequence {
                observerError.onChanged(any())
                observerLoad.onChanged(any())
                observerSuccessList.onChanged(any())
                observerLoad.onChanged(any())
            }

            Truth.assertThat(listSuccess).isNotEmpty()
            Truth.assertThat(listSuccess).hasSize(output.size - 1)
            Truth.assertThat(listSuccess[0]).isEqualTo(output.drop(1))

            Truth.assertThat(listLoader).isNotEmpty()
            Truth.assertThat(listLoader).hasSize(2)

            Truth.assertThat(listError).isNotEmpty()
            Truth.assertThat(listError).hasSize(1)
        }
}