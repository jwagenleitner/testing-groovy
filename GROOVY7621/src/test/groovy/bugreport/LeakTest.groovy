package bugreport

class LeakTest extends GroovyTestCase {

    void testLeak() {
        def sampleSchema = [:]
        sampleSchema.table1 = [column1: [name:'column1', dataType : 'varchar']
                               , column2: [name:'column2' ,dataType : 'integer']]

        def propMissing = { metaThings ->
            def pMissing = null
            pMissing = { pName ->
                def innerProp = delegate.hasProperty("outerType")
                if (!innerProp ){
                    if (sampleSchema[pName]) {
                        def resolved = "->"+pName
                        resolved.metaClass.propertyMissing = pMissing
                        resolved.metaClass.outerType = "table"
                        resolved.metaClass.outer = pName
//                        metaThings << resolved
                        return resolved
                    }
                } else if (delegate.outerType == "table") {
                    if (sampleSchema[delegate.outer][pName]) {
                        def resCol = "->"+pName
                        resCol.metaClass.propertyMissing = pMissing
                        resCol.metaClass.outerType = "column"
                        resCol.metaClass.outer = sampleSchema[delegate.outer][pName]
//                        metaThings << resCol
                        return resCol
                    }
                } else if (delegate.outerType == "column") {
                    // No metaClasses because no more nesting...
                    if ( pName == "dataType" ) {
                        return delegate.outer.dataType
                    } else if ( pName == "name") {
                        return delegate.outer.name
                    }
                }
                throw new MissingPropertyException(pName)
            }
            return pMissing
        }

        def sampleScript = '''
            def result = table1.column1.dataType
            if (!(++counter % 1000)) println "${counter}. ${result}"
        '''

        def gs = new GroovyShell();
        def binding = new Binding()
        binding.counter = 0
        def script = gs.parse( sampleScript )
        script.setBinding(binding)
        def needsCleanup = []
        script.metaClass.propertyMissing = propMissing(needsCleanup)

        for ( int i = 0; i < 1000000; i++) {
            script.run()
//            if (!(i % 100)) {
                // needsCleanup ... how to? I tried remove from metaClassRegistry to no avail...
                //
                // this gives errro:
                //Caught: groovy.lang.MissingMethodException: No signature of method: org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl.removeMetaClass() is applicable for argument types: (java.lang.String, null) values: [->table1, null]
                //Possible solutions: removeMetaClass(java.lang.Class)
                //needsCleanup.each { GroovySystem.metaClassRegistry.removeMetaClass(it,null) }
//                needsCleanup.each {
//                    it.metaClass = null
//                }
//                needsCleanup = [] //.clear()
//            }
        }
    }
}
