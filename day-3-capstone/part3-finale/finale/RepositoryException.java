package finale;

/**
 * GIVEN. What a repository throws when the storage underneath it fails.
 *
 * SQLException is checked and full of vendor detail, and nothing above the
 * repository can do anything useful with it. So the repository translates: unchecked
 * type, readable message, and the original exception kept as the cause. Keeping the
 * cause is the whole deal. An exception that throws its cause away turns a
 * five-minute bug into an afternoon.
 */
public class RepositoryException extends RuntimeException {

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
