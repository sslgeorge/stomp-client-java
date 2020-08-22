package org.gamjee.stompClient.libs

import okhttp3.*
import okhttp3.internal.ws.RealWebSocket
import org.gamjee.stompClient.interfaces.*
import java.util.logging.Logger

class StompWebsocketImpl private constructor(
        private val request: Request,
        private val httpClient: OkHttpClient.Builder
) : StompWebSocket {

    private val webSocketListener = object: WebSocketListener() {

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            logger.info("\t\nMessage received\n<<<<\n$text\n\n")
            val message = StompMessageCodec().decode(text)

            listeners.forEach{ it.value.onMessage(message) }
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)

            logger.info("Web socket opened...")
            setSocketState(SocketState.OPEN)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)

            setSocketState(SocketState.CONNECT_ERROR)
            logger.info("Web socket opening failed: ${t.message}")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)

            setSocketState(SocketState.CLOSED)
            logger.info("Connection closed: $reason")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)

            setSocketState(SocketState.CLOSING)
            logger.info("Connection closing: $reason")
        }
    }

    companion object Builder {
        private val logger = Logger.getLogger("StompSession")
        private lateinit var listeners: HashMap<String, StompSocketListener>
        private var socketState: SocketState? = null
        private lateinit var socket: RealWebSocket

        fun init(
                request: Request,
                httpClient: OkHttpClient.Builder
        ) :  StompWebSocket {
            listeners = HashMap()
            return StompWebsocketImpl(request, httpClient)
        }
    }

    override fun connect() : StompWebSocket {
        logger.info("OPENING websocket...")
        setSocketState(SocketState.OPENING)
        socket = httpClient.build()
                .newWebSocket(request, webSocketListener) as RealWebSocket;
        return this
    }

    override fun disconnect() {
        logger.info("CLOSING websocket...")
        setSocketState(SocketState.CLOSING)
//        socket.close()
    }

    override fun send(frame: MessageFrame) {
        val message = frame.encode()
        logger.info("\t\n\n>>>>\n${message}\n\n")

        socket.send(message)
    }

    override fun setStateChangeListener(listener: StompSocketListener) {
        listeners[listener.hashCode().toString()] = listener
    }


    private fun setSocketState(state: SocketState) {
        socketState = state
        listeners.forEach{
            it.value.onChange(state)
        }
    }

}
