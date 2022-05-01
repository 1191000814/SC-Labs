package Lab3.test.interval;

import Lab3.src.interval.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LinearIntervalSetTest{
    // Testing strategy
    // 测试每个方法时:
    // 如果是observer方法,将返回值与正确值比较
    // 如果是constructor 或者 producer,将生成的对象与标准对象比较
    // 如果是mutator 将变化后的对象与正确的对象比较

    LinearIntervalSet<String> linearIntervalSet = new LinearIntervalSet<>();
    Interval<String> intervalA = new Interval<>(0, 5, "A");
    Interval<String> intervalB = new Interval<>(5, 10, "B");
    Interval<String> intervalC = new Interval<>(10,15, "C");

    @Test
    public void test(){
        try{
            linearIntervalSet.insert(intervalA);
            linearIntervalSet.insert(intervalB);
            linearIntervalSet.insert(intervalC);
        }catch(IntervalConflictException | LabelRepeatException e){
            System.out.println(e.toString());
        }
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        set.add("C");
        Assert.assertEquals(set, linearIntervalSet.labels());
        // 测试labels和构造函数
        Assert.assertEquals(intervalA, linearIntervalSet.getInterval("A"));
        // 测试getIntervals函数
        set.remove("B");
        set.remove("A");
        Assert.assertEquals(new ArrayList<>(set), linearIntervalSet.labelsForTime(10));
        // 测试labelsForTime函数
        Assert.assertEquals(15, linearIntervalSet.maxTime());
        // 测试maxTime函数
        Assert.assertEquals(0.0, linearIntervalSet.calcConflictRatio(), 0.0001);
        // 测试conflictScale函数
        Assert.assertEquals(0.0, linearIntervalSet.calcFreeTimeRatio(), 0.0001);
        // 测试freeScale函数
        Assert.assertEquals("A = [0,5)\nB = [5,10)\nC = [10,15)\n", linearIntervalSet.toString());
        // 测试toString函数
        linearIntervalSet.remove("A");
        set.remove("A");
        set.add("B");
        Assert.assertEquals(set, linearIntervalSet.labels());
        // 测试remove函数
    }
}
