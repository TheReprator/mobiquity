package reprator.mobiquity.addcity.di

import android.content.Context
import android.location.Geocoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import reprator.mobiquity.addcity.IsLocationEnabled
import reprator.mobiquity.addcity.IsLocationEnabledImpl
import reprator.mobiquity.addcity.ReverseGeoCoding
import reprator.mobiquity.addcity.data.repository.LocationRepositoryImpl
import reprator.mobiquity.addcity.data.repository.mapper.LocationMapper
import reprator.mobiquity.addcity.domain.repository.LocationRepository
import reprator.mobiquity.addcity.domain.usecase.LocationUseCase
import reprator.mobiquity.database.DBManager

@InstallIn(FragmentComponent::class)
@Module
object AddCityFragmentModule {

    @Provides
    fun provideIsLocationEnabled(@ActivityContext context: Context): IsLocationEnabled {
        return IsLocationEnabledImpl(context)
    }

}

