import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class DebugSimpleHandler implements Handler {
    @Override
    public Optional<Response> handleRequest(Request req) {
        switch (req.method){
            case "get":
                final String contentType = "text/html; charset=UTF-8";

                if (Objects.equals(req.requestUri.toString(), "/")){
                    return Optional.of(new Response(req,
                            420,
                            "Enhance your calm",
                            contentType,
                            "<!DOCTYPE html><span>'Hello world' fra det sprog der ikke må benævnes.</span>"));
                }

                // 404
                return Optional.of(new Response(req,
                            404,
                            "Not found",
                            contentType,
                            "<!DOCTYPE html><title>404</title><span>Siden blev ikke fundet.</span>"));
            case "put":
                break;
        }

        return Optional.empty();
    }
}
