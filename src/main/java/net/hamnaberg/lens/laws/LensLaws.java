package net.hamnaberg.lens.laws;

import net.hamnaberg.lens.Lens;

import java.util.function.Function;

public class LensLaws<S, A> {
    public final Lens<S, A> lens;

    public LensLaws(Lens<S, A> lens) {
        this.lens = lens;
    }

    public boolean getSet(S s) {
        return lens.set(s, lens.get(s)).equals(s);
    }

    public boolean setGet(S s, A a) {
        return lens.get(lens.set(s, a)).equals(a);
    }

    public boolean setIdempotent(S s, A a) {
        return lens.set(lens.set(s, a), a).equals(lens.set(s, a));
    }

    public boolean modifyIdentity(S s) {
        return lens.modify(s, Function.identity()).equals(s);
    }

    public boolean composeModify(S s, Function<A, A> f,  Function<A, A> g) {
        return lens.modify(lens.modify(s, f), g).equals(lens.modify(s, g.compose(f)));
    }

    public boolean consistentSetModify(S s, A a) {
        return lens.set(s, a).equals(lens.modify(s, ignore -> a));
    }

    public boolean satisfyAll(S s, A a) {
        return getSet(s) &&
                setGet(s, a) &&
                setIdempotent(s, a) &&
                modifyIdentity(s) &&
                composeModify(s, Function.identity(), Function.identity()) &&
                consistentSetModify(s, a);
    }
}
