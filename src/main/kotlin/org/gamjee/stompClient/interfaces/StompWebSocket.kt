package org.gamjee.stompClient.interfaces

interface StompWebSocket {
    fun connect(): StompWebSocket
    fun disconnect()
    fun send(frame: MessageFrame)
    fun setStateChangeListener(listener: StompSocketListener) { }
}