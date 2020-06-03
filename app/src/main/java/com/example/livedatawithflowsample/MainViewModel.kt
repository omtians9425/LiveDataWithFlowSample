package com.example.livedatawithflowsample

import androidx.lifecycle.*
import com.example.livedatawithflowsample.data.Theme
import com.example.livedatawithflowsample.data.ThemeDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel(private val themeDataSource: ThemeDataSource) : ViewModel() {

    // changes of theme flows here
    private val _theme =
        themeDataSource.themeFlow().asLiveData(viewModelScope.coroutineContext)

    val theme: LiveData<Theme>
        get() = _theme

    fun toggleTheme() {
        themeDataSource.toggleTheme()
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