package net.hamnaberg.lens;

import javaslang.control.Option;

import java.util.function.Function;

public final class Iso<S, A> {
    private final Function<S, A> get;
    private final Function<A, S> reverseGet;

    public Iso(Function<S, A> get, Function<A, S> reverseGet) {
        this.get = get;
        this.reverseGet = reverseGet;
    }

    public A get(S s) {
        return this.get.apply(s);
    }

    public S reverseGet(A a) {
        return this.reverseGet.apply(a);
    }

    public Function<S, S> modify(Function<A, A> f) {
        return a -> reverseGet(f.apply(get(a)));
    }

    public Iso<A, S> reverse() {
        return new Iso<>(this.reverseGet, this.get);
    }

    public <B> Iso<S, B> compose(Iso<A, B> iso) {
        return new Iso<>(
                this.get.andThen(iso.get),
                iso.reverseGet.andThen(this.reverseGet)
        );
    }

    public <B> Prism<S, B> composePrism(Prism<A, B> prism) {
        return toPrism().compose(prism);
    }

    public <B> Lens<S, B> composeLens(Lens<A, B> lens) {
        return toLens().compose(lens);
    }

    public Prism<S, A> toPrism() {
        return new Prism<>(s -> Option.some(get(s)), reverseGet);
    }

    public Lens<S, A> toLens() {
        return new Lens<>(get, (a, ignore) -> reverseGet(a));
    }

    public static <S> Iso<S, S> identity() {
        return new Iso<>(s -> s, s -> s);
    }
}
