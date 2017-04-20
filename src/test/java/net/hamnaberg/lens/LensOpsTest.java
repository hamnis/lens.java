package net.hamnaberg.lens;


import javaslang.control.Option;
import net.hamnaberg.lens.laws.LensLaws;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class LensOpsTest {

    @Test
    public void mapTest() {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);

        Lens<Map<String, Integer>, Option<Integer>> oneLens = LensOps.at("1");
        oneLens.get(map).forEach(i -> assertEquals(Integer.valueOf(1), i));
        Map<String, Integer> newMap = oneLens.set(map, Option.of(23));

        assertNotSame(map, newMap);
        assertNotEquals(map, newMap);
        oneLens.get(newMap).forEach(i -> assertEquals(Integer.valueOf(23), i));
        oneLens.get(map).forEach(i -> assertEquals(Integer.valueOf(1), i));

        assertTrue(new LensLaws<>(oneLens).satisfyAll(map, Option.of(100)));
        assertTrue(new LensLaws<>(oneLens).satisfyAll(map, Option.none()));
    }

    @Test
    public void linkedHashMap() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("1", 1);
        map.put("2daf", 2);
        map.put("3aaawrefvs", 3);
        map.put("sdfkldsf", 4);


        assertEquals(new LinkedHashSet<>(Arrays.asList("1", "2daf", "3aaawrefvs", "sdfkldsf")), map.keySet());

        Lens<Map<String, Integer>, Option<Integer>> oneLens = LensOps.at("1");
        Map<String, Integer> newMap = oneLens.set(map, Option.of(23));

        assertNotEquals(Arrays.asList("1", "2daf", "3aaawrefvs", "sdfkldsf"), new ArrayList<>(newMap.keySet()));
        Lens<Map<String, Integer>, Option<Integer>> newLens = LensOps.at("1", LinkedHashMap::new);
        Map<String, Integer> newMap2 = newLens.set(map, Option.of(23));

        assertEquals(new LinkedHashSet<>(Arrays.asList("1", "2daf", "3aaawrefvs", "sdfkldsf")), newMap2.keySet());
    }
}
