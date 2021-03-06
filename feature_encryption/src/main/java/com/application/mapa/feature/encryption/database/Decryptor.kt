package com.application.mapa.feature.encryption.database

import android.util.Base64
import com.application.mapa.feature.encryption.database.storable.StorableManager
import com.application.mapa.feature.encryption.database.util.toHex
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class Decryptor(
    private val storableManager: StorableManager,
    private val keyGenerator: KeyGenerator,
    private val encryptor: Encryptor
) {

    /**
     * Decrypts the [Storable] instance using the [passcode].
     *
     * @pararm passcode the user's passcode
     * @param storable the storable instance previously saved with [StorableManager.saveStorable]
     * @return the raw byte key previously generated with [generateRandomKey]
     */
    @Suppress("KDocUnresolvedReference")
    fun getRawByteKey(
        passcode: CharArray,
        storable: Storable
    ): ByteArray {
        val aesWrappedKey = Base64.decode(storable.key, Base64.DEFAULT)
        val iv = Base64.decode(storable.iv, Base64.DEFAULT)
        val salt = Base64.decode(storable.salt, Base64.DEFAULT)
        val secret: SecretKey = keyGenerator.generateSecretKey(passcode, salt)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secret, IvParameterSpec(iv))
        return cipher.doFinal(aesWrappedKey)
    }

    /**
     * Returns the database key suitable for using with Room.
     *
     * @param passcode the user's passcode
     */
    fun getCharKey(passcode: CharArray): CharArray {
        if (keyGenerator.dbCharKey == null) {
            initKey(passcode)
        }
        return keyGenerator.dbCharKey ?: error("Failed to decrypt database key")
    }

    private fun initKey(passcode: CharArray) {
        val storable = storableManager.getStorable()
        if (storable == null) {
            keyGenerator.createNewKey()
            encryptor.persistRawKey(keyGenerator.rawByteKey, passcode)
        } else {
            keyGenerator.rawByteKey = getRawByteKey(passcode, storable)
            keyGenerator.dbCharKey = keyGenerator.rawByteKey.toHex().toCharArray()
        }
    }
}