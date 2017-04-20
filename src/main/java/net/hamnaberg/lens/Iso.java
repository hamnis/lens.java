package net.hamnaberg.lens;

import javaslang.control.Option;

import java.util.function.Function;

public interface Iso<S, A> {
    static <S, A> Iso<S, A> of(Function<S, A> get, Function<A, S> reverseGet) {
        return new DefaultIso<>(get, reverseGet);
    }

    static <S> Iso<S, S> identity() {
        return of(s -> s, s -> s);
    }

    A get(S s);

    S reverseGet(A a);

    default S modify(S s, Function<A, A> f) {
        return reverseGet(f.apply(get(s)));
    }

    default S set(S s, A value) {
        return modify(s, ignore -> value);
    }

    default Iso<A, S> reverse() {
        return of(this::reverseGet, this::get);
    }

    default <B> Iso<S, B> compose(Iso<A, B> iso) {
        Function<S, A> getF = this::get;
        Function<B, A> reverseGetF = iso::reverseGet;
        return of(
                getF.andThen(iso::get),
                reverseGetF.andThen(this::reverseGet)
        );
    }

    default <B> Prism<S, B> composePrism(DefaultPrism<A, B> prism) {
        return toPrism().compose(prism);
    }

    default <B> Lens<S, B> composeLens(DefaultLens<A, B> lens) {
        return toLens().compose(lens);
    }

    default Prism<S, A> toPrism() {
        return Prism.of(s -> Option.some(get(s)), this::reverseGet);
    }

    default Lens<S, A> toLens() {
        return Lens.of(this::get, (ignore, a) -> reverseGet(a));
    }
}
