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
import reprator.mobiquity.saveCity.domain.repository.DeleteLocationRepository
import reprator.mobiquity.saveCity.modal.LocationModal
import reprator.mobiquity.testUtils.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DeleteLocationUseCaseTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutinesTestRule = MainCoroutineRule()

    @MockK
    lateinit var locationRepository: DeleteLocationRepository

    lateinit var deleteLocationUseCase: DeleteLocationUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, true)

        deleteLocationUseCase = DeleteLocationUseCase(locationRepository)
    }

    @Test
    fun `successfull delete from repo`() =
        coroutinesTestRule.runBlockingTest {

            val input = LocationModal("43.21,76.21", "London, UK")

            val output = Success(1)

            coEvery {
                locationRepository.deleteLocation(any())
            } returns flowOf(output)

            val result = deleteLocationUseCase(input).single()

            Truth.assertThat(result).isInstanceOf(Success::class.java)
            Truth.assertThat((result as Success).data).isEqualTo(output.data)
        }

    @Test
    fun `failed delete from repo`() =
        coroutinesTestRule.runBlockingTest {

            val input = LocationModal("43.21,76.21", "London, UK")

            val output = ErrorResult(message = "An Error Occurred")

            coEvery {
                locationRepository.deleteLocation(any())
            } returns flowOf(output)

            val result = deleteLocationUseCase(input).single()

            Truth.assertThat(result).isInstanceOf(ErrorResult::class.java)
            Truth.assertThat(result).isEqualTo(output)
            Truth.assertThat((result as ErrorResult).message).isEqualTo(output.message)
        }

}