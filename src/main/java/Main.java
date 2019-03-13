import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {

    static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    public static void main(String[] args) throws IOException {
        // 1. Read password from file
        // 2. Hash passwords with SHA-1
        // 3. Take only the first 5 hexadecimal characters of each password
        // 4. For every password, make a request to a certain API
        // 5. After a response, compare every password with response from API
        // 6. If there's a match, print how many times the password has leaked

        // 1
        final String file = args[0];
        var hashedPasswords = Files.lines(Path.of(file))
                // 2
                .map(DigestUtils::sha1Hex)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        var pwnedPasswords = hashedPasswords.stream()
                // 4
                .collect(Collectors.toMap(Object::toString,
                                            Main::haveIBeenPwned));

        // Have I been pwned?
        hashedPasswords.stream()
                .map(pwnedPasswords::get)
                .flatMap(Optional::stream)
                .flatMap(List::stream)
                // 5.
                .filter(pwnedPassword -> hashedPasswords.contains(pwnedPassword.substring(0, pwnedPassword.indexOf(":"))))
                // 6.
                .forEach(System.out::println);
    }

    public static Optional<List<String>> haveIBeenPwned(String passwordHash) {
        // We only need to send the first 5 characters of the hashed password to HaveIBeenPwned's API
        final String baseURL = "https://api.pwnedpasswords.com/range/";
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