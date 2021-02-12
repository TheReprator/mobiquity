package reprator.mobiquity.saveCity.data.repository.mapper

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
import reprator.mobiquity.saveCity.TestFakeData.getLocationEntity
import reprator.mobiquity.saveCity.TestFakeData.getLocationModal

@RunWith(JUnit4::class)
class DeleteLocationMapperTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `map from LocationModal to LocationEntity`() = runBlockingTest {
        val input = getLocationModal()
        val output = getLocationEntity()

        val mapper = spyk(DeleteLocationMapper())

        val result = mapper.map(input)

        Truth.assertThat(output).isEqualTo(result)

        coVerify(atMost = 1) { mapper.map(input) }

        confirmVerified(mapper)
    }
}