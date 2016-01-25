package org.codehaus.groovy.util;

import java.util.*;
import java.util.concurrent.TimeUnit;
import org.junit.*;

import static org.junit.Assert.*;

public class ManagedConcurrentMapTest {

    // Make sure this count crosses the Threshold
    static final int ENTRY_COUNT = 10371;

    ReferenceBundle bundle = ReferenceBundle.getWeakBundle();
    ManagedConcurrentMap<Object, String> map;

    @Before
    public void setup() {
        map = new ManagedConcurrentMap<Object, String>(bundle);
    }

    @Test
    public void testMap() throws InterruptedException {
        // Keep a hardref so we can test get later
        List<Object> keyList = new ArrayList<Object>(ENTRY_COUNT);

        for (int i = 0; i < ENTRY_COUNT; i++) {
            Object key = new Object();
            keyList.add(key);
            map.put(key, "value" + i);
        }
        assertEquals(ENTRY_COUNT, map.fullSize());

        // Make sure we still have our entries, sample a few
        Object key77 = keyList.get(77);
        String value77 = map.get(key77);
        assertEquals("value77", value77);

        Object key1337 = keyList.get(1337);
        String value1337 = map.get(key1337);
        assertEquals("value1337", value1337);

        // Clear hardrefs and gc()
        key77 = null;
        key1337 = null;
        keyList.clear();
        keyList = null;
        for (int i = 0; i < 10; i++) {
            System.gc();
            TimeUnit.MILLISECONDS.sleep(5L);
        }

        // Add an entries to force ReferenceManager.removeStaleEntries
        map.put(new Object(), "last");

        assertEquals("Map removed weak entries", 1, map.fullSize());
    }
}
