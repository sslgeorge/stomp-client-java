package org.gamjee.stompClient.libs

import org.gamjee.stompClient.interfaces.MessageFrame
import java.awt.Frame
import java.lang.IllegalStateException
import java.lang.StringBuilder


class MessageFrameImpl private constructor(
        var message: StompMessage
): MessageFrame {
   class Builder {
       private var command: Command? = null
       private var contentType: String = "text/plain"
       private var headers = mutableMapOf<String, String>()
       private var body: String?  = null

       fun addCommand(command: Command) : Builder {
           this.command = command
           return this
       }

       fun addContentType(contentType: String): Builder {
           this.contentType = contentType

           return this
       }

       fun addHeaders(headers: MutableMap<String, String>) : Builder {
           this.headers.putAll(headers)
           return this
       }

       fun addHeader(key: String, value: String) : Builder {
           this.headers[key] = value
           return this
       }

       fun addBody(body: String) : Builder {
           this.body = body
           return this
       }


       fun build(): MessageFrame {
           if(command == null) {
               throw IllegalStateException("Command is required: ${Command.values()}",)
           }

           val stompHeaders = headers.map{
               StompHeader(it.key, it.value)
           }

           val message = StompMessage(
                   command!!,
                   stompHeaders,
                   body
           )

           return MessageFrameImpl(message)
       }
   }

    override fun encode(): String {
        return StompMessageCodec().encode(message)
    }
}