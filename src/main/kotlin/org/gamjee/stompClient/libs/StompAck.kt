package org.gamjee.stompClient.libs

enum class StompAck(val value: String) {
    AUTO("auto"),
    CLIENT("client"),
    CLIENT_INDIVIDUAL("client-individual"),
}
