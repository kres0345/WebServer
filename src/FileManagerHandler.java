import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public class FileManagerHandler implements Handler {
    @Override
    public Optional<Response> handleRequest(Request req) {
        switch (req.method){
            case "get":
                // TODO: implement builder pattern instead of passing request.
                if (req.requestUri.toString().equals("/")){
                    req.requestUri = URI.create("/index.html");
                }

                Response res = null;
                try {
                    res = QueryPath(req.requestUri, req);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (res == null){
                    return Optional.empty();
                }

                return Optional.of(res);
        }

        return Optional.empty();
    }

    private Response QueryPath(URI fileUri, Request req) throws IOException {

        Path filePath = Path.of(".", fileUri.getPath());
        File queriedFile = new File(filePath.toAbsolutePath().toUri().getPath());

        if (queriedFile.isDirectory()){
            return GenerateDirectoryListing(filePath, queriedFile, req);
        }else if (queriedFile.exists()){
            return GenerateFileResponse(filePath, req);
        }

        // TODO: Returns a file (text/html), or a directory listing.
        return new Response(req,
                404,
                "Not found",
                "text/html; charset=UTF-8",
                "<!DOCTYPE html><title>404</title><span>Siden blev ikke fundet.</span>");
    }

    private Response GenerateFileResponse(Path filePath, Request req) throws IOException {
        String contentType = Files.probeContentType(filePath);
        String fileContent = Files.readString(filePath);

        return new Response(req,
                200,
                "OK",
                contentType,
                fileContent);
    }

    private Response GenerateDirectoryListing(Path filePath, File directory, Request req){
        // TODO: Presents the current directory in a list as html.
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<body>");
        sb.append("<ul>");
        for (String pathname: Objects.requireNonNull(directory.list())) {
            sb.append("<li>");
            sb.append("<a href='");
            sb.append(filePath).append('/').append(pathname);
            sb.append("'>");
            sb.append(pathname);
            sb.append("</a>");
            sb.append("</li>");
        }
        sb.append("</ul>");
        sb.append("</body>");
        sb.append("</html>");

        return new Response(req,
                200,
                "OK",
                "text/html",
                sb.toString());
    }
}
