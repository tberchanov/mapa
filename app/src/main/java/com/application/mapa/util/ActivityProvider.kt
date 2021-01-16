package com.application.mapa.util

import androidx.appcompat.app.AppCompatActivity

class ActivityProvider {

    private var activity: AppCompatActivity? = null

    fun getActivity() = activity

    fun setActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

    fun clear() {
        activity = null
    }
}