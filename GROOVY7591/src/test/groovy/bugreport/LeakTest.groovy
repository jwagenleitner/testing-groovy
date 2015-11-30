package bugreport;

import groovy.lang.ExpandoMetaClass;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;

import org.junit.Test;

public class LeakTest {
        @Test
        public void testForLeak() throws Exception{
                for(int i=1; i <= 10000 ; i++){
                        GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
                        Class<?> clazz = classLoader.parseClass("print 'hello world'");
                        ExpandoMetaClass metaClass = getExpandoMetaClass(clazz);
                        System.gc();
                        Thread.sleep(15);
                        //System.out.println("Done " + i + " iterations");
                }
        }
        //--Begin copied unmodified from GrailsMetaClassUtils--//
    public static ExpandoMetaClass getExpandoMetaClass(Class<?> aClass) {
        MetaClassRegistry registry = getRegistry();

        MetaClass mc = registry.getMetaClass(aClass);
        if (mc instanceof ExpandoMetaClass) {
            ExpandoMetaClass emc = (ExpandoMetaClass) mc;
            registry.setMetaClass(aClass, emc); // make permanent
            return emc;
        }

        registry.removeMetaClass(aClass);
        mc = registry.getMetaClass(aClass);
        if (mc instanceof ExpandoMetaClass) {
            return (ExpandoMetaClass)mc;
        }

        ExpandoMetaClass emc = new ExpandoMetaClass(aClass, true, true);
        emc.initialize();
        registry.setMetaClass(aClass, emc);
        return emc;
    }
   
    public static MetaClassRegistry getRegistry() {
        return GroovySystem.getMetaClassRegistry();
    }
        //--End copied unmodified from GrailsMetaClassUtils--//
       
} 