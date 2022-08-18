import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;

public class Request extends GeneralHeader {
    public static final Byte[] TERMINATOR = new Byte[] { 13, 10, 13, 10 };
    public String method;
    public URI requestUri;
    public String userAgent;
    public String host;
    public InetAddress originator;
    public int contentLength;

    public Request(String raw, InetAddress origin){
        connectionStatus = "close"; // closes request by default.
        originator = origin;
        ParseHeader(raw);
    }

    public void ParseHeader(String rawHeader){
        String[] lines = rawHeader.split("\r\n");

        for (String line: lines) {
            // Check whether this is the request-line.
            if (line.toLowerCase().endsWith("http/1.1")){
                String[] words = line.split(" ");

                method = words[0].toLowerCase();
                requestUri = URI.create(words[1]);
                continue;
            }

            String[] keyValuePair = line.split(":", 2);

            if (keyValuePair.length == 1){
                continue;
            }

            switch (keyValuePair[0].toLowerCase()) {
                case "user-agent" -> userAgent = keyValuePair[1];
                case "host" -> host = keyValuePair[1];
                case "content-length" -> contentLength = Integer.parseInt(keyValuePair[1]);
            }
        }
    }
}
