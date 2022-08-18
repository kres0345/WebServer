import java.io.IOException;

public class Main {
    static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        WebServer server = new WebServer(new DebugSimpleHandler());
        System.out.println("Starting webserver on port " + PORT);
        server.start(PORT);
    }
}