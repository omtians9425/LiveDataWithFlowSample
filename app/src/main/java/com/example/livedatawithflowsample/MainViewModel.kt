package com.example.livedatawithflowsample

import androidx.lifecycle.*
import com.example.livedatawithflowsample.data.ThemeDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel(private val themeDataSource: ThemeDataSource) : ViewModel() {

    val theme =
        themeDataSource.themeFlow().asLiveData(viewModelScope.coroutineContext)

    val themeStateFlow =
        themeDataSource.themeStateFlow().asLiveData(viewModelScope.coroutineContext)

    fun toggleTheme() {
        themeDataSource.toggleTheme()
    }

    fun toggleThemeStateFlow() {
        themeDataSource.toggleThemeStateFlow()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val themeDataSource: ThemeDataSource
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(themeDataSource) as T
        }
    }
}