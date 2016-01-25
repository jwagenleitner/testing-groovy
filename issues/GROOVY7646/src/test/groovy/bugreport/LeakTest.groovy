package bugreport;

import org.junit.Test;

class LeakTest {

    @Test
    public void testLeak() {
        100000.times{
            x -> assert 10 == Eval.x(2, 'x * 4 + 2;')
        }
    }
}
