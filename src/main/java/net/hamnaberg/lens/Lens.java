package net.hamnaberg.lens;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class Lens<S, A> {
    private final Function<S, A> get;
    private final BiFunction<A, S, S> set;

    public Lens(Function<S, A> get, BiFunction<A, S, S> set) {
        this.get = get;
        this.set = set;
    }

    public A get(S s) {
        return get.apply(s);
    }

    public S set(A a, S s) {
        return set.apply(a, s);
    }

    public <B> Lens<S, B> compose(Lens<A, B> lens) {
        return new Lens<>(
                get.andThen(lens.get),
                (b, s) -> set(lens.set(b, get(s)), s)
        );
    }
}
