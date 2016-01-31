package bugreport;

import java.lang.ref.*;
import java.util.*;
import groovy.lang.*;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoLeakTest {

    private static final int NUM_OBJECTS = 31;
    static ReferenceBundle bundle = ReferenceBundle.getWeakBundle();

    ReferenceQueue<ClassLoader> classLoaderQueue = new ReferenceQueue<ClassLoader>();
    ReferenceQueue<Class<?>> classQueue = new ReferenceQueue<Class<?>>();
    ReferenceQueue<ClassInfo> classInfoQueue = new ReferenceQueue<ClassInfo>();

    // Used to keep a hard reference to the PhantomReferences so they are not collected
    List<Object> refList = new ArrayList<Object>(NUM_OBJECTS);

    @Before
    public void setUp() {
        // Make sure we switch over to callback manager
        ReferenceManager manager = bundle.getManager();
        for (int i = 0; i < 501; i++) {
            manager.afterReferenceCreation(null);
        }
    }

    @Test
    public void testLeak() {
        assertFalse(Boolean.getBoolean("groovy.use.classvalue"));
        for (int i = 0; i < NUM_OBJECTS; i++) {
            GroovyClassLoader gcl = new GroovyClassLoader();
            Class scriptClass = gcl.parseClass("int myvar = " + i);
            ClassInfo ci = ClassInfo.getClassInfo(scriptClass);
            PhantomReference<ClassLoader> classLoaderRef = new PhantomReference<ClassLoader>(gcl, classLoaderQueue);
            PhantomReference<Class<?>> classRef = new PhantomReference<Class<?>>(scriptClass, classQueue);
            PhantomReference<ClassInfo> classInfoRef = new PhantomReference<ClassInfo>(ci, classInfoQueue);
            refList.add(classLoaderRef);
            refList.add(classRef);
            refList.add(classInfoRef);
            System.gc();
        }
        System.gc();
        // Encourage GC to collect soft references
        try { throw new OutOfMemoryError(); } catch(OutOfMemoryError oom) { }
        for (int i = 0; i < 10; i++) {
            System.gc();
        }

        // All objects should have been collected
        assertEquals("GroovyClassLoaders not collected by GC", NUM_OBJECTS, queueSize(classLoaderQueue));
        assertEquals("Script Classes not collected by GC", NUM_OBJECTS, queueSize(classQueue));

        int ciSize = queueSize(classInfoQueue);
        int ciSizeExpected = NUM_OBJECTS  - 3; // give or take a few
        assertTrue("ClassInfo objects [" + ciSize + "] not collected by GC, expected [" + ciSizeExpected + "]", ciSizeExpected <= ciSize);
    }

    private int queueSize(ReferenceQueue<?> queue) {
        int size = 0;
        while (queue.poll() != null) {
            ++size;
        }
        return size;
    }
}
