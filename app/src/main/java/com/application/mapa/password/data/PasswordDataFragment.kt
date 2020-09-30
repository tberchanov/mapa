package com.application.mapa.password.data

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.application.mapa.data.model.Password
import com.application.mapa.ui.MapaTheme

class PasswordDataFragment : Fragment() {

    private val args: PasswordDataFragmentArgs by navArgs()

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

    private fun onSavePasswordClicked(password: Password) {
        Log.e("PasswordDataFragment", "onSavePasswordClicked: $password")
        findNavController().popBackStack()
    }
}