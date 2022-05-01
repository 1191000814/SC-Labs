package Lab3.test.interval;

import Lab3.src.interval.Interval;
import Lab3.src.interval.IntervalConflictException;
import Lab3.src.interval.LabelRepeatException;
import Lab3.src.interval.MultiIntervalSet;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.IIOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tests for instance methods of MultiIntervalSet.
 */

public class MultiIntervalSetTest{
	// Testing strategy
	// 测试每个方法时:
	// 如果是observer方法,将返回值与正确值比较
	// 如果是constructor 或者 producer,将生成的对象与标准对象比较
	// 如果是mutator 将变化后的对象与正确的对象比较
    MultiIntervalSet<String> multiIntervalSet = new MultiIntervalSet<>();
    MultiIntervalSet<String> multiIntervalSet1 = new MultiIntervalSet<>();
    Interval<String> intervalA1 = new Interval<>(0, 5, "A");
    Interval<String> intervalA2 = new Interval<>(13,15, "A");
    Interval<String> intervalB = new Interval<>(5, 10, "B");

    @Test
    public void test(){
        try{
            multiIntervalSet.insert(intervalA1);
            multiIntervalSet.insert(intervalA2);
            multiIntervalSet.insert(intervalB);
        }catch(IntervalConflictException | LabelRepeatException e){
            System.out.println(e.toString());
        }
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        Assert.assertEquals(set, multiIntervalSet.labels());
        // 测试labels和构造函数
        List<Interval<String>> list = new ArrayList<>();
        list.add(intervalA1);
        list.add(intervalA2);
        Assert.assertEquals(list, multiIntervalSet.getIntervals("A"));
        // 测试getIntervals函数
        set.remove("B");
        Assert.assertEquals(new ArrayList<>(set), multiIntervalSet.labelsForTime(13));
        // 测试labelsForTime函数
        Assert.assertEquals(15, multiIntervalSet.maxTime());
        // 测试maxTime函数
        Assert.assertEquals(0.0, multiIntervalSet.calcConflictRatio(), 0.0001);
        // 测试conflictScale函数
        Assert.assertEquals(0.2, multiIntervalSet.calcFreeTimeRatio(), 0.0001);
        // 测试freeScale函数
        Assert.assertEquals("A = [0,5)\nB = [5,10)\nA = [13,15)\n", multiIntervalSet.toString());
        // 测试toString函数
        try{
            multiIntervalSet1.insert(new Interval<>(0, 3, "A"));
            multiIntervalSet1.insert(new Interval<>(6, 12, "B"));
            multiIntervalSet1.insert(new Interval<>(13, 18, "C"));
        }catch(IntervalConflictException | LabelRepeatException e){
            System.out.println(e.toString());
        }
        Assert.assertEquals((double) 7 / 18, multiIntervalSet.similarity(multiIntervalSet1), 0.0001);
        // 测试similarity函数
        multiIntervalSet.remove("A");
        set.remove("A");
        set.add("B");
        Assert.assertEquals(set, multiIntervalSet.labels());
        // 测试remove函数
    }
}
