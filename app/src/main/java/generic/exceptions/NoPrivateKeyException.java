package generic.exceptions;

public class NoPrivateKeyException extends Exception {
    public NoPrivateKeyException() {
    }

    public NoPrivateKeyException(String str) {
        super(str);
    }
}
