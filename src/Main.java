import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("InfiniteLoopStatement")
public class Main {
    static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        WebServer server = new WebServer(new DebugSimpleHandler());
        System.out.println("Starting webserver on port " + PORT);
        server.start(PORT);
    }
}