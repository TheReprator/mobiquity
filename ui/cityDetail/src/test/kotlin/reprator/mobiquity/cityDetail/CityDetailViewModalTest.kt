package reprator.mobiquity.cityDetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
import reprator.mobiquity.base.MeasureMentUnitType
import reprator.mobiquity.base.SettingPreferenceManager
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.cityDetail.TestFakeData.getFakeLocationModalDataList
import reprator.mobiquity.cityDetail.domain.usecase.ForecastWeatherUseCase
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
    }

    @Test
    fun `get forecast for next 2 days`() = coroutinesTestRule.runBlockingTest {

        val output = getFakeLocationModalDataList()

        coEvery {
            forecastWeatherUseCase(any())
        } returns flowOf(Success(output))

        coroutinesTestRule.pauseDispatcher()

        cityListViewModal.getForeCastWeatherUse()

        cityListViewModal._foreCastWeatherList.observeForTesting {
            Truth.assertThat(cityListViewModal.isLoadingForeCast.getOrAwaitValue()).isTrue()
            Truth.assertThat(cityListViewModal.errorMsgForeCast.getOrAwaitValue()).isEmpty()

            coroutinesTestRule.resumeDispatcher()

            Truth.assertThat(cityListViewModal.isLoadingForeCast.getOrAwaitValue()).isFalse()

            Truth.assertThat(cityListViewModal._foreCastWeatherList.getOrAwaitValue()).hasSize(output.size - 1)

            Truth.assertThat(cityListViewModal.todayWeatherItem.getOrAwaitValue())
                .isEqualTo(output[0])
        }
    }

    @Test
    fun `error on fetching forecast for next 2 days`() = coroutinesTestRule.runBlockingTest {

        val output = "An error occurred"

        coEvery {
            forecastWeatherUseCase(any())
        } returns flowOf(ErrorResult(message = output))

        coroutinesTestRule.pauseDispatcher()

        cityListViewModal.getForeCastWeatherUse()

        cityListViewModal._foreCastWeatherList.observeForTesting {
            Truth.assertThat(cityListViewModal.isLoadingForeCast.getOrAwaitValue()).isTrue()
            Truth.assertThat(cityListViewModal.errorMsgForeCast.getOrAwaitValue()).isEmpty()

            coroutinesTestRule.resumeDispatcher()

            Truth.assertThat(cityListViewModal.isLoadingForeCast.getOrAwaitValue()).isFalse()
            Truth.assertThat(cityListViewModal.errorMsgForeCast.getOrAwaitValue()).isEqualTo(output)

            Truth.assertThat(cityListViewModal._foreCastWeatherList.getOrAwaitValue()).isEmpty()
        }
    }

    @Test
    fun `retry, after error occurred on fetching forecast for next 2 days`() =
        coroutinesTestRule.runBlockingTest {

            val output = getFakeLocationModalDataList()

            coEvery {
                forecastWeatherUseCase(any())
            } returns flowOf(Success(output))

            coroutinesTestRule.pauseDispatcher()

            cityListViewModal.retryForeCastWeather()

            cityListViewModal._foreCastWeatherList.observeForTesting {
                Truth.assertThat(cityListViewModal.isLoadingForeCast.getOrAwaitValue()).isTrue()
                Truth.assertThat(cityListViewModal.errorMsgForeCast.getOrAwaitValue()).isEmpty()

                coroutinesTestRule.resumeDispatcher()

                Truth.assertThat(cityListViewModal.isLoadingForeCast.getOrAwaitValue()).isFalse()

                Truth.assertThat(cityListViewModal._foreCastWeatherList.getOrAwaitValue())
                    .hasSize(output.size - 1)
            }
        }
}