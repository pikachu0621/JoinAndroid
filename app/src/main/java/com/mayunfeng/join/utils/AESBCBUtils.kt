package com.mayunfeng.join.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object AESBCBUtils {


    private lateinit var ivs: IvParameterSpec
    private lateinit var keys: SecretKeySpec
    private lateinit var cipher: Cipher
    private const val hexStr = "0123456789ABCDEF"
    private val hexCode = hexStr.toCharArray()

    fun init(aesKey: String) {
        val coding = "UTF-8"
        val key = md5(aesKey, true)
        val iv = md5(key!!.substring(0, 8), true)
        try {
            ivs = IvParameterSpec(iv!!.toByteArray(charset(coding)))
            keys = SecretKeySpec(key.toByteArray(charset(coding)), "AES")
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        } catch (_: Exception) { }
    }


    /**
     * 加密
     *
     * @param value str
     * @return str
     */
    fun encrypt(value: String): String? {
        return try {
            cipher.init(Cipher.ENCRYPT_MODE, keys, ivs)
            bytesToHexStr(cipher.doFinal(value.toByteArray(StandardCharsets.UTF_8)))
        } catch (e: Exception) {
            null
        }

    }


    /**
     * 解密
     *
     * @param encrypted str
     * @return str
     */
    fun decrypt(encrypted: String): String? {
        return try {
            cipher.init(Cipher.DECRYPT_MODE, keys, ivs)
            val decode = hexStrToBytes(encrypted)
           String(cipher.doFinal(decode))
        } catch (_: Exception){
            null
        }
    }





    /**
     * 2 to 16
     *
     * @param data bytes
     * @return string
     */
    fun bytesToHexStr(data: ByteArray): String {
        val r = StringBuilder(data.size * 2)
        for (b in data) {
            r.append(hexCode[b.toInt() shr 4 and 0xF])
            r.append(hexCode[b.toInt() and 0xF])
        }
        return r.toString()
    }


    /**
     * 16 to 2
     *
     * @param hex 16 string
     * @return bytes
     */
    fun hexStrToBytes(hex: String): ByteArray {
        val len = hex.length / 2
        val result = ByteArray(len)
        val abhor = hex.toCharArray()
        for (i in 0 until len) {
            val pos = i * 2
            result[i] = (toByte(abhor[pos]) shl 4 or toByte(abhor[pos + 1])).toByte()
        }
        return result
    }

    private fun toByte(c: Char): Int {
        return hexStr.indexOf(c).toByte().toInt()
    }




    /**
     * md5
     *
     * @param sourceStr 字符串
     * @param is16      是否为16位长度
     * @return MD5
     */
    fun md5(sourceStr: String, is16: Boolean): String? {
        var md532: String? = null
        var md516: String? = null
        val result: String
        try {
            val md: MessageDigest = MessageDigest.getInstance("MD5")
            md.update(sourceStr.toByteArray())
            val b: ByteArray = md.digest()
            var i: Int
            val buf = StringBuilder()
            for (value in b) {
                i = value.toInt()
                if (i < 0) i += 256
                if (i < 16) buf.append("0")
                buf.append(Integer.toHexString(i))
            }
            result = buf.toString()
            md532 = result
            md516 = buf.substring(8, 24)
        } catch (_: NoSuchAlgorithmException) {
        }
        return if (is16) md516 else md532
    }

}