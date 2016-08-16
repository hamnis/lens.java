package net.hamnaberg.lens;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class LensOps {

    public static <K, V> Lens<Map<K, V>, Option<V>> at(K k) {
        return at(k, HashMap::new);
    }

    public static <K, V> Lens<Map<K, V>, Option<V>> at(K k, Supplier<Map<K, V>> newMap) {
        return new Lens<>(
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
        return new Lens<>(Tuple2::_1, (a, t) -> Tuple.of(a, t._2));
    }

    public <A, B> Lens<Tuple2<A, B>, B> second() {
        return new Lens<>(Tuple2::_2, (b, t) -> Tuple.of(t._1, b));
    }
}
