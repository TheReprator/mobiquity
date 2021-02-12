package reprator.mobiquity.database

import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.database.DBFakeData.getFakeDataList
import reprator.mobiquity.testUtils.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DBManagerImplTest {

    @JvmField
    @Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var weatherDao: LocationDao

    private lateinit var dbManager: DBManager

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        dbManager = DBManagerImpl(weatherDao)
    }

    @Test
    fun `No Records found`() = mainCoroutineRule.runBlockingTest {
        val input = emptyList<LocationEntity>()
        coEvery {
            weatherDao.getLocationList()
        } returns input

        val outputResult = dbManager.getLocationList().single()

        Truth.assertThat(outputResult).isInstanceOf(Success::class.java)
        Truth.assertThat(outputResult.get()).hasSize(0)
    }

    @Test
    fun saveLocation() = mainCoroutineRule.runBlockingTest {
        val input = getFakeDataList()[0]
        val expectedOutput = 0L

        coEvery {
            weatherDao.insertLocation(input)
        } returns expectedOutput

        val outputResult = dbManager.saveLocation(input).single()

        Truth.assertThat(outputResult.get()).isEqualTo(expectedOutput)
    }

    @Test
    fun getWeatherList() = mainCoroutineRule.runBlockingTest {
        val expectedOutput = getFakeDataList()
        coEvery {
            weatherDao.getLocationList()
        } returns expectedOutput

        val outputResult = dbManager.getLocationList().single()

        Truth.assertThat(outputResult.get()).isEqualTo(expectedOutput)
        Truth.assertThat(outputResult.get()).hasSize(2)
    }

    @Test
    fun deleteWeather() = mainCoroutineRule.runBlockingTest {
        val input = getFakeDataList()[0]
        val expectedOutput = 1
        coEvery {
            weatherDao.deleteLocation(input)
        } returns expectedOutput

        val outputResult = dbManager.deleteLocation(input).single()

        Truth.assertThat(outputResult.get()).isEqualTo(expectedOutput)
    }

    @Test
    fun clearTable() = mainCoroutineRule.runBlockingTest {
        val expectedOutput = 1
        coEvery {
            weatherDao.clearTable()
        } returns expectedOutput

        val outputResult = dbManager.clearTable().single()

        Truth.assertThat(outputResult.get()).isEqualTo(expectedOutput)
    }
}