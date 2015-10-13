package com.myapp.calculator;

/**
 *  Android Calculator App
 */

// Lazy structure that computes only when required.
// Thunks can be cached to avoid recomputing results.
public abstract class Thunk<T> {
    private abstract class ThunkGetter {
        abstract T get();
        abstract boolean isComputed();
    }

    private volatile ThunkGetter thunkGetter = new ThunkGetter() {
        @Override
        synchronized T get() {
            final T value = compute();
            thunkGetter = new ThunkGetter() {
                @Override
                T get() {
                    return value;
                }

                @Override
                boolean isComputed() {
                    return true;
                }
            };
            return value;
        }

        @Override
        boolean isComputed() {
            return false;
        }
    };
    protected abstract T compute();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Thunk<?> thunk = (Thunk<?>) o;

        return this.get() == thunk.get() || this.get().equals(thunk.get());
    }

    public T get() {
        return thunkGetter.get();
    }

    public boolean isComputed() {
        return thunkGetter.isComputed();
    }
}
