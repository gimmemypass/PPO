package com.example.third.utils
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Utils {
    companion object{
         fun md5(mail: String): String? {
            var result: String? = null
            try {
                val digest =
                    MessageDigest.getInstance("MD5")
                digest.reset()
                digest.update(mail.toByteArray())
                val bigInt = BigInteger(1, digest.digest())
                result = bigInt.toString(16)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return result
        }

    }
}