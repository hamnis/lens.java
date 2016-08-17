package net.hamnaberg.lens;

import javaslang.control.Either;
import javaslang.control.Option;

import java.util.function.Function;

public final class Prism<S, A> {
    private final Function<A, S> reverseGet;
    private final Function<S, Option<A>> getOption;

    public Prism(Function<S, Option<A>> getOption, Function<A, S> reverseGet) {
        this.reverseGet = reverseGet;
        this.getOption = getOption;
    }

    public S reverseGet(A a) {
        return reverseGet.apply(a);
    }
    public Option<A> getOption(S s) {
        return getOption.apply(s);
    }

    public Either<S, A> getOrModify(S s) {
        return getOption(s).map(Either::<S, A>right).getOrElse(Either.left(s));
    }

    public boolean isMatching(S s) {
        return getOption(s).isDefined();
    }

    public Function<S, S> modify(Function<A, A> f) {
        return s -> getOrModify(s).fold(Function.identity(), a -> reverseGet(f.apply(a)));
    }

    public Function<S, Option<S>> modifyOption(Function<A, A> f) {
        return s -> getOption(s).map(a -> reverseGet(f.apply(a)));
    }

    public <B> Prism<S, B> compose(Prism<A, B> prism) {
        return new Prism<>(
                s -> this.getOption(s).flatMap(prism::getOption),
                prism.reverseGet.andThen(this.reverseGet)
        );
    }

    public <B> Prism<S, B> compose(Iso<A, B> iso) {
        return compose(iso.toPrism());
    }
}
