package bugreport

import org.junit.Ignore;
import org.junit.Test;

public class LeakTest {
    @Test
    @Ignore("FIXME: fails with OOME")
    public void testForLeak() throws Exception{
        for(int i=1; i <= 10000 ; i++){
            GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
            Class<?> clazz = classLoader.parseClass("print 'hello world'");
            ExpandoMetaClass metaClass = getExpandoMetaClass(clazz);
            System.gc();
            Thread.sleep(2L);
        }
    }

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
       
}
