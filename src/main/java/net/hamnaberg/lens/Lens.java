package net.hamnaberg.lens;

import javaslang.collection.List;
import javaslang.control.Option;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Lens<S, A> {
    static <S,A> Lens<S, A> of(Function<S, A> get, BiFunction<S, A, S> set) {
        return new DefaultLens<>(get, set);
    }

    A get(S s);

    S set(S s, A a);

    default S modify(S s, Function<A, A> f) {
        return set(s, f.apply(get(s)));
    }

    default Option<S> modifyOption(S s, Function<A, Option<A>> f) {
        return f.apply(get(s)).map(a -> set(s, a));
    }

    default List<S> modifyList(S s, Function<A, List<A>> f) {
        return f.apply(get(s)).map(a -> set(s, a));
    }

    default <B> Lens<S, B> compose(Lens<A, B> lens) {
        Function<S, A> get = this::get;
        return of(
                get.andThen(lens::get),
                (s, b) -> set(s, lens.set(get(s), b))
        );
    }
}
