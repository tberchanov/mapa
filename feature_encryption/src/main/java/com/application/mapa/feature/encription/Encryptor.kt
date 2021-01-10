package com.application.mapa.feature.encription

import android.os.Build
import android.util.Base64
import com.application.mapa.feature.encription.storable.StorableManager
import java.security.AlgorithmParameters
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class Encryptor(
    private val storableManager: StorableManager,
    private val keyGenerator: KeyGenerator
) {

    fun persistRawKey(rawByteKey: ByteArray, userPasscode: CharArray) {
        val storable = toStorable(rawByteKey, userPasscode)
        storableManager.saveStorable(storable)
    }

    /**
     * Returns a [Storable] instance with the db key encrypted using PBE.
     *
     * @param rawDbKey the raw database key
     * @param userPasscode the user's passcode
     * @return storable instance
     */
    private fun toStorable(rawDbKey: ByteArray, userPasscode: CharArray): Storable {
        // Generate a random 8 byte salt
        val salt = ByteArray(8).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                SecureRandom.getInstanceStrong().nextBytes(this)
            } else {
                SecureRandom().nextBytes(this)
            }
        }
        val secret: SecretKey = keyGenerator.generateSecretKey(userPasscode, salt)

        // Now encrypt the database key with PBE
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        val params: AlgorithmParameters = cipher.parameters
        val iv: ByteArray = params.getParameterSpec(IvParameterSpec::class.java).iv
        val ciphertext: ByteArray = cipher.doFinal(rawDbKey)

        // Return the IV and CipherText which can be stored to disk
        return Storable(
            Base64.encodeToString(iv, Base64.DEFAULT),
            Base64.encodeToString(ciphertext, Base64.DEFAULT),
            Base64.encodeToString(salt, Base64.DEFAULT)
        )
    }
}