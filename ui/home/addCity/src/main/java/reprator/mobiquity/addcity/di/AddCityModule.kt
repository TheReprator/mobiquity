package reprator.mobiquity.addcity.di

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import reprator.mobiquity.addcity.IsLocationEnabled
import reprator.mobiquity.addcity.IsLocationEnabledImpl
import reprator.mobiquity.addcity.ReverseGeoCoding
import reprator.mobiquity.addcity.data.repository.LocationRepositoryImpl
import reprator.mobiquity.addcity.data.repository.mapper.LocationMapper
import reprator.mobiquity.addcity.domain.repository.LocationRepository
import reprator.mobiquity.addcity.domain.usecase.LocationUseCase
import reprator.mobiquity.base_android.util.isAndroidPOrLater
import reprator.mobiquity.database.DBManager
import javax.inject.Inject

@InstallIn(ActivityComponent::class)
@Module
object DiAddCityModule {

    @Provides
    fun provideIsLocationEnabled(@ActivityContext context: Context): IsLocationEnabled {
        return IsLocationEnabledImpl(context)
    }

    @Provides
    fun provideGeoCoder(@ActivityContext context: Context): Geocoder {
        return Geocoder(context)
    }

    @Provides
    fun provideReverseGeoCoding(geoCoder: Geocoder): ReverseGeoCoding {
        return ReverseGeoCoding(geoCoder)
    }

    @Provides
    fun provideLocationRepository(
        locationMapper: LocationMapper, dbManager: DBManager
    ): LocationRepository {
        return LocationRepositoryImpl(locationMapper, dbManager)
    }

    @Provides
    fun provideLocationUseCase(
        locationRepository: LocationRepository
    ): LocationUseCase {
        return LocationUseCase(locationRepository)
    }
}

