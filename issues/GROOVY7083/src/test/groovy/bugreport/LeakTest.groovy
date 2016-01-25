package bugreport;

import org.junit.Test;

class LeakTest {

    @Test
    public void testLeak() {
        20000.times{
            new ConfigSlurper().parse("")
        }
    }
}
