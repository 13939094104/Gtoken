package vdsMain.peer;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class UserThread extends Thread {

    //f12777a
    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    /* access modifiers changed from: protected */
    //mo43708a
    public abstract void threadStartEvent();

    /* access modifiers changed from: protected */
    //mo43709b
    public abstract void threadEndEvent();

    public synchronized void start() {
        if (!this.atomicBoolean.get()) {
            this.atomicBoolean.set(true);
            super.start();
        }
    }

    public final void run() {
        if (this.atomicBoolean.get()) {
            threadStartEvent();
            this.atomicBoolean.set(false);
            threadEndEvent();
        }
    }

    //mo43710c
    public synchronized void setAtomicBooleanFalse() {
        this.atomicBoolean.set(false);
    }

    //mo43711d
    public boolean getAtomicBoolean() {
        return this.atomicBoolean.get();
    }
}
