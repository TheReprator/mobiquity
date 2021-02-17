package reprator.mobiquity.cityDetail.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        JackSonModule::class
    ]
)
open class NetworkModule {

    @Provides
    fun provideMovieOkHttpClient(
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
        return client.build()
    }

    @Provides
    open fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: JacksonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://localhost:8080/3/")
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }
}
