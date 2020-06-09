package com.example.livedatawithflowsample.data

import android.content.SharedPreferences
import com.example.livedatawithflowsample.PREFERENCE_KEY_THEME
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use Channel/StateFlow as entry point of stream.
 */
@ExperimentalCoroutinesApi
@Singleton
class ThemeDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    // By Channel
    private val _themeChannel: ConflatedBroadcastChannel<Theme> by lazy {
        ConflatedBroadcastChannel<Theme>().also { channel ->
            // Not read-safe because you have to remember to set the initial value.
            channel.offer(getDefaultTheme())
        }
    }
    val themeFlowByChannel: Flow<Theme>
        get() = _themeChannel.asFlow()

    // By StateFlow
    // read-safe: you must specify initial value.
    private val _themeStateFlow = MutableStateFlow(getDefaultTheme())
    val themeStateFlow: StateFlow<Theme>
        get() = _themeStateFlow

    fun toggleTheme() {
        val toggled = _themeChannel.value.toggle()
        save(toggled)

        // notify change
        _themeChannel.offer(toggled)
    }

    fun toggleThemeStateFlow() {
        val toggled = _themeStateFlow.value.toggle()
        save(toggled)

        // notify change
        _themeStateFlow.value = toggled
    }

    private fun getDefaultTheme(): Theme {
        val name = sharedPreferences.getString(
            PREFERENCE_KEY_THEME, null
        ) ?: Theme.LIGHT.name
        return Theme.valueOf(name)
    }

    private fun save(theme: Theme) {
        sharedPreferences
            .edit()
            .putString(PREFERENCE_KEY_THEME, theme.name)
            .apply()
    }
}
