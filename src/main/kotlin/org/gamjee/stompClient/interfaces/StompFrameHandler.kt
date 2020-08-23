package org.gamjee.stompClient.interfaces

import org.gamjee.stompClient.libs.StompHeader

interface StompFrameHandler {
    fun getPayloadType(): Class<*> { return String::class.java }
    fun onMessage(payload: Any, headers: Collection<StompHeader>);
}
