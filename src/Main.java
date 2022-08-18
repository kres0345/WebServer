import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@SuppressWarnings("InfiniteLoopStatement")
public class Main {
    static final int PORT = 8080;
    static final Byte[] TERMINATOR = new Byte[] { 13, 10, 13, 10 };
    //static final Byte[] TERMINATOR = new Byte[] { 'a', 'b', 'c', 'd' };

    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");

        ServerSocket listener = new ServerSocket(PORT);

        while (true){
            try {
                Socket connectedClient = listener.accept();
                clientConnected(connectedClient);
            }catch (Throwable ex){
                System.err.println("Exception receiving client connection: " + ex.getMessage());
            }
        }
    }

    static void clientConnected(Socket client) throws IOException {
        System.out.println("Client connected");
        DataInputStream stream = new DataInputStream(new BufferedInputStream(client.getInputStream()));

        List<Byte> data = new ArrayList<>();

        while(client.isConnected()){

            int endCharsIndex = 0;

            // retrieve all data until termination sequence is received.
            do {
                byte newByte = stream.readByte();
                data.add(newByte);
                System.out.println(newByte);

                if (newByte == TERMINATOR[endCharsIndex]) {
                    endCharsIndex++;
                } else {
                    endCharsIndex = 0;
                }

            } while (endCharsIndex != 4);

            byte[] dataArray = new byte[data.size()];
            for (int i = 0; i < data.size(); i++) {
                dataArray[i] = data.get(i);
            }

            String requestString = new String(dataArray, StandardCharsets.UTF_8);
            System.out.println(requestString);

            Request request = new Request(requestString, client.getInetAddress());
            if (request.contentLength > 0){
                byte[] requestbody = new byte[request.contentLength];
                stream.read(requestbody, 0, request.contentLength);

                request.body = requestbody;
            }

            System.out.println("Recieved request from " + request.originator + "\n -- " + request.requestUri);
        }

    }
}