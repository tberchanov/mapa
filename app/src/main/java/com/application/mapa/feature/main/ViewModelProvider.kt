package com.application.mapa.feature.main

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel

class ViewModelProvider {

    val viewModelsList = mutableListOf<ViewModel>()

    inline fun <reified VM : ViewModel> ComponentActivity.initViewModel(): VM {
        val viewModel: VM by viewModels()
        viewModelsList.add(viewModel)
        return viewModel
    }

    inline fun <reified VM : ViewModel> provideViewModel(activity: ComponentActivity): VM {
        return viewModelsList.find { it is VM } as? VM ?: activity.initViewModel()
    }
}