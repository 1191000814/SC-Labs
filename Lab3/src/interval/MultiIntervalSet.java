package Lab3.src.interval;

import java.util.*;

/**
 * A mutable set of labeled intervals, where each label is associated with one
 * or more non-overlapping half-open intervals [start, end). Neither intervals
 * with the same label nor with different labels may overlap.
 * <p>
 * Labels are of immutable type L and must implement the Object contract: they are
 * compared for equality using Object.equals(java.lang.Object).
 * <p>
 * For example, { * "A"=[[0,10)], "B"=[[20,30)] } is a multi-interval set where
 * the labels are Strings "A" and "B". We could add "A"=[10,20) to that set to obtain
 * {"A"=[[0,10),[10,20)], "B"=[[20,30)] }.
 * <p>
 * PS2 instructions: this is a required ADT class. You may not change the
 * specifications or add new public methods. You must use IntervalSet in your
 * rep, but otherwise the implementation of this class is up to you.
 *
 * @param <L> type of labels in this set, must be immutable
 */
public class MultiIntervalSet<L> extends CommonIntervalSet<L>{
    // Abstraction function:
    //   AF(intervalSet) = 一个不能重叠,有空白,重复标签的时间段
    // Representation invariant:
    //   每个时间段都要符合Interval的RI
    // Safety from rep exposure:
    //   属性是protect,不能被外部类引用,只能被子类引用,而该类与子类中均无get方法

    /**
     * Create an empty multi-interval set.
     */
    public MultiIntervalSet(){}

    // checkRep
    @Override
    public void checkRep(){
        super.checkRep(); // 至少得满足父类的checkRep
        int n = intervalList.size();
        // 任意两个时间段都不能重叠
        for(int i = 0; i < n; i ++)
            for(int j = i + 1; j < n; j ++)
                assert ! intervalList.get(i).conflict(intervalList.get(j));
    }

    /**
     * Add a labeled interval (if not present) to this set, if it does not conflict with existing intervals.
     * <p>
     * Labeled intervals conflict if:
     * they have the same label with different, overlapping intervals, or
     * they have different labels with overlapping intervals.
     * <p>
     * For example, if this set is { "A"=[[0,10),[20,30)] },
     * insert("A"=[0,10)) has no effect
     * insert("B"=[10,20)) adds "B"=[[10,20)]
     * insert("C"=[20,30)) throws IntervalConflictException
     *
     * @param interval interval to add
     * @throws IntervalConflictException if label is already in this set and associated
     *                                   with an interval other than [start,end) that
     *                                   overlaps [start,end), or if an interval in this
     *                                   set with a different label overlaps [start,end)
     */
    @Override
    public void insert(Interval<L> interval) throws IntervalConflictException, LabelRepeatException{
        for(Interval<L> i : intervalList)
            if(i.conflict(interval))
                throw new IntervalConflictException("存在时间冲突,无法添加");
        super.insert(interval);
        checkRep();
    }

    /**
     * 求两个MultiIntervalSet的相似度
     * @param o 另一个MultiIntervalSet
     * @return 相似度
     */
    public float similarity(MultiIntervalSet<L> o){
        long maxTime = Math.max(o.maxTime(), this.maxTime());
        long similarTime = 0;
        for(long time = 0; time < maxTime; time ++){
            if(! this.labelsForTime(time).isEmpty() && ! o.labelsForTime(time).isEmpty()){
                if(this.labelsForTime(time).get(0).equals(o.labelsForTime(time).get(0)))
                    similarTime++;
            }
        }
        return (float) similarTime / maxTime;
    }
}