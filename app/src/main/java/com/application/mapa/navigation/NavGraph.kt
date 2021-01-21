package com.application.mapa.navigation

import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.application.mapa.navigation.Destinations.CREATE_PASSWORD
import com.application.mapa.navigation.Destinations.PASSWORDS_LIST
import com.application.mapa.navigation.Destinations.PASSWORD_DETAILS
import com.application.mapa.navigation.Destinations.PASSWORD_GENERATOR
import com.application.mapa.navigation.Destinations.SETTINGS

object Destinations {
    const val PASSWORDS_LIST = "PASSWORDS_LIST"
    const val CREATE_PASSWORD = "CREATE_PASSWORD"
    const val PASSWORD_DETAILS = "PASSWORD_DETAILS"
    const val MASTER_PASSWORD = "MASTER_PASSWORD"
    const val SETTINGS = "SETTINGS"
    const val PASSWORD_GENERATOR = "PASSWORD_GENERATOR"

    object PasswordDataArgs {
        const val PASSWORD_ID = "PASSWORD_ID"
    }

    object PasswordGeneratorArgs {
        const val CURRENT_PASSWORD = "CURRENT_PASSWORD"
    }
}

class NavActions(navController: NavHostController) {
    val createPassword: () -> Unit = {
        navController.navigate(CREATE_PASSWORD)
    }
    val passwordDetails: (Long) -> Unit = { passwordId ->
        navController.navigate("$PASSWORD_DETAILS/$passwordId")
    }
    val navigateUp: () -> Unit = {
        navController.popBackStack()
    }
    val passwordList: () -> Unit = {
        navController.navigate(PASSWORDS_LIST)
    }
    val settings: () -> Unit = {
        navController.navigate(SETTINGS)
    }
    val passwordGenerator: (String) -> Unit = { password ->
        navController.navigate("$PASSWORD_GENERATOR/$password")
    }
}