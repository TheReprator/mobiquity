package reprator.mobiquity.cityDetail

import org.junit.runner.RunWith
import org.junit.runners.Suite
import reprator.mobiquity.cityDetail.data.repository.ForecastWeatherRepositoryImplTest
import reprator.mobiquity.cityDetail.datasource.remote.ForeCastWeatherRemoteDataSourceImplTest
import reprator.mobiquity.cityDetail.datasource.remote.api.ForeCastWeatherApiTest
import reprator.mobiquity.cityDetail.datasource.remote.remoteMapper.ForecastWeatherMapperTest
import reprator.mobiquity.cityDetail.domain.usecase.ForecastWeatherUseCaseTest

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ForecastWeatherRepositoryImplTest::class,
    ForeCastWeatherApiTest::class,
    ForecastWeatherMapperTest::class,
    ForeCastWeatherRemoteDataSourceImplTest::class,
    ForecastWeatherUseCaseTest::class,
    CityDetailViewModalTest::class,
)
class CityDetailTestSuite