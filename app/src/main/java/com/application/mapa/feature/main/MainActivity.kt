package com.application.mapa.feature.main

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.application.mapa.feature.password.data.PasswordDataScreen
import com.application.mapa.feature.password.data.PasswordDataState
import com.application.mapa.feature.password.data.PasswordDataViewModel
import com.application.mapa.feature.password.list.PasswordListScreen
import com.application.mapa.feature.password.list.PasswordListViewModel
import com.application.mapa.navigation.Destinations.CREATE_PASSWORD
import com.application.mapa.navigation.Destinations.PASSWORDS_LIST
import com.application.mapa.navigation.Destinations.PASSWORD_DETAILS
import com.application.mapa.navigation.Destinations.PasswordDataArgs.PASSWORD_ID
import com.application.mapa.navigation.NavActions
import com.application.mapa.ui.MapaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val passwordListViewModel: PasswordListViewModel by viewModels()

    private val passwordDataViewModel: PasswordDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            navController.enableOnBackPressed(true)
            val navActions = remember(navController) { NavActions(navController) }

            // TODO it should be inside of PasswordDataScreen
            passwordDataViewModel.state.observe(this) {
                if (it == PasswordDataState.SavingSuccess) {
                    navActions.navigateUp()
                }
            }

            this@MainActivity.onBackPressedDispatcher.addCallback() {
                val currentRoute = navController.currentDestination
                    ?.arguments?.get(KEY_ROUTE)?.defaultValue as? String
                if (currentRoute == PASSWORDS_LIST) {
                    onBackPasswordListClicked()
                }
            }

            MapaTheme {
                NavHost(
                    navController = navController,
                    startDestination = PASSWORDS_LIST
                ) {
                    composable(PASSWORDS_LIST) {
                        PasswordListScreen(
                            passwords = passwordListViewModel.state.passwords,
                            onCreatePasswordClick = {
                                navActions.createPassword()
                            },
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
                            selectionEnabled = passwordListViewModel.state.selectionEnabled
                        )
                    }
                    composable(CREATE_PASSWORD) { backStackEntry ->
                        PasswordDataScreen(
                            null,
                            { passwordDataViewModel.savePassword(it) },
                            passwordDataViewModel
                        )
                    }
                    composable(
                        "$PASSWORD_DETAILS/{$PASSWORD_ID}",
                        arguments = listOf(navArgument(PASSWORD_ID) { type = NavType.LongType })
                    ) { backStackEntry ->
                        PasswordDataScreen(
                            backStackEntry.arguments?.getLong(PASSWORD_ID) ?: -1,
                            { passwordDataViewModel.savePassword(it) },
                            passwordDataViewModel
                        )
                    }
                }
            }
        }
    }

    private fun onBackPasswordListClicked() {
        if (passwordListViewModel.state.selectionEnabled) {
            passwordListViewModel.disableSelection()
        } else {
            finish()
        }
    }
}
