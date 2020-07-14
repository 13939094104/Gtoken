package generic.exceptions;

public class SignatureFailedException extends Exception {
    public SignatureFailedException() {
    }

    public SignatureFailedException(String str) {
        super(str);
    }
}
