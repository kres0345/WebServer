import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class DebugSimpleHandler implements Handler {
    @Override
    public Optional<Response> handleRequest(Request req) {
        switch (req.method){
            case "get":
                Response res = new Response();
                res.date = LocalDateTime.now();
                res.connectionStatus = req.connectionStatus;
                res.contentType = "text/html; charset=UTF-8";

                if (Objects.equals(req.requestUri.toString(), "/")){
                    res.statusCode = 420;
                    res.statusResponse = "Enhance your calm";
                    res.content = "<!DOCTYPE html><span>'Hello world' fra det sprog der ikke må benævnes.</span>";
                }else{
                    res.statusCode = 404;
                    res.statusResponse = "Not found";
                    res.content = "<!DOCTYPE html><title>404</title><span>Siden blev ikke fundet.</span>";
                }

                return Optional.of(res);
            case "put":
                break;
        }

        return Optional.empty();
    }
}
