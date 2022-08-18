import java.time.LocalDateTime;
import java.util.Optional;

public class StandardHandler extends Handler {
    @Override
    public Optional<Response> HandleRequest(Request req) {
        switch (req.method){
            case "get":
                Response res = new Response();
                res.statusCode = 420;
                res.statusResponse = "Enhance your calm";
                res.date = LocalDateTime.now();
                res.connectionStatus = req.connectionStatus;
                res.contentType = "text/html";
                res.content = "<span>Hello world from Java</span>";

                return Optional.of(res);
            case "put":
                break;
        }

        return Optional.empty();
    }
}
