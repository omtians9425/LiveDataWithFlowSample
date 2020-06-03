package com.example.livedatawithflowsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.livedatawithflowsample.data.Theme
import com.example.livedatawithflowsample.databinding.ActivityMainBinding
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: MainViewModel.Factory

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        viewModel.theme.observe(this, Observer { theme ->
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (theme) {
                Theme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        })

        binding.themeButton.setOnClickListener {
            viewModel.toggleTheme()
        }
    }
}