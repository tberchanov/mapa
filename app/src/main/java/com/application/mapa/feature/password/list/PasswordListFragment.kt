package com.application.mapa.feature.password.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.application.mapa.ui.MapaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordListFragment : Fragment() {

    private val viewModel: PasswordListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(context = requireContext()).apply {
            setContent {
                MapaTheme {
                    PasswordListScreen(
                        passwords = viewModel.state.passwords,
                        onCreatePasswordClick = {
                            findNavController().navigate(
                                PasswordListFragmentDirections.actionPasswordListToCreatePassword()
                            )
                        },
                        onPasswordClick = {
                            findNavController().navigate(
                                PasswordListFragmentDirections.actionPasswordListToPasswordDetails(it.password)
                            )
                        },
                        onPasswordLongClick = {
                            viewModel.selectPassword(it)
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadData()
    }
}