package reprator.mobiquity.cityDetail.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
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
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.base.util.ConnectionDetector
import reprator.mobiquity.cityDetail.TestFakeData.getFakeLocationModalDataList
import reprator.mobiquity.cityDetail.data.datasource.ForecastWeatherRemoteDataSource
import reprator.mobiquity.cityDetail.domain.repository.ForecastWeatherRepository
import reprator.mobiquity.cityDetail.modals.LocationRequestModal
import reprator.mobiquity.testUtils.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ForecastWeatherRepositoryImplTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutinesTestRule = MainCoroutineRule()

    @MockK
    lateinit var forecastWeatherRemoteDataSource: ForecastWeatherRemoteDataSource

    @MockK
    lateinit var connectionDetector: ConnectionDetector

    lateinit var forecastWeatherRepository: ForecastWeatherRepository

    private val input = LocationRequestModal("24.31", "76.32")

    @Before
    fun setup() {
        MockKAnnotations.init(this, true)

        forecastWeatherRepository = ForecastWeatherRepositoryImpl(
            forecastWeatherRemoteDataSource, connectionDetector
        )
    }

    @Test
    fun `get weather data from server, if internet is available`() =
        coroutinesTestRule.runBlockingTest {

            val output = getFakeLocationModalDataList()

            coEvery {
                connectionDetector.isInternetAvailable
            } returns true

            coEvery {
                forecastWeatherRemoteDataSource.getForecastWeather(any())
            } returns Success(output)

            val result = forecastWeatherRepository.getForeCastWeatherRepository(input).single()

            Truth.assertThat(result).isInstanceOf(Success::class.java)
            Truth.assertThat(result.get()!!).hasSize(output.size)

            coVerifySequence {
                connectionDetector.isInternetAvailable
                forecastWeatherRemoteDataSource.getForecastWeather(any())
            }

            coVerify(atMost = 1) {
                connectionDetector.isInternetAvailable
                forecastWeatherRemoteDataSource.getForecastWeather(any())
            }
        }

    @Test
    fun `No internet available`() =
        coroutinesTestRule.runBlockingTest {

            val output = "No internet connection."

            coEvery {
                connectionDetector.isInternetAvailable
            } returns false

            val result = forecastWeatherRepository.getForeCastWeatherRepository(input).single()

            Truth.assertThat(result).isInstanceOf(ErrorResult::class.java)
            Truth.assertThat((result as ErrorResult).message).isEqualTo(output)
        }
}