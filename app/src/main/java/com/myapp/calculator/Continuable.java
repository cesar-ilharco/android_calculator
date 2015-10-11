package com.myapp.calculator;

/**
 * Android Calculator App
 */

public class Continuable<T> {
    private final Thunk<T> thunk;

    public Continuable(Thunk<T> thunk) {
        this.thunk = thunk;
    }
    public void computeFor(int timeMs) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                thunk.get();
            }
        });

        thread.start();

        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // We know
        thread.stop();
    }
    public T get() {
        if(!thunk.isComputed()) throw new IllegalStateException("Not fully computed");
        return thunk.get();
    };

    public boolean isComputed() {
        return thunk.isComputed();
    }
}
