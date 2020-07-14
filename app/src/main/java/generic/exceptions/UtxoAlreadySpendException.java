package generic.exceptions;

public class UtxoAlreadySpendException extends InvalidateUtxoException {
    public UtxoAlreadySpendException() {
    }

    public UtxoAlreadySpendException(String str) {
        super(str);
    }
}
