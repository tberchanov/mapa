package com.application.mapa.feature.launch

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.application.mapa.NavActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {

    private val viewModel: LaunchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeState()
        viewModel.verifyEncryption(applicationContext)
    }

    private fun observeState() {
        viewModel.state.observe(this) {
            if (it == LaunchState.EncryptionVerified) {
                openNavActivity()
            }
        }
    }

    private fun openNavActivity() {
        startActivity(
            Intent(
                this,
                NavActivity::class.java
            )
        )
        finish()
    }
}