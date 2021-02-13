package reprator.mobiquity.addcity.domain.usecase

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
import reprator.mobiquity.addcity.TestFakeData
import reprator.mobiquity.addcity.domain.repository.LocationRepository
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.testUtils.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LocationUseCaseTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutinesTestRule = MainCoroutineRule()

    @MockK
    lateinit var locationRepository: LocationRepository

    private lateinit var locationUseCase: LocationUseCase

    private val input = TestFakeData.getLocationModalList()[0]

    @Before
    fun setup() {
        MockKAnnotations.init(this, true)

        locationUseCase = LocationUseCase(locationRepository)
    }

    @Test
    fun `save record in repository`() =
        coroutinesTestRule.runBlockingTest {

            val output = Success(1L)

            coEvery {
                locationRepository.saveLocation(any())
            } returns flowOf(output)

            val result = locationRepository.saveLocation(input).single()

            Truth.assertThat(result).isInstanceOf(Success::class.java)
            Truth.assertThat(result).isEqualTo(output)
            Truth.assertThat((result as Success).data).isEqualTo(output.data)
        }
}