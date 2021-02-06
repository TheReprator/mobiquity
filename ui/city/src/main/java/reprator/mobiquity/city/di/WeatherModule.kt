package reprator.mobiquity.city.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import reprator.mobiquity.base.util.ConnectionDetector
import reprator.mobiquity.city.data.datasource.ForecastWeatherRemoteDataSource
import reprator.mobiquity.city.data.datasource.TodayRemoteDataSource
import reprator.mobiquity.city.data.repository.ForecastWeatherRepositoryImpl
import reprator.mobiquity.city.data.repository.TodayRepositoryImpl
import reprator.mobiquity.city.datasource.remote.ForeCastWeatherRemoteDataSourceImpl
import reprator.mobiquity.city.datasource.remote.GetTodayWeatherRemoteDataSourceImpl
import reprator.mobiquity.city.datasource.remote.WeatherApiService
import reprator.mobiquity.city.datasource.remote.remoteMapper.ForecastWeatherMapper
import reprator.mobiquity.city.datasource.remote.remoteMapper.TodayWeatherMapper
import reprator.mobiquity.city.domain.repository.ForecastWeatherRepository
import reprator.mobiquity.city.domain.repository.GetTodayWeatherRepository
import reprator.mobiquity.city.domain.usecase.ForecastWeatherUseCase
import reprator.mobiquity.city.domain.usecase.GetTodayWeatherUseCase
import retrofit2.Retrofit

@InstallIn(ActivityComponent::class)
@Module
class WeatherModule {

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
    fun provideGetTodayWeatherRemoteDataSource(
        weatherApiService: WeatherApiService,
        todayWeatherMapper: TodayWeatherMapper
    ): TodayRemoteDataSource {
        return GetTodayWeatherRemoteDataSourceImpl(
            weatherApiService,
            todayWeatherMapper
        )
    }

    @Provides
    fun provideGetTodayWeatherRepository(
        todayRemoteDataSource: TodayRemoteDataSource,
        connectionDetector: ConnectionDetector
    ): GetTodayWeatherRepository {
        return TodayRepositoryImpl(
            todayRemoteDataSource,
            connectionDetector
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
    fun provideGetTodayWeatherUseCase(
        getTodayWeatherRepository: GetTodayWeatherRepository
    ): GetTodayWeatherUseCase {
        return GetTodayWeatherUseCase(getTodayWeatherRepository)
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

}