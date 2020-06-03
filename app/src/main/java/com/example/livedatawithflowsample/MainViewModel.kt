package com.example.livedatawithflowsample

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.livedatawithflowsample.data.ThemeDataSource
import javax.inject.Inject

class MainViewModel(private val themeDataSource: ThemeDataSource) : ViewModel() {

    init {
        Log.d("MainViewModel", "$themeDataSource")
    }

    class Factory @Inject constructor(
        private val themeDataSource: ThemeDataSource
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(themeDataSource) as T
        }
    }
}