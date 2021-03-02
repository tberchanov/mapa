package com.application.mapa.feature.password.generator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.mapa.R
import com.application.mapa.feature.password.generator.model.CurrentPasswordArg
import com.application.mapa.feature.password.generator.model.PasswordGeneratorScreenAction.*
import com.application.mapa.ui.components.BackIconButton

@Composable
fun PasswordGeneratorScreen(
    currentPasswordArg: CurrentPasswordArg?,
    onBackClicked: () -> Unit,
    viewModel: PasswordGeneratorViewModel
) {
    viewModel.postAction(
        ModifyGeneratedPassword(currentPasswordArg?.currentPassword ?: "")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.password_generator))
                },
                navigationIcon = {
                    BackIconButton(onBackClicked)
                }
            )
        },
        content = {
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    val state by viewModel.state.observeAsState()

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 8.dp
                            )
                            .fillMaxWidth(),
                        value = state?.generatedPassword ?: "",
                        onValueChange = { viewModel.postAction(ModifyGeneratedPassword(it)) },
                        label = { Text(text = stringResource(R.string.generated_password)) },
                    )
                    Row(
                        modifier = Modifier.padding(top = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(Modifier.weight(1f))
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = stringResource(R.string.settings)
                        )
                        Divider(Modifier.weight(1f))
                    }
                    state?.settings?.forEach { setting ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 8.dp),
                                text = stringResource(setting.nameRes),
                                fontSize = 20.sp,
                            )
                            Switch(
                                checked = setting.enabled,
                                onCheckedChange = {
                                    viewModel.postAction(
                                        ModifyGenerationSetting(setting.copy(enabled = it))
                                    )
                                }
                            )
                        }
                    }
                    Slider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        valueRange = viewModel.getPasswordLengthRange(),
                        value = state?.length?.toFloat() ?: 0f,
                        onValueChange = {
                            viewModel.postAction(ModifyPasswordLength(it.toInt()))
                        }
                    )
                    Text(text = stringResource(R.string.format_length, state?.length ?: 0))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { viewModel.postAction(GeneratePassword) }) {
                            Text(text = stringResource(id = R.string.generate))
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Button(onClick = {
                            viewModel.postAction(ApplyPassword)
                            onBackClicked()
                        }) {
                            Text(text = stringResource(id = R.string.apply))
                        }
                    }
                    if (state?.showNoSettingEnabledError == true) {
                        Text(
                            text = stringResource(R.string.no_generation_settings_selected_error),
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
        }
    )
}
