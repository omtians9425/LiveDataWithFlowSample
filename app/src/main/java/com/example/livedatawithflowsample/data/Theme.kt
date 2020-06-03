package com.example.livedatawithflowsample.data

import android.content.SharedPreferences
import com.example.livedatawithflowsample.PREFERENCE_KEY_THEME
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class Theme {
    DARK, LIGHT;

    fun toggle(): Theme {
        return when(this) {
            DARK -> LIGHT
            LIGHT -> DARK
        }
    }
}

/**
 * Use Channel as entry point of stream.
 */
@ExperimentalCoroutinesApi
@Singleton
class ThemeDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    private val themeChannel: ConflatedBroadcastChannel<Theme> by lazy {
        ConflatedBroadcastChannel<Theme>().also { channel ->
            val defaultTheme = sharedPreferences.getString(
                PREFERENCE_KEY_THEME, null
            ) ?: Theme.LIGHT.name

            channel.offer(Theme.valueOf(defaultTheme))
        }
    }

    @FlowPreview
    fun themeFlow(): Flow<Theme> {
        return themeChannel.asFlow()
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
}
