package net.hamnaberg.lens;

import java.util.function.BiFunction;
import java.util.function.Function;

final class DefaultLens<S, A> implements Lens<S, A> {
    private final Function<S, A> get;
    private final BiFunction<A, S, S> set;

    DefaultLens(Function<S, A> get, BiFunction<A, S, S> set) {
        this.get = get;
        this.set = set;
    }

    @Override
    public A get(S s) {
        return get.apply(s);
    }

    @Override
    public S set(A a, S s) {
        return set.apply(a, s);
    }

}
