package reprator.mobiquity.cityDetail.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import reprator.mobiquity.base.util.ConnectionDetector
import reprator.mobiquity.cityDetail.data.datasource.ForecastWeatherRemoteDataSource
import reprator.mobiquity.cityDetail.data.repository.ForecastWeatherRepositoryImpl
import reprator.mobiquity.cityDetail.datasource.remote.ForeCastWeatherRemoteDataSourceImpl
import reprator.mobiquity.cityDetail.datasource.remote.WeatherApiService
import reprator.mobiquity.cityDetail.datasource.remote.remoteMapper.ForecastWeatherMapper
import reprator.mobiquity.cityDetail.domain.repository.ForecastWeatherRepository
import reprator.mobiquity.cityDetail.domain.usecase.ForecastWeatherUseCase
import reprator.mobiquity.navigation.AppNavigator
import reprator.mobiquity.navigation.CityDetailNavigator
import retrofit2.Retrofit

@InstallIn(ViewModelComponent::class)
@Module
class CityDetailModule {

    @Provides
    fun provideForecastWeatherRemoteDataSource(
        weatherApiService: WeatherApiService,
        forecastWeatherMapper: ForecastWeatherMapper
    ): ForecastWeatherRemoteDataSource {
        return ForeCastWeatherRemoteDataSourceImpl(
            weatherApiService,
            forecastWeatherMapper
        )
    }

    @Provides
    fun provideForecastWeatherRepository(
        forecastWeatherRemoteDataSource: ForecastWeatherRemoteDataSource,
        connectionDetector: ConnectionDetector
    ): ForecastWeatherRepository {
        return ForecastWeatherRepositoryImpl(
            forecastWeatherRemoteDataSource,
            connectionDetector
        )
    }

    @Provides
    fun provideForecastWeatherUseCase(
        forecastWeatherRepository: ForecastWeatherRepository
    ): ForecastWeatherUseCase {
        return ForecastWeatherUseCase(forecastWeatherRepository)
    }

    @Provides
    fun provideWeatherApiService(
        retrofit: Retrofit
    ): WeatherApiService {
        return retrofit
            .create(WeatherApiService::class.java)
    }

    @Provides
    fun provideCityDetailNavigator(
        appNavigator: AppNavigator
    ): CityDetailNavigator {
        return appNavigator
    }

}