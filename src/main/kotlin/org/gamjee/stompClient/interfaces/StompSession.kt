package org.gamjee.stompClient.interfaces

import org.gamjee.stompClient.libs.SessionState
import org.gamjee.stompClient.libs.SocketState
import org.gamjee.stompClient.libs.StompAck

interface StompSession {
    fun connect() : StompSession
    fun send(destination: String, message: String);
    fun setStateChangeListener(listener: StompSessionListener) { };
    fun getState(): SessionState? { return null }
    fun subscribe(destination: String, listener: StompFrameHandler) : String;
    fun subscribe(destination: String, listener: StompFrameHandler, ack: StompAck) : String;
    fun unsubscribe(subscriptionId: String);
    fun begin(): String;
    fun commit(transactionId: String);
    fun abort(transactionId: String);
    fun acknowledge(messageId: String);
    fun noneAcknowledged(messageId: String);
    fun disconnect() : String;
}