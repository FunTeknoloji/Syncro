package com.example.quakesafe

import com.example.quakesafe.data.entities.MessageEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class MessageEntityTest {
    @Test
    fun testMessageCreation() {
        val message = MessageEntity(
            senderId = "user1",
            content = "Hello Mesh",
            timestamp = 123456789L,
            priority = 0
        )
        assertEquals("user1", message.senderId)
        assertEquals("Hello Mesh", message.content)
        assertEquals(0, message.priority)
    }
}
