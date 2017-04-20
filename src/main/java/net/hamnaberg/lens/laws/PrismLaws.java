package net.hamnaberg.lens.laws;

import javaslang.control.Option;
import net.hamnaberg.lens.Prism;

import java.util.function.Function;

public class PrismLaws<S, A> {
    public final Prism<S, A> prism;

    public PrismLaws(Prism<S, A> prism) {
        this.prism = prism;
    }

    public boolean partialRoundTripOneWay(S s) {
        return prism.getOrModify(s).fold(Function.identity(), prism::reverseGet).equals(s);
    }

    public boolean roundTripOtherWay(A a) {
        return prism.getOption(prism.reverseGet(a)).equals(Option.of(a));
    }

    public boolean modifyIdentity(S s) {
        return prism.modify(s, Function.identity()).equals(s);
    }

    public boolean composeModify(S s, Function<A, A> f,  Function<A, A> g) {
        return prism.modify(prism.modify(s, f), g).equals(prism.modify(s, g.compose(f)));
    }

    public boolean consistentSetModify(S s, A a) {
        return prism.set(s, a).equals(prism.modify(s, ignore -> a));
    }

    public boolean satisfyAll(S s, A a) {
        return partialRoundTripOneWay(s) &&
                roundTripOtherWay(a) &&
                modifyIdentity(s) &&
                composeModify(s, Function.identity(), Function.identity()) &&
                consistentSetModify(s, a);
    }
}
