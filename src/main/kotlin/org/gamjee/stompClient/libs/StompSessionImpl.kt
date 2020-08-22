package org.gamjee.stompClient.libs

import org.gamjee.stompClient.interfaces.StompSession
import org.gamjee.stompClient.interfaces.StompSessionListener
import org.gamjee.stompClient.interfaces.StompSocketListener
import org.gamjee.stompClient.interfaces.StompWebSocket
import java.util.*
import java.util.logging.Logger

class StompSessionImpl private constructor(
        private val socket: StompWebSocket
) : StompSession {
    private val logger = Logger.getLogger("StompSession")
    private var sessionListener: StompSessionListener? = null
    private var sessionState: SessionState?  = null

    private val socketListener = object : StompSocketListener {
        override fun onChange(state: SocketState) {
            when(state) {
                SocketState.OPEN -> connect()
                else -> { }
            }
        }

        override fun onMessage(text: StompMessage) {
            if(text.command == Command.CONNECTED)  {
                setSessionState(SessionState.CONNECTED)
            }

            if(text.command == Command.DISCONNECT)  {
                socket.disconnect()
                setSessionState(SessionState.DISCONNECTED)
            }
        }
    }

    init {
        socket.setStateChangeListener(socketListener)
        socket.connect()
    }

    companion object Builder {
        var subscriptionsCount = 0
        var transactionsCount = 0
        var receiptCount = 0
        fun init(
                stompWebSocket: StompWebSocket
        ) : StompSession {
            return StompSessionImpl(stompWebSocket)
        }
    }

    override fun connect() : StompSession {
        val frame = MessageFrameImpl
                .Builder()
                .addCommand(Command.CONNECT)
                .addHeader("accept-version", "1.1,1.2")
                .addHeader("heart-beat", "10000,10000")
                .build()
        socket.send(frame)
        return this
    }

    override fun send(destination: String, message: String) {
        val frame = MessageFrameImpl
                .Builder()
                .addCommand(Command.SEND)
                .addHeader("destination", destination)
                .addBody(message)
                .build()
        socket.send(frame)
    }

    override fun setStateChangeListener(listener: StompSessionListener) {
        this.sessionListener = listener
    }

    override fun subscribe(destination: String) : String {
        return this.subscribe(destination, StompAck.AUTO)
    }

    override fun subscribe(destination: String, ack: StompAck) : String {
        val id = getNextSubscriptionId()
        val frame = MessageFrameImpl
                .Builder()
                .addCommand(Command.SUBSCRIBE)
                .addHeader("id", id)
                .addHeader("ack", ack.value)
                .addHeader("destination", destination)
                .build()
        socket.send(frame)


        return id
    }

    override fun unsubscribe(subscriptionId: String) {
        val frame = MessageFrameImpl
                .Builder()
                .addCommand(Command.UNSUBSCRIBE)
                .addHeader("id", subscriptionId)
                .build()
        socket.send(frame)
    }

    override fun begin(): String {
        val id = getNextTransactionId()
        val frame = MessageFrameImpl
                .Builder()
                .addCommand(Command.BEGIN)
                .addHeader("transaction", id)
                .build()
        socket.send(frame)

        return id
    }

    override fun commit(transactionId: String) {
        val frame = MessageFrameImpl
                .Builder()
                .addCommand(Command.COMMIT)
                .addHeader("transaction", transactionId)
                .build()
        socket.send(frame)
    }

    override fun abort(transactionId: String) {
        val frame = MessageFrameImpl
                .Builder()
                .addCommand(Command.ABORT)
                .addHeader("transaction", transactionId)
                .build()
        socket.send(frame)
    }

    override fun acknowledge(messageId: String) {
        val frame = MessageFrameImpl
                .Builder()
                .addCommand(Command.ACK)
                .addHeader("id", messageId)
                .build()
        socket.send(frame)
    }

    override fun noneAcknowledged(messageId: String) {
        val frame = MessageFrameImpl
                .Builder()
                .addCommand(Command.NACK)
                .addHeader("id", messageId)
                .build()
        socket.send(frame)
    }

    override fun disconnect(): String {
        val id = getNextReceiptId()
        val frame = MessageFrameImpl
                .Builder()
                .addCommand(Command.BEGIN)
                .addHeader("receipt", id)
                .build()
        socket.send(frame)

        return id
    }


    override fun getState() : SessionState? {
        return sessionState
    }

    private fun setSessionState(state: SessionState) {
        sessionState = state
        sessionListener?.onChange(state)
    }

    private fun getNextSubscriptionId(): String {
        val id = subscriptionsCount

        subscriptionsCount += 1
        return "${UUID.randomUUID()}-${id}"
    }

    private fun getNextTransactionId(): String {
        val id = transactionsCount

        transactionsCount += 1
        return "${UUID.randomUUID()}-${id}"
    }

    private fun getNextReceiptId(): String {
        val id = receiptCount

        receiptCount += 1
        return "${UUID.randomUUID()}-${id}"
    }
}