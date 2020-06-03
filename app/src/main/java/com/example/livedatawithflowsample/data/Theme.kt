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

enum class Theme {
    DARK, LIGHT;

    fun toggle(): Theme {
        return when (this) {
            DARK -> LIGHT
            LIGHT -> DARK
        }
    }
}

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
            val defaultTheme = sharedPreferences.getString(
                PREFERENCE_KEY_THEME, null
            ) ?: Theme.LIGHT.name

            // Not read-safe because you don't forget to set initial value.
            channel.offer(Theme.valueOf(defaultTheme))
        }
    }

    // By StateFlow
    // read-safe: you must specify initial value.
    private val themeStateFlow = MutableStateFlow(
        Theme.valueOf(
            sharedPreferences.getString(PREFERENCE_KEY_THEME, null)
                ?: Theme.LIGHT.name
        )
    )

    @FlowPreview
    fun themeFlow(): Flow<Theme> {
        return themeChannel.asFlow()
    }

    fun themeStateFlow(): Flow<Theme> {
        return themeStateFlow
    }

    fun toggleTheme() {
        val toggled = themeChannel.value.toggle()
        sharedPreferences
            .edit()
            .putString(PREFERENCE_KEY_THEME, toggled.name)
            .apply()

        // notify
        themeChannel.offer(toggled)
    }

    fun toggleThemeStateFlow() {
        val toggled = themeStateFlow.value.toggle()
        sharedPreferences
            .edit()
            .putString(PREFERENCE_KEY_THEME, toggled.name)
            .apply()

        // notify
        themeStateFlow.value = toggled
    }

}
