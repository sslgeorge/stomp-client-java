package org.gamjee.stompClient

import okhttp3.OkHttpClient
import okhttp3.Request
import org.gamjee.stompClient.interfaces.StompSession
import org.gamjee.stompClient.interfaces.StompSessionListener
import org.gamjee.stompClient.interfaces.StompSocketListener
import org.gamjee.stompClient.libs.StompSessionImpl
import org.gamjee.stompClient.libs.StompWebsocketImpl

class StompClient private constructor(
        private val request: Request.Builder
) {
    private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
    private var socketListener: StompSocketListener? = null
    private var sessionListener: StompSessionListener? = null

    companion object Builder {
        fun url(url: String): StompClient {
            val startsWithWs = url.regionMatches( 0, "ws://", 0, 5)
            val startsWithWss =  url.regionMatches(0, "wss://", 0, 6)

            if (!startsWithWs && !startsWithWss) {
                throw IllegalArgumentException("Invalid socket url provided, $url, needs to start with wss or ws ");
            }

            return StompClient(Request.Builder().url("$url/websocket"))
        }
    }

    fun setSocketStateListener(listener: StompSocketListener) : StompClient {
        this.socketListener = listener

        return this
    }

    fun setSessionStateListener(listener: StompSessionListener) : StompClient {
        this.sessionListener = listener

        return this
    }

    fun build(): StompSession {
        val socket = StompWebsocketImpl.init(request.build(), httpClient)
        val session = StompSessionImpl.init(socket)
        socketListener?.let { socket.setStateChangeListener(it) }
        sessionListener?.let { session.setStateChangeListener(it) }
        return session
    }

}