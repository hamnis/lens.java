package net.hamnaberg.lens;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Lens<S, A> {
    static <S,A> Lens<S, A> of(Function<S, A> get, BiFunction<A, S, S> set) {
        return new DefaultLens<>(get, set);
    }

    A get(S s);

    S set(A a, S s);

    default <B> Lens<S, B> compose(Lens<A, B> lens) {
        Function<S, A> get = this::get;
        return of(
                get.andThen(lens::get),
                (b, s) -> set(lens.set(b, get(s)), s)
        );
    }
}
