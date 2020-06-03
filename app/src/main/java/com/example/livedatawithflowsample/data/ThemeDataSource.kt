package com.example.livedatawithflowsample.data

import android.content.SharedPreferences
import com.example.livedatawithflowsample.PREFERENCE_KEY_THEME
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val themeChannel: ConflatedBroadcastChannel<Theme> by lazy {
        ConflatedBroadcastChannel<Theme>().also { channel ->
            // Not read-safe because you don't forget to set initial value.
            channel.offer(getDefaultTheme())
        }
    }

    // By StateFlow
    // read-safe: you must specify initial value.
    private val themeStateFlow = MutableStateFlow(getDefaultTheme())

    @FlowPreview
    fun themeFlow(): Flow<Theme> {
        return themeChannel.asFlow()
    }

    fun themeStateFlow(): Flow<Theme> {
        return themeStateFlow
    }

    fun toggleTheme() {
        val toggled = themeChannel.value.toggle()
        save(toggled)

        // notify change
        themeChannel.offer(toggled)
    }

    fun toggleThemeStateFlow() {
        val toggled = themeStateFlow.value.toggle()
        save(toggled)

        // notify change
        themeStateFlow.value = toggled
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
