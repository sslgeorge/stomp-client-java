package org.gamjee.stompClient.interfaces

import org.gamjee.stompClient.libs.SocketState
import org.gamjee.stompClient.libs.StompMessage

interface StompSocketListener {
    fun onChange(state: SocketState)
    fun onMessage(text: StompMessage) {}
}