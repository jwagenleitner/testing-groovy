package org.codehaus.groovy.util;

import java.util.*;
import java.util.concurrent.TimeUnit;
import org.junit.*;

import static org.junit.Assert.*;

public class ManagedConcurrentValueMapTest {

    // Make sure this count crosses the Threshold
    static final int ENTRY_COUNT = 10371;

    ReferenceBundle bundle = ReferenceBundle.getWeakBundle();
    ManagedConcurrentValueMap<String, Object> map;

    @Before
    public void setup() {
        map = new ManagedConcurrentValueMap<String, Object>(bundle);
    }

    @Test
    public void testMap() throws InterruptedException {
        // Keep a hardref so we can test get later
        List<Object> valueList = new ArrayList<Object>(ENTRY_COUNT);

        for (int i = 0; i < ENTRY_COUNT; i++) {
            Object value = new Object();
            valueList.add(value);
            map.put("key" + i, value);
        }

        // Make sure we still have our entries, sample a few
        Object value77 = map.get("key77");
        assertEquals(valueList.get(77), value77);

        Object value1337 = map.get("key1337");
        assertEquals(valueList.get(1337), value1337);

        // Clear hardrefs and gc()
        value77 = null;
        value1337 = null;
        valueList.clear();
        valueList = null;
        for (int i = 0; i < 10; i++) {
            System.gc();
            TimeUnit.MILLISECONDS.sleep(5L);
        }

        // Add an entries to force ReferenceManager.removeStaleEntries
        map.put("keyLast", new Object());

        // No size() method, so let's just check a few keys we that should have been collected
        assertEquals(null, map.get("key77"));
        assertEquals(null, map.get("key1337"));
        assertEquals(null, map.get("key3559"));
    }

}
