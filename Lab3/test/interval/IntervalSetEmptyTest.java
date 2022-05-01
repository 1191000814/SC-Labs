package Lab3.test.interval;

import static org.junit.Assert.assertEquals;

import Lab3.src.interval.IntervalSet;
import org.junit.Test;

import java.util.Collections;

/**
 * Tests for static methods of IntervalSet.
 * <p>
 * To facilitate testing multiple implementations of IntervalSet, instance methods are
 * tested in IntervalSetTest.
 */
public class IntervalSetEmptyTest{
    // Testing strategy
    // 测试每个方法时:
    // 如果是observer方法,将返回值与正确值比较
    // 如果是constructor 或者 producer,将生成的对象与标准对象比较
    // 如果是mutator 将变化后的对象与正确的对象比较

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled(){
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testEmptyVerticesEmpty(){
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), IntervalSet.empty().labels());
    }
}
