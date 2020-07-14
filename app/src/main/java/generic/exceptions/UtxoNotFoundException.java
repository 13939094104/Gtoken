package generic.exceptions;

public class UtxoNotFoundException extends InvalidateUtxoException {

    /* renamed from: a */
    String f690a = null;

    public UtxoNotFoundException() {
    }

    public UtxoNotFoundException(String str) {
        super(str);
    }

    /* renamed from: a */
    public void mo18969a(String str) {
        this.f690a = str;
    }
}
