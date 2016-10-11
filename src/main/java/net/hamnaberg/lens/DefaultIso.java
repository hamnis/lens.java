package net.hamnaberg.lens;

import java.util.function.Function;

final class DefaultIso<S, A> implements Iso<S, A> {
    private final Function<S, A> get;
    private final Function<A, S> reverseGet;

    DefaultIso(Function<S, A> get, Function<A, S> reverseGet) {
        this.get = get;
        this.reverseGet = reverseGet;
    }

    @Override
    public A get(S s) {
        return this.get.apply(s);
    }

    @Override
    public S reverseGet(A a) {
        return this.reverseGet.apply(a);
    }

}
