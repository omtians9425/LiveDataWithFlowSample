package com.example.livedatawithflowsample.di

import com.example.livedatawithflowsample.App
import com.example.livedatawithflowsample.MainViewModel
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityModule::class,
    AppModule::class
])
interface AppComponent : AndroidInjector<App> {
    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<App>

//    fun mainViewModelFactory(): MainViewModel.Factory
}