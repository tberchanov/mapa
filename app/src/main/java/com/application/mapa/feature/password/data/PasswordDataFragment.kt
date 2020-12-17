package com.application.mapa.feature.password.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.application.mapa.data.domain.model.Password
import com.application.mapa.ui.MapaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordDataFragment : Fragment() {

    private val args: PasswordDataFragmentArgs by navArgs()

    private val viewModel: PasswordDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(context = requireContext()).apply {
            setContent {
                MapaTheme {
                    PasswordDataScreen(args.password, ::onSavePasswordClicked)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) {
            if (it == PasswordDataState.SavingSuccess) {
                findNavController().popBackStack()
            }
        }
    }

    private fun onSavePasswordClicked(password: Password) {
        viewModel.savePassword(password)
    }
}