# LiveDataWithFlowSample
Implement Theme changes with ConflatedBroadcastChannel or StateFlow.

## Producer
ThemeDataSource.kt
~~~kotlin
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
val theme = themeDataSource.themeFlowByChannel.asLiveData(viewModelScope.coroutineContext)

val themeStateFlow =nthemeDataSource.themeStateFlow.asLiveData(viewModelScope.coroutineContext)
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
