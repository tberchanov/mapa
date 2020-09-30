package com.application.mapa.password.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.application.mapa.data.model.Password
import com.application.mapa.ui.MapaTheme

class PasswordListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(context = requireContext()).apply {
            setContent {
                MapaTheme {
                    PasswordListScreen(
                        // TODO load data
                        passwords = listOf(
                            Password("1", "Name", "Value"),
                            Password("2", "Name2", "Value2"),
                            Password("3", "Name3", "Value3")
                        ),
                        onCreatePasswordClick = {
                            findNavController().navigate(
                                PasswordListFragmentDirections.actionPasswordListToCreatePassword()
                            )
                        },
                        onPasswordClick = {
                            findNavController().navigate(
                                PasswordListFragmentDirections.actionPasswordListToPasswordDetails(it)
                            )
                        }
                    )
                }
            }
        }
    }
}