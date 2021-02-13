package reprator.mobiquity.cityDetail.datasource.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.cityDetail.TestFakeData.getFakeLocationModalDataList
import reprator.mobiquity.cityDetail.TestFakeData.getFakeRemoteDataList
import reprator.mobiquity.cityDetail.data.datasource.ForecastWeatherRemoteDataSource
import reprator.mobiquity.cityDetail.datasource.remote.remoteMapper.ForecastWeatherMapper
import reprator.mobiquity.cityDetail.modals.LocationRequestModal
import reprator.mobiquity.testUtils.MainCoroutineRule
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

@RunWith(JUnit4::class)
class ForeCastWeatherRemoteDataSourceImplTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutinesTestRule = MainCoroutineRule()

    @MockK
    lateinit var weatherApiService: WeatherApiService

    @MockK
    lateinit var forecastWeatherMapper: ForecastWeatherMapper

    private lateinit var foreCastWeatherRemoteDataSource: ForecastWeatherRemoteDataSource

    private val input = LocationRequestModal("24.31", "76.32")

    @Before
    fun setUp() {
        MockKAnnotations.init(this, true)

        foreCastWeatherRemoteDataSource = ForeCastWeatherRemoteDataSourceImpl(
            weatherApiService,
            forecastWeatherMapper
        )
    }

    @Test
    fun `fetch list successful from server`() = coroutinesTestRule.runBlockingTest {

        val output = getFakeLocationModalDataList()
        coEvery {
            weatherApiService.foreCastWeather(any(), any(), any(), any(), any())
        } returns Response.success(getFakeRemoteDataList())

        coEvery {
            forecastWeatherMapper.map(any())
        } returns output

        val result = foreCastWeatherRemoteDataSource.getForecastWeather(input)

        Truth.assertThat(result).isInstanceOf(Success::class.java)
        Truth.assertThat((result as Success).data).hasSize(2)
        Truth.assertThat(result.data).isEqualTo(output)
        Truth.assertThat(result.data[0]).isEqualTo(getFakeLocationModalDataList()[0])

        coVerifySequence {
            weatherApiService.foreCastWeather(any(), any(), any(), any(), any())
            forecastWeatherMapper.map(any())
        }
    }

    @Test
    fun `fetch list failed with errorBody`() = coroutinesTestRule.runBlockingTest {

        coEvery {
            weatherApiService.foreCastWeather(any(), any(), any(), any(), any())
        } returns Response.error(404, mockk(relaxed = true))

        val resp = foreCastWeatherRemoteDataSource.getForecastWeather(input)

        Truth.assertThat(resp).isInstanceOf(ErrorResult::class.java)
        Truth.assertThat((resp as ErrorResult).throwable).isInstanceOf(HttpException::class.java)
    }
}