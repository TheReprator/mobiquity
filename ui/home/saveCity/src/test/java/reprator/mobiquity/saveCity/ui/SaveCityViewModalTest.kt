package reprator.mobiquity.saveCity.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import reprator.mobiquity.androidTest.util.getOrAwaitValue
import reprator.mobiquity.androidTest.util.observeForTesting
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.saveCity.TestFakeData.getLocationModalList
import reprator.mobiquity.saveCity.domain.usecase.DeleteLocationUseCase
import reprator.mobiquity.saveCity.domain.usecase.GetLocationUseCase
import reprator.mobiquity.saveCity.domain.usecase.SearchItemUseCase
import reprator.mobiquity.testUtils.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SaveCityViewModalTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val coroutinesTestRule = MainCoroutineRule()

    @MockK
    lateinit var savedStateHandle: SavedStateHandle

    @MockK
    lateinit var getLocationUseCase: GetLocationUseCase

    @MockK
    lateinit var deleteLocationUseCase: DeleteLocationUseCase

    @MockK
    lateinit var searchItemUseCase: SearchItemUseCase

    lateinit var saveCityViewModal: SaveCityViewModal

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        saveCityViewModal =
            SaveCityViewModal(
                savedStateHandle, coroutinesTestRule.testDispatcherProvider,
                getLocationUseCase, deleteLocationUseCase, searchItemUseCase
            )

        coEvery {
            savedStateHandle.get<String>(any())
        } returns "London"
    }

    @Test
    fun `get savedLocationList`() = coroutinesTestRule.runBlockingTest {

        val output = getLocationModalList()

        coEvery {
            getLocationUseCase()
        } returns flowOf(Success(output))

        coEvery {
            searchItemUseCase(any(), any())
        } returns Success(output)

        coroutinesTestRule.pauseDispatcher()

        saveCityViewModal.getSavedLocationList()

        saveCityViewModal.bookMarkListManipulated.observeForTesting {
            Truth.assertThat(saveCityViewModal._isLoading.getOrAwaitValue()).isFalse()
            Truth.assertThat(saveCityViewModal._isError.getOrAwaitValue()).isEmpty()

            coroutinesTestRule.resumeDispatcher()

            Truth.assertThat(saveCityViewModal.bookMarkListManipulated.getOrAwaitValue())
                .hasSize(output.size)
        }
    }

    @Test
    fun `get savedLocationList fetch failed`() = coroutinesTestRule.runBlockingTest {

        val output = ErrorResult(message = "errorOccurred")

        coEvery {
            getLocationUseCase()
        } returns flowOf(output)

        coEvery {
            searchItemUseCase(any(), any())
        } returns Success(emptyList())

        coroutinesTestRule.pauseDispatcher()

        saveCityViewModal.getSavedLocationList()

        saveCityViewModal.bookMarkListManipulated.observeForTesting {
            Truth.assertThat(saveCityViewModal._isLoading.getOrAwaitValue()).isFalse()
            Truth.assertThat(saveCityViewModal._isError.getOrAwaitValue()).isEmpty()

            coroutinesTestRule.resumeDispatcher()

            Truth.assertThat(saveCityViewModal.bookMarkListManipulated.getOrAwaitValue()).isEmpty()
            Truth.assertThat(saveCityViewModal._isError.getOrAwaitValue()).isEqualTo(output.message)
        }
    }

    @Test
    fun `search item from given savedLocationList`() = coroutinesTestRule.runBlockingTest {

        val output = getLocationModalList()
        val searchString = "UK"

        coEvery {
            searchItemUseCase(any(), any())
        } returns Success(output)

        saveCityViewModal.searchServer(searchString)

        saveCityViewModal.bookMarkListManipulated.observeForTesting {

            Truth.assertThat(saveCityViewModal.bookMarkListManipulated.getOrAwaitValue()).isNotEmpty()
            Truth.assertThat(saveCityViewModal.bookMarkListManipulated.getOrAwaitValue()).isEqualTo(output)
        }
    }
}