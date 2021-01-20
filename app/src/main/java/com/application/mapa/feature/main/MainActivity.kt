package com.application.mapa.feature.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.application.mapa.di.DatabaseFactory
import com.application.mapa.feature.password.data.PasswordDataScreen
import com.application.mapa.feature.password.data.PasswordDataViewModel
import com.application.mapa.feature.password.list.PasswordListScreen
import com.application.mapa.feature.password.list.PasswordListViewModel
import com.application.mapa.feature.password.master.MasterPasswordScreen
import com.application.mapa.feature.password.master.MasterPasswordViewModel
import com.application.mapa.feature.settings.SettingsScreen
import com.application.mapa.feature.settings.SettingsViewModel
import com.application.mapa.navigation.Destinations.CREATE_PASSWORD
import com.application.mapa.navigation.Destinations.MASTER_PASSWORD
import com.application.mapa.navigation.Destinations.PASSWORDS_LIST
import com.application.mapa.navigation.Destinations.PASSWORD_DETAILS
import com.application.mapa.navigation.Destinations.PasswordDataArgs.PASSWORD_ID
import com.application.mapa.navigation.Destinations.SETTINGS
import com.application.mapa.navigation.NavActions
import com.application.mapa.ui.MapaTheme
import com.application.mapa.util.ActivityProvider
import com.application.mapa.util.ObserveOnBackPressed
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var activityProvider: ActivityProvider

    @Inject
    lateinit var databaseFactory: DatabaseFactory

    private val passwordListViewModel: PasswordListViewModel by viewModels()

    private val passwordDataViewModel: PasswordDataViewModel by viewModels()

    private val masterPasswordViewModel: MasterPasswordViewModel by viewModels()

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProvider.setActivity(this)

        setContent {
            val navController = rememberNavController()
            val navActions = remember(navController) { NavActions(navController) }

            val darkThemeEnabledState = settingsViewModel.darkThemeEnabled.observeAsState()
            darkThemeEnabledState.value?.let { darkThemeEnabled ->
                MapaTheme(
                    darkTheme = darkThemeEnabled
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = MASTER_PASSWORD
                    ) {
                        composable(MASTER_PASSWORD) {
                            MasterPasswordScreen(
                                viewModel = masterPasswordViewModel,
                                navigateToPasswordList = navActions.passwordList,
                                onUnlockClick = {
                                    masterPasswordViewModel.verifyMasterPassword(it)
                                }
                            )
                        }
                        composable(PASSWORDS_LIST) {
                            this@MainActivity.ObserveOnBackPressed(PASSWORDS_LIST, navController) {
                                if (passwordListViewModel.state.selectionEnabled) {
                                    passwordListViewModel.disableSelection()
                                } else {
                                    finish()
                                }
                            }

                            PasswordListScreen(
                                passwords = passwordListViewModel.state.passwords,
                                onCreatePasswordClick = navActions.createPassword,
                                onDeletePasswordsClick = {
                                    passwordListViewModel.deleteSelectedPasswords()
                                },
                                onPasswordClick = {
                                    if (passwordListViewModel.state.selectionEnabled) {
                                        passwordListViewModel.selectPassword(it)
                                    } else {
                                        navActions.passwordDetails(it.password.id)
                                    }
                                },
                                onPasswordLongClick = {
                                    passwordListViewModel.selectPassword(it)
                                },
                                onPasswordChecked = {
                                    passwordListViewModel.selectPassword(it)
                                },
                                onCloseClicked = {
                                    passwordListViewModel.disableSelection()
                                },
                                onSettingsClicked = navActions.settings,
                                selectionEnabled = passwordListViewModel.state.selectionEnabled
                            )
                        }
                        composable(CREATE_PASSWORD) { backStackEntry ->
                            PasswordDataScreen(
                                null,
                                { passwordDataViewModel.savePassword(it) },
                                passwordDataViewModel,
                                navActions.navigateUp
                            )
                        }
                        composable(
                            "$PASSWORD_DETAILS/{$PASSWORD_ID}",
                            arguments = listOf(navArgument(PASSWORD_ID) { type = NavType.LongType })
                        ) { backStackEntry ->
                            PasswordDataScreen(
                                backStackEntry.arguments?.getLong(PASSWORD_ID) ?: -1,
                                { passwordDataViewModel.savePassword(it) },
                                passwordDataViewModel,
                                navActions.navigateUp
                            )
                        }
                        composable(SETTINGS) {
                            SettingsScreen(
                                viewModel = settingsViewModel,
                                onBackClicked = navActions.navigateUp
                            )
                        }
                    }
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
