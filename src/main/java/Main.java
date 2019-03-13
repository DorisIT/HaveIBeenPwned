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
        final int passwordCharacterLimit = 5;
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

        var hashedShortenedPasswords = hashedPasswords.stream()
                // 3
                .map(password -> password.substring(0, passwordCharacterLimit))
                .collect(Collectors.toList());

        var pwnedPasswords = hashedShortenedPasswords.stream()
                // 4
                .map(Main::haveIBeenPwned)
                .flatMap(Optional::stream)
                .flatMap(List::stream)
                .flatMap(pwned -> hashedShortenedPasswords.stream()
                        .map(hashed -> hashed.concat(pwned))
                )
                .collect(Collectors.toList());


        // Have I been pwned?
        pwnedPasswords.stream()
                // 5.
                .filter(pwnedPassword -> hashedPasswords.contains(pwnedPassword.substring(0, pwnedPassword.indexOf(":"))))
                // 6.
                .forEach(System.out::println);


    }

    public static Optional<List<String>> haveIBeenPwned(String passwordHash) {
        final String baseURL = "https://api.pwnedpasswords.com/range/";
        var apiURL = baseURL.concat(passwordHash);
        // Make request to API with given string/password

        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(apiURL))
                                .build();
        try {
            var respone = httpClient.send(request, HttpResponse.BodyHandlers.ofLines());

            return Optional.of(respone.body()
                    .collect(Collectors.toList()));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
