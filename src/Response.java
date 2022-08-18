import java.nio.charset.StandardCharsets;

public class Response extends GeneralHeader {
    public int statusCode;
    public String statusResponse;
    public String contentType;
    public String content;

    @Override
    public String toString() {
        // TODO: implement
        return "test response";
    }

    public byte[] toBytes(){
        return toString().getBytes(StandardCharsets.UTF_8);
    }
}
