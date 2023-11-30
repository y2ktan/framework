@file:JvmName("HashGenerator")
package com.ressphere.framework.authenticate

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

private const val HASHING_ALGORITHM = "SHA3-256"

fun generateSalt(): String {
    val secureRandom = SecureRandom()
    val salt = ByteArray(20)
    secureRandom.nextBytes(salt)
    return Base64.encodeToString(salt, Base64.NO_WRAP)
}

@Throws(NoSuchAlgorithmException::class)
fun generateHash(data: String, salt:String): String {
    val decodedSalt = Base64.decode(salt, Base64.NO_WRAP)
    val saltedData = data.toByteArray(StandardCharsets.UTF_8) + decodedSalt
    val messageDigest = MessageDigest.getInstance(HASHING_ALGORITHM)
    val hashedBytes = messageDigest.digest(saltedData)
    return Base64.encodeToString(hashedBytes, Base64.NO_WRAP)
}
