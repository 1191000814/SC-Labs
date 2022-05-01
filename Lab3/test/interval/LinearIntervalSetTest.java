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
    // ����ÿ������ʱ:
    // �����observer����,������ֵ����ȷֵ�Ƚ�
    // �����constructor ���� producer,�����ɵĶ������׼����Ƚ�
    // �����mutator ���仯��Ķ�������ȷ�Ķ���Ƚ�

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
        // ����labels�͹��캯��
        Assert.assertEquals(intervalA, linearIntervalSet.getInterval("A"));
        // ����getIntervals����
        set.remove("B");
        set.remove("A");
        Assert.assertEquals(new ArrayList<>(set), linearIntervalSet.labelsForTime(10));
        // ����labelsForTime����
        Assert.assertEquals(15, linearIntervalSet.maxTime());
        // ����maxTime����
        Assert.assertEquals(0.0, linearIntervalSet.calcConflictRatio(), 0.0001);
        // ����conflictScale����
        Assert.assertEquals(0.0, linearIntervalSet.calcFreeTimeRatio(), 0.0001);
        // ����freeScale����
        Assert.assertEquals("A = [0,5)\nB = [5,10)\nC = [10,15)\n", linearIntervalSet.toString());
        // ����toString����
        linearIntervalSet.remove("A");
        set.remove("A");
        set.add("B");
        Assert.assertEquals(set, linearIntervalSet.labels());
        // ����remove����
    }
}
