package reprator.mobiquity.saveCity.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import reprator.mobiquity.base.SaveSettingPreferenceManager
import reprator.mobiquity.base.SettingPreferenceManager
import reprator.mobiquity.database.DBManager
import reprator.mobiquity.navigation.AppNavigator
import reprator.mobiquity.navigation.SavedCityNavigator
import reprator.mobiquity.saveCity.data.repository.DeleteLocationRepositoryImpl
import reprator.mobiquity.saveCity.data.repository.GetLocationRepositoryImpl
import reprator.mobiquity.saveCity.data.repository.mapper.DeleteLocationMapper
import reprator.mobiquity.saveCity.data.repository.mapper.LocationMapper
import reprator.mobiquity.saveCity.domain.repository.DeleteLocationRepository
import reprator.mobiquity.saveCity.domain.repository.GetLocationRepository
import reprator.mobiquity.saveCity.domain.usecase.DeleteLocationUseCase
import reprator.mobiquity.saveCity.domain.usecase.GetLocationUseCase

@InstallIn(ViewModelComponent::class)
@Module
class SaveCityModule {

    @Provides
    fun provideDeleteLocationRepository(
        locationMapper: DeleteLocationMapper, dbManager: DBManager
    ): DeleteLocationRepository {
        return DeleteLocationRepositoryImpl(
            locationMapper, dbManager
        )
    }

    @Provides
    fun provideDeleteLocationUseCase(
        locationRepository: DeleteLocationRepository
    ): DeleteLocationUseCase {
        return DeleteLocationUseCase(locationRepository)
    }

    @Provides
    fun provideGetLocationRepository(
        locationMapper: LocationMapper, dbManager: DBManager,
        settingPreferenceManager: SettingPreferenceManager,
        saveSettingPreferenceManager: SaveSettingPreferenceManager
    ): GetLocationRepository {
        return GetLocationRepositoryImpl(
            locationMapper, dbManager, settingPreferenceManager,
            saveSettingPreferenceManager
        )
    }

    @Provides
    fun provideGetLocationUseCase(
        locationRepository: GetLocationRepository
    ): GetLocationUseCase {
        return GetLocationUseCase(locationRepository)
    }
}