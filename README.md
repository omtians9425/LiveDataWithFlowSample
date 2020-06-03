# LiveDataWithFlowSample
Implement Theme changes with ConflatedBroadcastChannel or StateFlow.

## Producer
ThemeDataSource.kt
~~~kotlin
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
~~~

MainViewModel.kt
~~~kotlin
 val theme =
     themeDataSource.themeFlow().asLiveData(viewModelScope.coroutineContext)

 val themeStateFlow =
     themeDataSource.themeStateFlow().asLiveData(viewModelScope.coroutineContext)
~~~


## Consumer
MainActivity.kt
~~~kotlin
 viewModel.theme.observe(this, Observer { theme ->
     if (USE_CHANNEL) changeTheme(theme)
 })
 viewModel.themeStateFlow.observe(this, Observer { theme ->
     if (!USE_CHANNEL) changeTheme(theme)
 })
~~~
