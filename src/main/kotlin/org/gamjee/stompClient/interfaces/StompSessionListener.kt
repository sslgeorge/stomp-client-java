package org.gamjee.stompClient.interfaces

import org.gamjee.stompClient.libs.SessionState
import org.gamjee.stompClient.libs.SocketState

interface StompSessionListener {
    fun onChange(state: SessionState)
}