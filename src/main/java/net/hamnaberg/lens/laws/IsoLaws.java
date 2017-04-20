package net.hamnaberg.lens.laws;

import net.hamnaberg.lens.Iso;

import java.util.function.Function;

public class IsoLaws<S, A> {
    public Iso<S, A> iso;

    public IsoLaws(Iso<S, A> iso) {
        this.iso = iso;
    }

    public boolean roundTripOneWay(S s) {
        return s.equals(iso.reverseGet(iso.get(s)));
    }

    public boolean roundTripOtherWay(A a) {
        return a.equals(iso.get(iso.reverseGet(a)));
    }

    public boolean modifyIdentity(S s) {
        return modify(Function.identity(), s, s);
    }

    public boolean modify(Function<A, A> f, S value, S expected) {
        return iso.modify(value, f).equals(expected);
    }

    public boolean composeModify(S s, Function<A, A> f,  Function<A, A> g ) {
        return iso.modify(iso.modify(s, f), g).equals(iso.modify(s, g.compose(f)));
    }

    public boolean consistentSetModify(S s, A a) {
        return iso.set(s, a).equals(iso.modify(s, ignore -> a));
    }

    public boolean satisfyAll(S s, A a) {
        return roundTripOneWay(s) &&
                roundTripOtherWay(a) &&
                modifyIdentity(s) &&
                composeModify(s, Function.identity(), Function.identity()) &&
                consistentSetModify(s, a);
    }
}
