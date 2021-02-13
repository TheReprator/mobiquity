package reprator.mobiquity.saveCity.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import reprator.mobiquity.base.SaveSettingPreferenceManager
import reprator.mobiquity.base.SettingPreferenceManager
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.database.DBManager
import reprator.mobiquity.saveCity.TestFakeData.getLocationEntityList
import reprator.mobiquity.saveCity.TestFakeData.getLocationModalList
import reprator.mobiquity.saveCity.data.repository.mapper.LocationMapper
import reprator.mobiquity.saveCity.modal.LocationModal
import reprator.mobiquity.testUtils.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GetLocationRepositoryImplTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutinesTestRule = MainCoroutineRule()

    @MockK
    lateinit var dbManager: DBManager

    @MockK
    lateinit var locationMapper: LocationMapper

    @MockK
    lateinit var settingPreferenceManager: SettingPreferenceManager

    private lateinit var coroutineScope: CoroutineScope

    @MockK
    lateinit var saveSettingPreferenceManager: SaveSettingPreferenceManager

    lateinit var getLocationRepository: GetLocationRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, true)

        coroutineScope = TestCoroutineScope(coroutinesTestRule.dispatcher)

        getLocationRepository = GetLocationRepositoryImpl(
            locationMapper,
            dbManager,
            coroutineScope,
            settingPreferenceManager,
            saveSettingPreferenceManager
        )
    }

    @Test
    fun `clearDatabase, as user had chosen to reset from settings, & return empty list`() =
        coroutinesTestRule.runBlockingTest {

            val outputClearTable = Success(1)
            val output = emptyList<LocationModal>()

            every {
                settingPreferenceManager.shouldClearSavedLocation
            } returns true

            every {
                saveSettingPreferenceManager.saveClearSavedLocation = false
            } returns Unit

            coEvery {
                dbManager.clearTable()
            } returns flowOf(outputClearTable)

            val result = getLocationRepository.getLocationList().single()

            Truth.assertThat(result).isInstanceOf(Success::class.java)
            Truth.assertThat((result as Success).data).isEqualTo(output)
            Truth.assertThat(result.data).isEmpty()

            coVerifySequence {
                settingPreferenceManager.shouldClearSavedLocation
                dbManager.clearTable()
                saveSettingPreferenceManager.saveClearSavedLocation = false
            }
        }

    @Test
    fun `fetch data from database`() =
        coroutinesTestRule.runBlockingTest {

            val input = getLocationEntityList()
            val output = getLocationModalList()

            every {
                settingPreferenceManager.shouldClearSavedLocation
            } returns false

            coEvery {
                dbManager.getLocationList()
            } returns flowOf(Success(input))

            coEvery {
                locationMapper.map(any())
            } returns output

            val result = getLocationRepository.getLocationList().single()

            Truth.assertThat(result).isInstanceOf(Success::class.java)
            Truth.assertThat((result as Success).data).isEqualTo(output)
            Truth.assertThat(result.data).isNotEmpty()
        }

    @Test
    fun `errorOccurred while fetching data from database`() =
        coroutinesTestRule.runBlockingTest {

            val output = "An error occcured"

            every {
                settingPreferenceManager.shouldClearSavedLocation
            } returns false

            coEvery {
                dbManager.getLocationList()
            } returns flowOf(ErrorResult(message = output))

            val result = getLocationRepository.getLocationList().single()

            Truth.assertThat(result).isInstanceOf(ErrorResult::class.java)
            Truth.assertThat((result as ErrorResult).message).isEqualTo(output)
        }
}