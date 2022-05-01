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
    // 测试每个方法时:
    // 如果是observer方法,将返回值与正确值比较
    // 如果是constructor 或者 producer,将生成的对象与标准对象比较
    // 如果是mutator 将变化后的对象与正确的对象比较
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
        // 测试labels和构造函数
        List<Interval<String>> list = new ArrayList<>();
        list.add(intervalA1);
        list.add(intervalA2);
        assertEquals(list, commonIntervalSet.getIntervals("A"));
        // 测试getIntervals函数
        assertEquals(new ArrayList<>(set), commonIntervalSet.labelsForTime(10));
        // 测试labelsForTime函数
        assertEquals(16, commonIntervalSet.maxTime());
        // 测试maxTime函数
        assertEquals((double) 1 / 8, commonIntervalSet.calcConflictRatio(), 0.0001);
        // 测试conflictScale函数
        assertEquals((double) 1 / 16, commonIntervalSet.calcFreeTimeRatio(), 0.0001);
        // 测试freeScale函数
        assertEquals("A = [0,5)\nA = [6,12)\nB = [10,16)\n", commonIntervalSet.toString());
        // 测试toString函数
        commonIntervalSet.remove("A");
        set.remove("A");
        assertEquals(set, commonIntervalSet.labels());
        // 测试remove函数
    }
}