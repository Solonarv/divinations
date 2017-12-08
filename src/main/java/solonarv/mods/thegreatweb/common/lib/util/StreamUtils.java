package solonarv.mods.thegreatweb.common.lib.util;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.stream.Stream;

public class StreamUtils {
    public static <U, V> Stream<Pair<U, V>> product(Collection<U> uCollection, Collection<V> vCollection) {
        return uCollection.stream().flatMap(u -> vCollection.stream().map(v -> Pair.of(u, v)));
    }
}
