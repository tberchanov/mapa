package com.application.mapa.feature.password.list

import androidx.compose.foundation.Image
import androidx.compose.material.Text
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.res.vectorResource
import androidx.ui.tooling.preview.Preview
import com.application.mapa.R
import com.application.mapa.data.domain.model.Password
import com.application.mapa.feature.password.list.model.SelectablePassword

@Preview
@Composable
fun PasswordListScreenPreview() {
    PasswordListScreen(
        passwords = listOf(
            SelectablePassword(Password(1, "Name", "Value"), false),
            SelectablePassword(Password(2, "Name2", "Value2"), false),
            SelectablePassword(Password(3, "Name3", "Value3"), false)
        )
    )
}

@Composable
fun PasswordListScreen(
    passwords: List<SelectablePassword>,
    onCreatePasswordClick: () -> Unit = {},
    onPasswordClick: (SelectablePassword) -> Unit = {},
    onPasswordLongClick: (SelectablePassword) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Passwords" /*TODO move to resources*/) },
                backgroundColor = Color.Gray /*TODO use color from resources*/
            )
        },
        floatingActionButton = {
            CreatePasswordButton(onCreatePasswordClick)
        },
        bodyContent = {
            LazyColumnFor(items = passwords) { password ->
                PasswordItem(password, onPasswordClick)
            }
        }
    )
}

@Composable
fun CreatePasswordButton(onCreatePasswordClick: () -> Unit) {
    FloatingActionButton(onClick = onCreatePasswordClick) {
        val image = loadVectorResource(id = R.drawable.ic_add)
        image.resource.resource?.let {
            Image(imageVector = it)
        }
    }
}