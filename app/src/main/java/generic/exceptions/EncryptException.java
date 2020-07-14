package generic.exceptions;

public class EncryptException extends Exception {
    public EncryptException() {
    }

    public EncryptException(String str) {
        super(str);
    }

    public EncryptException(Throwable th) {
        super(th);
    }
}
