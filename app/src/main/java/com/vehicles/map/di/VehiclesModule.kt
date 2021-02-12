package com.vehicles.map.di

import android.content.Context
import com.vehicles.map.BuildConfig
import com.vehicles.map.network.ApiInterface
import com.vehicles.map.network.datasource.AppDataSource
import com.vehicles.map.network.datasource.RemoteDataSource
import com.vehicles.map.utils.VehicleNetworkHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@InstallIn(SingletonComponent::class)
@Module
object VehiclesModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()
        } else OkHttpClient
            .Builder()
            .build()
    }

    @Provides
    fun provideNetworkHelper(@ApplicationContext appContext: Context) = VehicleNetworkHelper(appContext)

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://fake-poi-api.mytaxi.com/") //base url for api calling
            .client(okHttpClient)
            .build()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Provides
    fun provideApiHelper(apiService: ApiInterface): AppDataSource = RemoteDataSource(apiService)

}