import javax.swing.text.html.Option;
import java.util.Optional;

public abstract class Handler {
    public Optional<Response> HandleRequest(Request req){
        return Optional.empty();
    }
}
