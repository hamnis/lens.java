package net.hamnaberg.lens;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.control.Option;
import javaslang.control.Try;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class LensOps {

    private LensOps() {
    }

    public static <A> Lens<List<A>, Option<A>> indexed(int i) {
        return Lens.of(
                list -> Try.of(() -> list.apply(i)).toOption(),
                (opt, list) -> opt.map(v -> list.insert(i, v)).getOrElse(list)
        );
    }

    public static <K, V> Lens<Map<K, V>, Option<V>> at(K k) {
        return at(k, HashMap::new);
    }

    public static <K, V> Lens<Map<K, V>, Option<V>> at(K k, Supplier<Map<K, V>> newMap) {
        return Lens.of(
                map -> Option.of(map.get(k)),
                (opt, map) -> {
                    Map<K, V> copy = newMap.get();
                    copy.putAll(map);
                    if (opt.isDefined()) {
                        copy.put(k, opt.get());
                    } else {
                        copy.remove(k);
                    }
                    return copy;
                }
        );
    }

    public <A, B> Lens<Tuple2<A, B>, A> first() {
        return Lens.of(Tuple2::_1, (a, t) -> Tuple.of(a, t._2));
    }

    public <A, B> Lens<Tuple2<A, B>, B> second() {
        return Lens.of(Tuple2::_2, (b, t) -> Tuple.of(t._1, b));
    }
}
