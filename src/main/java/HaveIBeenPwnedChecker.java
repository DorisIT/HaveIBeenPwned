import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HaveIBeenPwnedChecker {
    private static final API api = new HaveIBeenPwnedAPI();

    public static List<String> haveIBeenPwned(List<String> passwords) {

        var hashedPasswords = hashPasswords(passwords);
        var pwnedPasswords = pwnedPasswords(hashedPasswords);

        // Have I been pwned?
        return hashedPasswords.stream()
                .map(pwnedPasswords::get)
                .flatMap(Optional::stream)
                .flatMap(List::stream)
                // 5.
                .filter(pwnedPassword -> hashedPasswords.contains(pwnedPassword.substring(0, pwnedPassword.indexOf(":"))))
                .collect(Collectors.toList());
    }

    private static Map<String, Optional<List<String>>> pwnedPasswords(List<String> hashedPasswords) {
        // 4
        return hashedPasswords.stream()
                .collect(Collectors.toMap(Object::toString,
                                          api::haveIBeenPwned));
    }

    private static List<String> hashPasswords(List<String> passwords) {
        // 2
        return passwords.stream()
                .map(DigestUtils::sha1Hex)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }
}
