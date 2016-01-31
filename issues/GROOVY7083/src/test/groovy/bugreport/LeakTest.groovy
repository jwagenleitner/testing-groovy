package bugreport

import org.junit.Ignore;
import org.junit.Test;

class LeakTest {

    @Test
    @Ignore("FIXME: fails with OOME")
    public void testLeak() {
        20000.times{
            new ConfigSlurper().parse("")
        }
    }
}
