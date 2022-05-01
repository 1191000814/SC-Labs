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
    // ����ÿ������ʱ:
    // �����observer����,������ֵ����ȷֵ�Ƚ�
    // �����constructor ���� producer,�����ɵĶ������׼����Ƚ�
    // �����mutator ���仯��Ķ�������ȷ�Ķ���Ƚ�

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
