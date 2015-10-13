package com.myapp.calculator;

/**
 * Android Calculator App
 */

// Used for accumulating thunks on the heap rather than on the stack.
public class AccumThunk<L, R> {
    private final Function2<L, R, L> func;
    private volatile ThunkGetter thunkGetter;

    public AccumThunk(Function2<L, R, L> func, Thunk<L> init, Node<R> rest) {
        this.func = func;
        thunkGetter = new ThunkGetter(init, rest);
    }
    private class ThunkGetter {
        private final Thunk<L> init;
        private final Node<R> rest;

        ThunkGetter(Thunk<L> init, Node<R> rest) {
            this.init = init;
            this.rest = rest;
        }

        void next() {
            Thunk<L> res = new ConstThunk<>(func.apply(init.get(), rest.getHead().get()));
            thunkGetter = new ThunkGetter(res, rest.getTail());
        }

        L get() {
            return init.get();
        }

        boolean isComputed() {
            return rest == null;
        }
    }

    public L get() {
        while(!thunkGetter.isComputed()) {
            thunkGetter.next();
        }
        return thunkGetter.get();
    }
}
