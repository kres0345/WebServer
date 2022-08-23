import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Response extends HTTPMessage {
    public int statusCode;
    public String statusResponse;
    public String contentType;
    public String content;

    public Response(){}

    public Response(Request req, int statusCode, String statusResponse, String contentType, String content){
        this.statusCode = statusCode;
        this.statusResponse = statusResponse;
        this.contentType = contentType;
        this.content = content;
        super.date = LocalDateTime.now();
        connectionStatus = req.connectionStatus;
    }

    @Override
    public String toString() {
        String dateFormatted = DateTimeFormatter.ofPattern("E, dd MMM yyyy hh:mm:ss", Locale.US).format(date);

        return String.format("HTTP/1.1 %d %s\r\n", statusCode, statusResponse) +
                String.format("Date: %s GMT\r\n", dateFormatted) +
                String.format("Connection: %s\r\n", connectionStatus) +
                String.format("Content-Length: %d\r\n", content.length()) +
                String.format("Content-Type: %s\r\n", contentType) +
                "\r\n" +
                content;
    }

    public byte[] toBytes(){
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(toString());
        return byteBuffer.array();
    }
}
