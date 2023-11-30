package com.ressphere.framework

import com.ressphere.framework.authenticate.generateHash
import com.ressphere.framework.authenticate.generateSalt
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test


class HashGeneratorTest {
    @Before
    fun setup() {
        mockkStatic(android.util.Base64::class)
        every { android.util.Base64.encodeToString(any(), any()) } answers {
            val bytes = arg<ByteArray>(0)
            java.util.Base64.getEncoder().encodeToString(bytes)
        }

        every { android.util.Base64.decode(any<String>(), any()) } answers {
            java.util.Base64.getDecoder().decode(arg<String>(0))
        }
    }

    @After
    fun teardown() {
        unmockkStatic(android.util.Base64::class)
    }

    @Test
    fun `test generated salt`() {
        val salt = generateSalt()
        assertEquals(28, salt.length)
    }

    @Test
    fun `test generated hash`() {
        val salt = generateSalt()
        val hash = generateHash("password", salt)
        assertEquals(44, hash.length)
    }

    @Test
    fun `test generated hash is randomized`() {
        val hash1 = generateHash("password", generateSalt())
        val hash2 = generateHash("password", generateSalt())
        assertNotEquals(hash1, hash2)
    }

    @Test
    fun `test generated hash with same salt are equal`() {
        val salt = generateSalt()
        val hash1 = generateHash("password", salt)
        val hash2 = generateHash("password", salt)
        assertEquals(hash1, hash2)
    }
}