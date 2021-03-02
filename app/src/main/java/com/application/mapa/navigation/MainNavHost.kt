package com.application.mapa.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.application.mapa.feature.main.ViewModelProvider
import com.application.mapa.feature.password.data.PasswordDataScreen
import com.application.mapa.feature.password.data.PasswordDataViewModelImpl
import com.application.mapa.feature.password.generator.PasswordGeneratorScreen
import com.application.mapa.feature.password.generator.PasswordGeneratorViewModelImpl
import com.application.mapa.feature.password.generator.model.CurrentPasswordArg
import com.application.mapa.feature.password.list.PasswordListScreen
import com.application.mapa.feature.password.list.PasswordListViewModel
import com.application.mapa.feature.password.list.PasswordListViewModelImpl
import com.application.mapa.feature.password.master.MasterPasswordScreen
import com.application.mapa.feature.password.master.MasterPasswordViewModelImpl
import com.application.mapa.feature.settings.SettingsScreen
import com.application.mapa.feature.settings.SettingsViewModelImpl
import com.application.mapa.util.ActivityProvider
import com.application.mapa.util.ObserveOnBackPressed

@Composable
fun MainNavHost(
    navController: NavHostController,
    navActions: NavActions,
    viewModelProvider: ViewModelProvider,
    activityProvider: ActivityProvider
) {
    val activity = remember { activityProvider.getActivity() }
    activity?.let {
        NavHost(
            navController = navController,
            startDestination = Destinations.MASTER_PASSWORD
        ) {
            composable(Destinations.MASTER_PASSWORD) {
                MasterPasswordScreen(
                    viewModel = viewModelProvider.provideViewModel<MasterPasswordViewModelImpl>(activity),
                    navigateToPasswordList = navActions.passwordList,
                )
            }
            composable(Destinations.PASSWORDS_LIST) {
                val passwordListViewModel: PasswordListViewModel =
                    viewModelProvider.provideViewModel<PasswordListViewModelImpl>(activity)

                activity.ObserveOnBackPressed(Destinations.PASSWORDS_LIST, navController) {
                    if (passwordListViewModel.state.value?.selectionEnabled == true) {
                        passwordListViewModel.disableSelection()
                    } else {
                        activity.finish()
                    }
                }

                PasswordListScreen(
                    viewModel = passwordListViewModel,
                    onCreatePasswordClick = navActions.createPassword,
                    onSettingsClicked = navActions.settings,
                    passwordDetails = navActions.passwordDetails
                )
            }
            composable(Destinations.CREATE_PASSWORD) {
                PasswordDataScreen(
                    null,
                    viewModelProvider.provideViewModel<PasswordDataViewModelImpl>(activity),
                    navActions.navigateUp,
                    { navActions.passwordGenerator(CurrentPasswordArg(it)) }
                )
            }
            composable(
                "${Destinations.PASSWORD_DETAILS}/{${Destinations.PasswordDataArgs.PASSWORD_ID}}",
                arguments = listOf(navArgument(Destinations.PasswordDataArgs.PASSWORD_ID) {
                    type = NavType.LongType
                })
            ) { backStackEntry ->
                PasswordDataScreen(
                    backStackEntry.arguments?.getLong(Destinations.PasswordDataArgs.PASSWORD_ID)
                        ?: -1,
                    viewModelProvider.provideViewModel<PasswordDataViewModelImpl>(activity),
                    navActions.navigateUp,
                    { navActions.passwordGenerator(CurrentPasswordArg(it)) }
                )
            }
            composable(Destinations.SETTINGS) {
                SettingsScreen(
                    viewModel = viewModelProvider.provideViewModel<SettingsViewModelImpl>(activity),
                    onBackClicked = navActions.navigateUp
                )
            }
            composable(
                "${Destinations.PASSWORD_GENERATOR}/{${Destinations.PasswordGeneratorArgs.CURRENT_PASSWORD}}",
                arguments = listOf(
                    navArgument(Destinations.PasswordGeneratorArgs.CURRENT_PASSWORD) {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val currentPasswordArg = backStackEntry
                    .arguments
                    ?.getString(Destinations.PasswordGeneratorArgs.CURRENT_PASSWORD)
                    ?.let { CurrentPasswordArg.fromString(it) }
                PasswordGeneratorScreen(
                    currentPasswordArg,
                    navActions.navigateUp,
                    viewModelProvider.provideViewModel<PasswordGeneratorViewModelImpl>(activity)
                )
            }
        }
    }
}