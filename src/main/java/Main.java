import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        // 1. Read password from file
        // 2. Hash passwords with SHA-1
        // 3. Take only the first 5 hexadecimal characters of each password -> API implementation detail
        // 4. For every password, make a request to a certain API
        // 5. After a response, compare every password with response from API
        // 6. If there's a match, print how many times the password has leaked

        // 1
        Arrays.stream(args)
                .forEach(Main::hasThisFileBeenPwned);
    }

    public static void hasThisFileBeenPwned(String file) {
        System.out.println("Checking passwords from " + file);

        List<String> passwords = readLines(file);

        // 6
        HaveIBeenPwnedChecker.haveIBeenPwned(passwords).stream()
                .forEach(System.out::println);
    }

    // Method for auto-closing IO resources
    public static List<String> readLines(String filename) {
        try (Stream<String> lines = Files.lines(Path.of(filename))) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}