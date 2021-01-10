package com.application.mapa.feature.encription.storable

import com.application.mapa.feature.encription.Storable

interface StorableManager {

    fun getStorable(): Storable?

    fun saveStorable(storable: Storable)

    fun storableEnabled(): Boolean
}