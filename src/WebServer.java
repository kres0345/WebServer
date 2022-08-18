import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WebServer {
    private Handler handler;

    public WebServer(Handler requestHandler) {
        handler = requestHandler;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void start(int port) throws IOException {
        ServerSocket listener = new ServerSocket(port);

        while (true){
            try {
                Socket client = listener.accept();
                clientConnected(client);
            }catch (Throwable ex){
                System.err.println("Exception receiving client connection: " + ex.getMessage());
            }
        }
    }

    private void clientConnected(Socket client) throws IOException {
        System.out.println("Client connected");
        DataInputStream streamReader = new DataInputStream(new BufferedInputStream(client.getInputStream()));
        DataOutputStream streamWriter = new DataOutputStream(client.getOutputStream());
        List<Byte> data = new ArrayList<>();

        while(!client.isClosed() && client.isConnected()){

            int endCharsIndex = 0;

            // retrieve all data until termination sequence is received.
            do {
                byte newByte = streamReader.readByte();
                data.add(newByte);

                if (newByte == Request.TERMINATOR[endCharsIndex]) {
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
                byte[] body = new byte[request.contentLength];
                streamReader.read(body, 0, request.contentLength);

                request.body = body;
            }

            System.out.println("Received request from " + request.originator + "\n -- " + request.requestUri);

            // No response
            Optional<Response> response = handler.handleRequest(request);
            if (response.isEmpty()){
                continue;
            }

            try{
                byte[] responseBytes = response.get().toBytes();
                streamWriter.write(responseBytes, 0, responseBytes.length);
            }catch (IOException ex){
                System.err.println("Couldn't write response to client: " + ex.getMessage());
                client.close();
                return;
            }

            if (Objects.equals(request.connectionStatus, "close")){
                client.close();
            }
        }
    }
}
