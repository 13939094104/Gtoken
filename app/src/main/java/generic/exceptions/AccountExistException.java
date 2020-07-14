package generic.exceptions;

public class AccountExistException extends Exception {
    public AccountExistException() {
    }

    public AccountExistException(String str) {
        super(str);
    }
}
