package reprator.mobiquity.saveCity.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.saveCity.TestFakeData.getLocationModalList
import reprator.mobiquity.saveCity.modal.LocationModal
import reprator.mobiquity.testUtils.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SearchItemUseCaseTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutinesTestRule = MainCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this, true)
    }

    @Test
    fun `filter matched item, found`() = coroutinesTestRule.runBlockingTest {

        val input = getLocationModalList()
        val query = "London"

        val searchItemUseCase = spyk(SearchItemUseCase())

        val result = searchItemUseCase(input, query)

        Truth.assertThat(result).isInstanceOf(Success::class.java)
        Truth.assertThat((result as Success).data).isEqualTo(input)

        coVerify(atMost = 1) { searchItemUseCase(input, query) }

        confirmVerified(searchItemUseCase)
    }

    @Test
    fun `filter matched item, No element found`() = coroutinesTestRule.runBlockingTest {

        val input = getLocationModalList()
        val query = "Hajipur"
        val output = emptyList<LocationModal>()

        val searchItemUseCase = spyk(SearchItemUseCase())

        val result = searchItemUseCase(input, query)

        Truth.assertThat(result).isInstanceOf(Success::class.java)
        Truth.assertThat((result as Success).data).isEqualTo(output)

        coVerify(atMost = 1) { searchItemUseCase(input, query) }

        confirmVerified(searchItemUseCase)
    }

    @Test
    fun `filter matched item, No element found, when matched list is empty`() = coroutinesTestRule.runBlockingTest {

        val input =  emptyList<LocationModal>()
        val query = "UK"
        val output = "No record exist"

        val searchItemUseCase = spyk(SearchItemUseCase())

        val result = searchItemUseCase(input, query)

        Truth.assertThat(result).isInstanceOf(ErrorResult::class.java)
        Truth.assertThat((result as ErrorResult).message).isEqualTo(output)

        coVerify(atMost = 1) { searchItemUseCase(input, query) }

        confirmVerified(searchItemUseCase)
    }

    @Test
    fun `return original list, when query is empty`() = coroutinesTestRule.runBlockingTest {

        val input = getLocationModalList()
        val query = " "

        val searchItemUseCase = spyk(SearchItemUseCase())

        val result = searchItemUseCase(input, query)

        Truth.assertThat(result).isInstanceOf(Success::class.java)
        Truth.assertThat((result as Success).data).isEqualTo(input)

        coVerify(atMost = 1) { searchItemUseCase(input, query) }

        confirmVerified(searchItemUseCase)
    }
}