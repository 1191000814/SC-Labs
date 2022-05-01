package Lab3.src.interval;

import java.util.NoSuchElementException;

// 一个标签一个时间段, 不允许交叉的时间段, 不允许有空白
public class LinearIntervalSet<L> extends MultiIntervalSet<L>{
    // Abstraction function:
    //   AF(intervalSet) = 一个可以重叠,不能有空白,不能又重复标签的时间段
    // Representation invariant:
    //   每个时间段都要符合Interval的RI
    // Safety from rep exposure:
    //   属性是protect,不能被外部类引用,只能被子类引用,而该类没有子类

    // 构造方法
    public LinearIntervalSet(){};

    // checkRep
    @Override
    public void checkRep(){
        super.checkRep();
        int n = intervalList.size();
        // 任意两个时间段都不能重叠
        for(int i = 0; i < n; i ++)
            for(int j = i + 1; j < n; j ++)
                assert intervalList.get(i).label() != intervalList.get(j).label();
    }

    /**
     * 插入一个时间段
     * @param interval interval to add
     * @throws IntervalConflictException 时间冲突
     * @throws LabelRepeatException 标签重复
     */
    @Override
    public void insert(Interval<L> interval) throws IntervalConflictException, LabelRepeatException{
        for(Interval<L> i : intervalList)
            if(i.label().equals(interval.label()))
                throw new LabelRepeatException("标签重复不能添加");
        super.insert(interval);
        checkRep();
    }

    /**
     * 获得该标签所在的时间段(只有一个)
     * @param label 标签
     * @return 时间段
     * @throws NoSuchElementException 没有这个标签
     */
    public Interval<L> getInterval(L label) throws NoSuchElementException{
        for(Interval<L> i: intervalList)
            if(i.label().equals(label))
                return i;
        throw new NoSuchElementException("没有该标签的时间段");
    }
}