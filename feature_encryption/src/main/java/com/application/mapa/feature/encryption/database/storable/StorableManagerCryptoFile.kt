package com.application.mapa.feature.encryption.database.storable

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.application.mapa.feature.encryption.database.Storable
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets


class StorableManagerCryptoFile(
    private val context: Context
) : StorableManager {

    override fun getStorable(): Storable? {
        val serializedString = getFileBytes(getEncryptedFile()).decodeToString()
        return try {
            Gson().fromJson(serializedString, object : TypeToken<Storable>() {}.type)
        } catch (ex: JsonSyntaxException) {
            null
        }
    }

    private fun getEncryptedFile(): EncryptedFile {
        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedFile.Builder(
            context,
            File(context.filesDir, FILE_NAME),
            mainKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    private fun getFileBytes(encryptedFile: EncryptedFile) =
        encryptedFile.openFileInput().use { inputStream ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            var nextByte: Int = inputStream.read()
            while (nextByte != -1) {
                byteArrayOutputStream.write(nextByte)
                nextByte = inputStream.read()
            }
            return@use byteArrayOutputStream
        }.use { it.toByteArray() }

    override fun saveStorable(storable: Storable) {
        val encryptedFile = getEncryptedFile()
        val fileContent = Gson().toJson(storable)
            .toByteArray(StandardCharsets.UTF_8)
        encryptedFile.openFileOutput().use {
            it.write(fileContent)
            it.flush()
        }
    }

    override fun storableEnabled(): Boolean {
        val file = File(context.filesDir, FILE_NAME)
        return if (file.exists()) {
            val size = file.length()
            size != 0L
        } else {
            false
        }
    }

    companion object {
        private const val FILE_NAME = "system"
    }
}