import java.util.Optional;

public interface Handler {
    Optional<Response> handleRequest(Request req);
}
