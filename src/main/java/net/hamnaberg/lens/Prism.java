package net.hamnaberg.lens;

import javaslang.control.Either;
import javaslang.control.Option;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Prism<S, A> {
    static <S,A> Prism<S, A> of(Function<S, Option<A>> getOption, Function<A, S> reverseGet) {
        return new DefaultPrism<>(getOption, reverseGet);
    }

    S reverseGet(A a);

    Option<A> getOption(S s);

    default Either<S, A> getOrModify(S s) {
        return getOption(s).map(Either::<S, A>right).getOrElse(Either.left(s));
    }

    default boolean isEmpty(S s) {
        return getOption(s).isEmpty();
    }

    default boolean nonEmpty(S s) {
        return getOption(s).isDefined();
    }

    default Option<A> find(S s, Predicate<A> p) {
        return getOption(s).filter(p);
    }

    default boolean exists(S s, Predicate<A> p) {
        return find(s, p).isDefined();
    }

    default S modify(S s, Function<A, A> f) {
        return getOrModify(s).fold(Function.identity(), a -> reverseGet(f.apply(a)));
    }

    default S set(S s, A a) {
        return modify(s, ignore -> a);
    }

    default Option<S> modifyOption(S s, Function<A, A> f) {
        return getOption(s).map(a -> reverseGet(f.apply(a)));
    }

    default <B> Prism<S, B> compose(Prism<A, B> prism) {
        Function<B, A> reverseGet = prism::reverseGet;
        return of(
                s -> this.getOption(s).flatMap(prism::getOption),
                reverseGet.andThen(this::reverseGet)
        );
    }

    default <B> Prism<S, B> composeIso(Iso<A, B> iso) {
        return compose(iso.toPrism());
    }
}
