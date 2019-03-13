import java.util.List;
import java.util.Optional;

public interface API {
    public Optional<List<String>> haveIBeenPwned(String passwordHash);
}
