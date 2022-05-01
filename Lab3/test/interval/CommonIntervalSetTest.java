package Lab3.test.interval;

import static org.junit.Assert.assertEquals;

import Lab3.src.interval.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tests for instance methods of IntervalSet.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain IntervalSet instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public class CommonIntervalSetTest{
	// Testing strategy
    // ����ÿ������ʱ:
    // �����observer����,������ֵ����ȷֵ�Ƚ�
    // �����constructor ���� producer,�����ɵĶ������׼����Ƚ�
    // �����mutator ���仯��Ķ�������ȷ�Ķ���Ƚ�
    CommonIntervalSet<String> commonIntervalSet = new CommonIntervalSet<>();
    Interval<String> intervalA1 = new Interval<>(0, 5, "A");
    Interval<String> intervalA2 = new Interval<>(6,12, "A");
    Interval<String> intervalB = new Interval<>(10, 16, "B");
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty IntervalSet of the particular implementation being tested
     */
    public IntervalSet<String> emptyInstance(){
        return new CommonIntervalSet<>();
    }
    
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled(){
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialLabelsEmpty(){
        Assert.assertEquals(emptyInstance(), new CommonIntervalSet<>());
    }
    
    @Test
    public void test(){
        try{
            commonIntervalSet.insert(intervalA1);
            commonIntervalSet.insert(intervalA2);
            commonIntervalSet.insert(intervalB);
        }catch(IntervalConflictException | LabelRepeatException e){
            System.out.println(e.toString());
        }
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        assertEquals(set, commonIntervalSet.labels());
        // ����labels�͹��캯��
        List<Interval<String>> list = new ArrayList<>();
        list.add(intervalA1);
        list.add(intervalA2);
        assertEquals(list, commonIntervalSet.getIntervals("A"));
        // ����getIntervals����
        assertEquals(new ArrayList<>(set), commonIntervalSet.labelsForTime(10));
        // ����labelsForTime����
        assertEquals(16, commonIntervalSet.maxTime());
        // ����maxTime����
        assertEquals((double) 1 / 8, commonIntervalSet.calcConflictRatio(), 0.0001);
        // ����conflictScale����
        assertEquals((double) 1 / 16, commonIntervalSet.calcFreeTimeRatio(), 0.0001);
        // ����freeScale����
        assertEquals("A = [0,5)\nA = [6,12)\nB = [10,16)\n", commonIntervalSet.toString());
        // ����toString����
        commonIntervalSet.remove("A");
        set.remove("A");
        assertEquals(set, commonIntervalSet.labels());
        // ����remove����
    }
}