package com.example.livedatawithflowsample.di

import android.content.Context
import android.content.SharedPreferences
import com.example.livedatawithflowsample.App
import com.example.livedatawithflowsample.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Provides
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }
}