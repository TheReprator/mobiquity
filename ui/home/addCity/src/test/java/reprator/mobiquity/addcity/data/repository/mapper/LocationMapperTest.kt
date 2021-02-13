package reprator.mobiquity.addcity.data.repository.mapper

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
import reprator.mobiquity.addcity.TestFakeData.getLocationEntityList
import reprator.mobiquity.addcity.TestFakeData.getLocationModalList

@RunWith(JUnit4::class)
class LocationMapperTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `map from Location to LocationEntity`() = runBlockingTest {
        val output = getLocationEntityList()[0]
        val input = getLocationModalList()[0]

        val mapper = spyk(LocationMapper())

        val result = mapper.map(input)

        Truth.assertThat(output).isEqualTo(result)

        coVerify(atMost = 1) { mapper.map(input) }

        confirmVerified(mapper)
    }
}