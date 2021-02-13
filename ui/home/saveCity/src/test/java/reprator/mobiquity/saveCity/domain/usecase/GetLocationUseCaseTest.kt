package reprator.mobiquity.saveCity.domain.usecase

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
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.saveCity.TestFakeData.getLocationModalList
import reprator.mobiquity.saveCity.domain.repository.GetLocationRepository
import reprator.mobiquity.testUtils.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GetLocationUseCaseTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutinesTestRule = MainCoroutineRule()

    @MockK
    lateinit var locationRepository: GetLocationRepository

    lateinit var getLocationUseCase: GetLocationUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, true)

        getLocationUseCase = GetLocationUseCase(locationRepository)
    }

    @Test
    fun `successfull fetch from repo`() =
        coroutinesTestRule.runBlockingTest {

            val output = getLocationModalList()

            coEvery {
                locationRepository.getLocationList()
            } returns flowOf(Success(output))

            val result = getLocationUseCase().single()

            Truth.assertThat(result).isInstanceOf(Success::class.java)
            Truth.assertThat((result as Success).data).isEqualTo(output)
        }

    @Test
    fun `failed to fetch list from repo`() =
        coroutinesTestRule.runBlockingTest {

            val output = ErrorResult(message = "An Error Occurred")

            coEvery {
                locationRepository.getLocationList()
            } returns flowOf(output)

            val result = getLocationUseCase().single()

            Truth.assertThat(result).isInstanceOf(ErrorResult::class.java)
            Truth.assertThat(result).isEqualTo(output)
            Truth.assertThat((result as ErrorResult).message).isEqualTo(output.message)
        }

}
