import org.apache.commons.codec.digest.DigestUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HaveIBeenPwnedChecker {
    private static final API api = new HaveIBeenPwnedAPI();

    public static List<String> haveIBeenPwnedPretty(List<String> passwords) {

        Map<String, String> hashedPasswords = passwords.stream()
                                                .collect(Collectors.toMap(HaveIBeenPwnedChecker::hashPassword,
                                                        Object::toString));

        List<String> pwnedPasswords = haveIBeenPwned(passwords);

        // Have I been pwned?
        return pwnedPasswords.stream()
                .filter(pwnedPassword -> isPwned(hashedPasswords.keySet(), pwnedPassword))
                .map(pwnedPassword -> prettify(hashedPasswords.get(pwnedToHash(pwnedPassword)), pwnedPassword))
                .collect(Collectors.toList());
    }


    public static List<String> haveIBeenPwned(List<String> passwords) {

        var hashedPasswords = hashPasswords(passwords);
        var pwnedPasswords = pwnedPasswords(hashedPasswords);

        // Have I been pwned?
        return hashedPasswords.stream()
                .map(pwnedPasswords::get)
                .flatMap(Optional::stream)
                .flatMap(List::stream)
                // 5.
                .filter(pwnedPassword -> isPwned(hashedPasswords, pwnedPassword))
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
                .map(HaveIBeenPwnedChecker::hashPassword)
                .collect(Collectors.toList());
    }

    private static String hashPassword(String password) {
        return DigestUtils.sha1Hex(password).toUpperCase();
    }

    /* Helper methods */

    private static String prettify(String password, String pwnedPassword) {
        String timesPwned = pwnedPassword.split(":")[1];
        return password
                // Since delimiter is not included when splitting ...
                .concat(":")
                .concat(timesPwned);
    }

    private static boolean isPwned(Collection<String> hashedPasswords, String pwnedPassword) {
        return hashedPasswords.contains(pwnedToHash(pwnedPassword));
    }

    private static String pwnedToHash(String pwnedPaswword) {
        return pwnedPaswword.substring(0, pwnedPaswword.indexOf(":"));
    }


}
