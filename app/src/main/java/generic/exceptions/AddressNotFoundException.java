package generic.exceptions;

public class AddressNotFoundException extends Exception {
    public AddressNotFoundException() {
    }

    public AddressNotFoundException(String str) {
        super(str);
    }
}
