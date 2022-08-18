import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Response extends GeneralHeader {
    public int statusCode;
    public String statusResponse;
    public String contentType;
    public String content;

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
        return toString().getBytes(StandardCharsets.UTF_8);
    }
}
