import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WebServer {
    private final Handler handler;

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
                ex.printStackTrace();
            }
        }
    }

    private void clientConnected(Socket client) throws IOException {
        System.out.println("Client connected");
        DataInputStream streamReader = new DataInputStream(new BufferedInputStream(client.getInputStream()));
        DataOutputStream streamWriter = new DataOutputStream(client.getOutputStream());

        while(!client.isClosed() && client.isConnected()){
            boolean disconnected = false;

            // retrieve all data until termination sequence is received.
            int endCharsIndex = 0;
            List<Byte> receivedData = new ArrayList<>();
            do {
                byte newByte;
                try{
                    newByte = streamReader.readByte();
                }catch (EOFException ex){
                    break;
                }catch (IOException ex){
                    disconnected = true;
                    break;
                }
                receivedData.add(newByte);

                if (newByte == Request.TERMINATOR[endCharsIndex]) {
                    endCharsIndex++;
                } else {
                    endCharsIndex = 0;
                }

            } while (endCharsIndex != 4);

            // Get header string and display to console.
            byte[] dataArray = new byte[receivedData.size()];
            for (int i = 0; i < receivedData.size(); i++) {
                dataArray[i] = receivedData.get(i);
            }

            String requestHeader = new String(dataArray, StandardCharsets.UTF_8);
            System.out.println(requestHeader);

            // Interpret request, and retrieve request content.
            Request request = new Request(requestHeader, client.getInetAddress());
            if (request.contentLength > 0){
                byte[] body = new byte[request.contentLength];
                streamReader.read(body, 0, request.contentLength);

                request.body = body;
            }

            System.out.println("Received request from " + request.originator + "\n -- " + request.requestUri);

            // No response
            Optional<Response> response = handler.handleRequest(request);

            // No response to send.
            if (response.isEmpty()){
                continue;
            }

            // Don't send response, if disconnected.
            if (disconnected){
                break;
            }

            // Transmit response message
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
