package com.example.livedatawithflowsample.data

enum class Theme {
    DARK, LIGHT;

    fun toggle(): Theme {
        return when (this) {
            DARK -> LIGHT
            LIGHT -> DARK
        }
    }
}
