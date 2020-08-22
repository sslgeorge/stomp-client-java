package org.gamjee.stompClient.libs

enum class Command(val value: String) {
    ABORT("ABORT"),
    ACK("ACK"),
    BEGIN("BEGIN"),
    COMMIT("COMMIT"),
    CONNECT("CONNECT"),
    CONNECTED("CONNECTED"),
    DISCONNECT("DISCONNECT"),
    ERROR("ERROR"),
    MESSAGE("MESSAGE"),
    NACK("NACK"),
    NONE("NONE"),
    RECEIPT("RECEIPT"),
    SEND("SEND"),
    SUBSCRIBE("SUBSCRIBE"),
    UNSUBSCRIBE("UNSUBSCRIBE"),
}