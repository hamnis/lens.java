package net.hamnaberg.lens;

import javaslang.control.Option;

import java.util.function.Function;

final class DefaultPrism<S, A> implements Prism<S, A> {
    private final Function<A, S> reverseGet;
    private final Function<S, Option<A>> getOption;

    DefaultPrism(Function<S, Option<A>> getOption, Function<A, S> reverseGet) {
        this.reverseGet = reverseGet;
        this.getOption = getOption;
    }

    @Override
    public S reverseGet(A a) {
        return reverseGet.apply(a);
    }

    @Override
    public Option<A> getOption(S s) {
        return getOption.apply(s);
    }

}
