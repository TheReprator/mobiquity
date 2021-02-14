package reprator.mobiquity.saveCity

import org.junit.runner.RunWith
import org.junit.runners.Suite
import reprator.mobiquity.saveCity.data.repository.DeleteLocationRepositoryImplTest
import reprator.mobiquity.saveCity.data.repository.GetLocationRepositoryImplTest
import reprator.mobiquity.saveCity.data.repository.mapper.DeleteLocationMapperTest
import reprator.mobiquity.saveCity.data.repository.mapper.LocationMapperTest
import reprator.mobiquity.saveCity.domain.usecase.DeleteLocationUseCaseTest
import reprator.mobiquity.saveCity.domain.usecase.GetLocationUseCaseTest
import reprator.mobiquity.saveCity.domain.usecase.SearchItemUseCaseTest
import reprator.mobiquity.saveCity.ui.SaveCityViewModalTest

@RunWith(Suite::class)
@Suite.SuiteClasses(
    DeleteLocationMapperTest::class,
    LocationMapperTest::class,
    DeleteLocationRepositoryImplTest::class,
    GetLocationRepositoryImplTest::class,
    DeleteLocationUseCaseTest::class,
    GetLocationUseCaseTest::class,
    SearchItemUseCaseTest::class,
    SaveCityViewModalTest::class
)
class SaveCitylTestSuite