import org.gamjee.stompClient.libs.Command
import org.gamjee.stompClient.libs.StompMessageCodec
import org.junit.Test
import org.junit.Assert.*

class StompMessageCodecTest {
    private val connectionResponse= "CONNECTED\nversion:1.1\nheart-beat:0,0\n\n\u0000"
    private val sendCode = "SEND\ndestination:/app/send.message\ncontent-length:34\n" +
            "content-type:application/json\n\n{\"text\":\"Hello\",\"toUser\":\"George\"}\u0000"

    @Test
    fun decodeCorrectCommand() {
        val stompMessage1 = StompMessageCodec().decode(connectionResponse)
        val stompMessage2 = StompMessageCodec().decode(sendCode)

        val expected1 = Command.CONNECTED.value
        val expected2 = Command.SEND.value
        val actual1 = stompMessage1.command.value
        val actual2 = stompMessage2.command.value

        assertEquals(
                "Command must be the same",
                expected1,
                actual1
        )

        assertEquals(
                "Command must be the same",
                expected2,
                actual2
        )
    }

    @Test
    fun decodeCorrectBody() {
        val stompMessage1 = StompMessageCodec().decode(connectionResponse)
        val stompMessage2 = StompMessageCodec().decode(sendCode)

        assertEquals(
                "Message body is supposed to be null",
                null,
                stompMessage1.body

        )
        assertEquals(
                "Message body is not correct",
                "{\"text\":\"Hello\",\"toUser\":\"George\"}",
                stompMessage2.body
        )

    }

    @Test
    fun decodeCorrectHeaders() {
        val stompMessage1 = StompMessageCodec().decode(connectionResponse)
        val stompMessage2 = StompMessageCodec().decode(sendCode)

        assertEquals(
                "There should be 2 headers",
                2,
                stompMessage1.headers.size

        )
        assertEquals(
                "There should be 3 headers",
                3,
                stompMessage2.headers.size

        )

        assertEquals(
                "Correct version header should be set",
                "1.1",
                stompMessage1.headers[0].value
        )

        assertEquals(
                "Correct content-type header should be set",
                "application/json",
                stompMessage2.headers[2].value
        )
    }

}