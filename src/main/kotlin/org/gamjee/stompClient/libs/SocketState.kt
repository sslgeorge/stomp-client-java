package org.gamjee.stompClient.libs

enum class SocketState {
    CLOSED,
    CLOSING,
    CONNECT_ERROR,
    OPEN,
    OPENING,
    RECONNECTING,
    RECONNECT_ATTEMPT,
}