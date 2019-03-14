import org.apache.commons.codec.digest.DigestUtils;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final API api = new HaveIBeenPwnedAPI();

    public static void main(String[] args) throws IOException {
        // 1. Read password from file
        // 2. Hash passwords with SHA-1
        // 3. Take only the first 5 hexadecimal characters of each password -> API implementation detail
        // 4. For every password, make a request to a certain API
        // 5. After a response, compare every password with response from API
        // 6. If there's a match, print how many times the password has leaked

        // 1
        final String file = args[0];
        List<String> hashedPasswords = readLines(file).stream()
                // 2
                .map(DigestUtils::sha1Hex)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        Map<String, Optional<List<String>>> pwnedPasswords = hashedPasswords.stream()
                // 4
                .collect(Collectors.toMap(Object::toString,
                                            api::haveIBeenPwned));

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

    // Method for auto-closing IO resources
    public static List<String> readLines(String filename) {
        try(Stream<String> lines = Files.lines(Path.of(filename))) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


}