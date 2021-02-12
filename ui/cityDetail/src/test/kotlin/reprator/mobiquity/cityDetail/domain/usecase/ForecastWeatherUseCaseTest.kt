package reprator.mobiquity.cityDetail.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.cityDetail.TestFakeData.getFakeLocationModalDataList
import reprator.mobiquity.cityDetail.domain.repository.ForecastWeatherRepository
import reprator.mobiquity.cityDetail.modals.LocationRequestModal
import reprator.mobiquity.testUtils.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ForecastWeatherUseCaseTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutinesTestRule = MainCoroutineRule()

    @MockK
    lateinit var forecastWeatherRepository: ForecastWeatherRepository

    lateinit var forecastWeatherUseCase: ForecastWeatherUseCase

    private val input = LocationRequestModal("24.31", "76.32")

    @Before
    fun setup() {
        MockKAnnotations.init(this, true)

        forecastWeatherUseCase = ForecastWeatherUseCase(forecastWeatherRepository)
    }

    @Test
    fun `fetch forecast data from remote data source`() = coroutinesTestRule.runBlockingTest {
        val output = getFakeLocationModalDataList()

        coEvery {
            forecastWeatherRepository.getForeCastWeatherRepository(any())
        } returns flowOf(Success(output))

        val result = forecastWeatherUseCase(input).single()

        Truth.assertThat(result).isInstanceOf(Success::class.java)
        Truth.assertThat((result as Success).data).isEqualTo(output)
    }
}