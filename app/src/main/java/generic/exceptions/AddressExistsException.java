package generic.exceptions;

public class AddressExistsException extends Exception {
    public AddressExistsException() {
    }

    public AddressExistsException(String str) {
        super(str);
    }
}
