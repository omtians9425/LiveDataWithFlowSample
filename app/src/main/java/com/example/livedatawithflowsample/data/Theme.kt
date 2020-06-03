package com.example.livedatawithflowsample.data

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

enum class Theme {
    DART, LIGHT
}

@Singleton
class ThemeDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

}