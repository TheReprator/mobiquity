package reprator.mobiquity.addcity.di

import android.content.Context
import android.location.Geocoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import reprator.mobiquity.addcity.ReverseGeoCoding
import reprator.mobiquity.addcity.data.repository.LocationRepositoryImpl
import reprator.mobiquity.addcity.data.repository.mapper.LocationMapper
import reprator.mobiquity.addcity.domain.repository.LocationRepository
import reprator.mobiquity.addcity.domain.usecase.LocationUseCase
import reprator.mobiquity.database.DBManager

@InstallIn(ViewModelComponent::class)
@Module
object AddCityModule {

    @Provides
    fun provideGeoCoder(@ApplicationContext context: Context): Geocoder {
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

