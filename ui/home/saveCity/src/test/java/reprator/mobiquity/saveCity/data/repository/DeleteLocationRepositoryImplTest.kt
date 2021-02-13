package reprator.mobiquity.saveCity.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import reprator.mobiquity.database.DBManager
import reprator.mobiquity.saveCity.TestFakeData.getLocationEntity
import reprator.mobiquity.saveCity.data.repository.mapper.DeleteLocationMapper
import reprator.mobiquity.saveCity.modal.LocationModal
import reprator.mobiquity.testUtils.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DeleteLocationRepositoryImplTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutinesTestRule = MainCoroutineRule()

    @MockK
    lateinit var dbManager: DBManager

    @MockK
    lateinit var locationMapper: DeleteLocationMapper

    lateinit var deleteLocationRepository: DeleteLocationRepositoryImpl

    private val input = LocationModal("24.31,76.32", "London, UK")

    @Before
    fun setup() {
        MockKAnnotations.init(this, true)

        deleteLocationRepository = DeleteLocationRepositoryImpl(locationMapper, dbManager)
    }

    @Test
    fun `delete record from db`() =
        coroutinesTestRule.runBlockingTest {

            val output = Success(1)

            coEvery {
                dbManager.deleteLocation(any())
            } returns output

            coEvery {
                locationMapper.map(any())
            } returns getLocationEntity()

            val result = deleteLocationRepository.deleteLocation(input).single()

            Truth.assertThat(result).isInstanceOf(Success::class.java)
            Truth.assertThat(result).isEqualTo(output)
            Truth.assertThat((result as Success).data).isEqualTo(output.data)
        }
}