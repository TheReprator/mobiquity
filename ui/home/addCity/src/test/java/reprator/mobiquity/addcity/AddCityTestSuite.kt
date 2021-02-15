package reprator.mobiquity.addcity

import org.junit.runner.RunWith
import org.junit.runners.Suite
import reprator.mobiquity.addcity.data.repository.LocationRepositoryImplTest
import reprator.mobiquity.addcity.data.repository.mapper.LocationMapperTest
import reprator.mobiquity.addcity.domain.usecase.LocationUseCaseTest

@RunWith(Suite::class)
@Suite.SuiteClasses(
    LocationMapperTest::class,
    LocationRepositoryImplTest::class,
    LocationUseCaseTest::class,
    AddLocationViewModalTest::class
)
class AddCityTestSuite