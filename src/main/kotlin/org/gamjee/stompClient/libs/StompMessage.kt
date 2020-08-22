package org.gamjee.stompClient.libs

data class StompMessage(
        val command: Command,
        val headers: List<StompHeader>,
        val body: String?
) {


}

