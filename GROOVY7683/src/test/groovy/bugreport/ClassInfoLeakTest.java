package bugreport;

import java.lang.ref.*;
import java.util.*;
import groovy.lang.*;
import org.codehaus.groovy.reflection.ClassInfo;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoLeakTest {

    private static final int NUM_OBJECTS = 10;
    private static final int OOM_BUFFER = 128 * 1024 * 1024;

    static {
        System.out.println("Groovy Version: " + GroovySystem.getVersion() + "\n");
    }

    ReferenceQueue<Class> q = new ReferenceQueue<Class>();
    List<PhantomReference<Class>> prefs = new ArrayList<PhantomReference<Class>>(NUM_OBJECTS);

    @Test
    public void testLeak() throws Exception {
        for (int i = 0; i < NUM_OBJECTS; i++) {
            GroovyClassLoader gcl = new GroovyClassLoader();
            Class scriptClass = gcl.parseClass("int myvar = " + i);
            ClassInfo ci = ClassInfo.getClassInfo(scriptClass);
            PhantomReference<Class> ref = new PhantomReference<Class>(scriptClass, q);
            prefs.add(ref);
            assertEquals(ci.getClass().getClassLoader(), this.getClass().getClassLoader());
            assertNotEquals(scriptClass.getClass().getClassLoader(), this.getClass().getClassLoader());
            System.gc();
        }
        System.gc();
        createOOM();
        int gcCollectedCount = 0;
        while (q.remove(5L) != null) {
            gcCollectedCount++;
        }
        System.out.println("Found " + gcCollectedCount + " collected objects in ReferenceQueue");
        assertEquals(NUM_OBJECTS, gcCollectedCount);
    }

    // Create memory pressure to force soft references to be GC'd
    private void createOOM() {
        List<Long[]> buffer = new ArrayList<Long[]>(1000);
        try {
            for (int i = 0; i < 500000; i++) {
                buffer.add(new Long[OOM_BUFFER]);
            }
        } catch (OutOfMemoryError oom) {
            buffer.clear();
            return;
        }
        throw new RuntimeException("OOM expected");
    }
}
