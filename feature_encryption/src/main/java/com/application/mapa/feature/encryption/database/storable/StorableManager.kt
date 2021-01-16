package com.application.mapa.feature.encryption.database.storable

import com.application.mapa.feature.encryption.database.Storable

interface StorableManager {

    fun getStorable(): Storable?

    fun saveStorable(storable: Storable)

    fun storableEnabled(): Boolean
}