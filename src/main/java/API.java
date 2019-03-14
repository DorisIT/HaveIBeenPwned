import java.util.List;
import java.util.Optional;

public interface API {
    Optional<List<String>> haveIBeenPwned(String passwordHash);
}
