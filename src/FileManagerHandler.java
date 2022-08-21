import java.net.URI;
import java.util.Optional;

public class FileManagerHandler implements Handler {
    @Override
    public Optional<Response> handleRequest(Request req) {
        switch (req.method){
            case "get":
                Response res = QueryPath(req.requestUri);

                if (res == null){
                    return Optional.empty();
                }

                return Optional.of(res);
        }

        return Optional.empty();
    }

    private Response QueryPath(URI path){
        // TODO: Returns a file (text/html), or a directory listing.
        return null;
    }

    private Response GenerateFileResponse(URI path){
        return null;
    }

    private Response GenerateDirectoryListing(){
        // TODO: Presents the current directory in a list as html.
        return null;
    }
}
