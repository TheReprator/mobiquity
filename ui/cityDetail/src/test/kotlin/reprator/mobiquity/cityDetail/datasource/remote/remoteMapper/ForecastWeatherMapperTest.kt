package reprator.mobiquity.cityDetail.datasource.remote.remoteMapper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.spyk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import reprator.mobiquity.cityDetail.TestFakeData.getFakeLocationModalDataList
import reprator.mobiquity.cityDetail.TestFakeData.getFakeRemoteDataList
import reprator.mobiquity.testUtils.DateUtilsImpl

@RunWith(JUnit4::class)
class ForecastWeatherMapperTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    val dateUtils: DateUtilsImpl = DateUtilsImpl()

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `create the map into Rate list pojo class`() = runBlockingTest {
        val input = getFakeRemoteDataList()
        val output = getFakeLocationModalDataList()

        val mapper = spyk(ForecastWeatherMapper(dateUtils))

        val result = mapper.map(input)

        Truth.assertThat(output).isEqualTo(result)

        coVerify(atMost = 1) { mapper.map(input) }

        confirmVerified(mapper)
    }
}