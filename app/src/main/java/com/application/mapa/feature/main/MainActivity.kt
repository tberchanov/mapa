package com.application.mapa.feature.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.setContent
import androidx.navigation.compose.rememberNavController
import com.application.mapa.feature.settings.SettingsViewModel
import com.application.mapa.navigation.MainNavHost
import com.application.mapa.navigation.NavActions
import com.application.mapa.ui.MapaTheme
import com.application.mapa.util.ActivityProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var activityProvider: ActivityProvider

    @Inject
    lateinit var viewModelProvider: ViewModelProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProvider.setActivity(this)

        setContent {
            val navController = rememberNavController()
            val navActions = remember(navController) { NavActions(navController) }

            val settingsViewModel: SettingsViewModel = viewModelProvider.provideViewModel(this)
            val darkThemeEnabledState = settingsViewModel.darkThemeEnabled.observeAsState()
            darkThemeEnabledState.value?.let { darkThemeEnabled ->
                MapaTheme(
                    darkTheme = darkThemeEnabled
                ) {
                    MainNavHost(
                        navController,
                        navActions,
                        viewModelProvider,
                        activityProvider
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        activityProvider.clear()
        super.onDestroy()
    }
}
