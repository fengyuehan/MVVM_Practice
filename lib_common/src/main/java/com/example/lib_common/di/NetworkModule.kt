package com.example.lib_common.di

import com.example.lib_base.VersionStatus
import com.example.lib_common.BuildConfig
import com.example.lib_common.constant.NetBaseUrlConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideOkhttpClient() :OkHttpClient {
        // 日志拦截器部分
        val level = if (BuildConfig.VERSION_TYPE != VersionStatus.RELEASE) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val logInterceptor = HttpLoggingInterceptor().setLevel(level)

        return OkHttpClient.Builder()
            .connectTimeout(15L * 1000L, TimeUnit.MILLISECONDS)
            .readTimeout(20L * 1000L, TimeUnit.MILLISECONDS)
            .addInterceptor(logInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) :Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetBaseUrlConstant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}