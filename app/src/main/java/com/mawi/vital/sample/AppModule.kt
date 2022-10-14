package com.mawi.vital.sample

import android.app.Application
import com.mawi.vital.ble.MawiVital
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMawiVital(application: Application) = MawiVital(application, BuildConfig.API_KEY)

}