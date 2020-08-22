package org.gamjee.stompClient.libs

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.StringBuilder

class StompMessageCodec  {
    fun decode(message: String) : StompMessage {
        val nullRegex = FrameDelimiters.NULL.toRegex()
        val bodySeparatorRegex = "\n\n".toRegex()
        val newLineRegex = "\n".toRegex()
        val headerRegex = ":".toRegex()

        val nullRemoved = nullRegex.split(message).getOrElse(0) {
            throw IllegalArgumentException("Malformed message")
        }
        val messageSplit = bodySeparatorRegex.split(nullRemoved)

        val headerString = messageSplit.getOrElse(0) {
            throw IllegalStateException("Missing header and or command")
        }
        val fullHeaders = newLineRegex.split(headerString)

        val commandString = fullHeaders.getOrElse(0) {
            throw IllegalStateException("Command is missing from message")
        }
        val command = Command.valueOf(commandString)

        val headers = fullHeaders.drop(1).map {
            val header = headerRegex.split(it)
            val key = header.getOrElse(0) {
                throw IllegalStateException("Stomp header is invalid")
            }
            val value = header.getOrNull(1)

            StompHeader(key, value)
        }

        var body: String? = messageSplit.getOrNull(1)
        if (body == "") {
            body = null
        }

        return StompMessage(
                command,
                headers,
                body
        )
    }

    fun encode(message: StompMessage): String {
        val frame = StringBuilder(message.command.value).append("\n")
        message.headers.forEach { frame.append("${it.key}:${it.value}").append("\n") }
        message.body?.let { frame.append("content-length:${it.length}").append("\n") }
        frame.append("\n")
        message.body?.let { frame.append(it) }

        frame.append(FrameDelimiters.NULL)

        return frame.toString()
    }
}