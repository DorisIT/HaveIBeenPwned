import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HaveIBeenPwnedAPI implements API {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    private final String baseURL = "https://api.pwnedpasswords.com/range/";

    public Optional<List<String>> haveIBeenPwned(String passwordHash) {
        // We only need to send the first 5 characters of the hashed password to HaveIBeenPwned's API
        final int PASSWORDCHARACTERLIMIT = 5;
        // 3
        var shortPasswordHash = passwordHash.substring(0, PASSWORDCHARACTERLIMIT);
        var apiURL = baseURL.concat(shortPasswordHash);

        // Make request to API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL))
                .build();

        try {
            var respone = httpClient.send(request, HttpResponse.BodyHandlers.ofLines());
            return Optional.of(
                    respone.body()
                            .map(shortPasswordHash::concat)
                            .collect(Collectors.toList()));

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
