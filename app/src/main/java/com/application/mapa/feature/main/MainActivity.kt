package com.application.mapa.feature.main

import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.FLAG_SECURE
import androidx.activity.compose.setContent
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.application.mapa.di.DatabaseFactory
import com.application.mapa.feature.settings.SettingsViewModel
import com.application.mapa.feature.settings.SettingsViewModelImpl
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

    @Inject
    lateinit var databaseFactory: DatabaseFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProvider.setActivity(this)

        window.setFlags(FLAG_SECURE, FLAG_SECURE)

        setContent {
            val navController = rememberNavController()
            val navActions = remember(navController) { NavActions(navController) }

            val settingsViewModel: SettingsViewModel = viewModelProvider.provideViewModel<SettingsViewModelImpl>(this)
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
        databaseFactory.closeDatabase()
        super.onDestroy()
    }
}
