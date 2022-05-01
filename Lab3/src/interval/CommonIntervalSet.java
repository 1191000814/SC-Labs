package Lab3.src.interval;

import java.util.*;
// 限制性最小的类,可以重叠,可以重复标签,可以有空白
public class CommonIntervalSet<L> implements IntervalSet<L>{
    /**
     * e.g., { "A"=[0,10), "B"=[20,30) } is represented by
     * labelList = <"A", "B">
     * valueList = <0, 10, 20, 30>
     */
    protected final List<Interval<L>> intervalList = new ArrayList<>();
    // Abstraction function:
    //   AF(intervalSet) = 一个可以重叠,有空白,重复标签的时间段
    // Representation invariant:
    //   每个时间段都要符合Interval的RI
    // Safety from rep exposure:
    //   属性是protect,不能被外部类引用,只能被子类引用,而该类与子类中均无get方法

    // constructor
    public CommonIntervalSet(){}

    // checkRep
    public void checkRep(){
        for(Interval<L> i : intervalList) // 对时间段列表中每一个时间段分别check
            i.checkRep();
    }

    /**
     * 插入一个时间间隔,按开始时间升序排序
     * 如果开始时间相同,则按结束时间升序排序
     * 这个类中的insert方法不会有时间冲突
     *
     * @param interval 插入的时间段
     */
    @Override
    public void insert(Interval<L> interval) throws IntervalConflictException, LabelRepeatException{
        if(intervalList.isEmpty()){// 如果列表为空,直接插入
            intervalList.add(interval);
            checkRep();
            return;
        }
        for(int i = 0; i < intervalList.size(); i++)
            if(interval.compareTo(intervalList.get(i)) < 0){
                intervalList.add(i, interval);
                checkRep();
                return;
            }
        intervalList.add(interval);
    }

    /**
     * @return 标签集合
     */
    @Override
    public Set<L> labels(){
        Set<L> labels = new LinkedHashSet<>();
        // 使用有序的LinkedHashSet集合
        for(Interval<L> i : intervalList)
            labels.add(i.label());
        return labels;
    }

    /**
     * @param label to remove
     * @return 是否移除成功
     */
    @Override
    public boolean remove(L label){
        boolean isRemove = false;
        int n = intervalList.size();
        for(int i = 0; i < n; i ++){
            if(intervalList.get(i).label().equals(label)){
                intervalList.remove(intervalList.get(i));
                i --;
                n --;
                if(! isRemove)
                    isRemove = true;
            }
        }
        checkRep();
        return isRemove;
    }

    /**
     * @param label the label
     * @return 标签的开始时间
     * @throws NoSuchElementException 没有这个标签
     */
    @Override
    public List<Interval<L>> getIntervals(L label) throws NoSuchElementException{
        boolean isExist = false;
        List<Interval<L>> list  = new ArrayList<>();
        for(Interval<L> i : intervalList)
            if(i.label().equals(label)){
                list.add(i);
                isExist = true;
            }
        if(! isExist)
            throw new NoSuchElementException();
        return list;
    }

    /**
     * 获得某个时刻所属的标签
     * @param time 时刻
     * @return 标签
     */
    public List<L> labelsForTime(long time){
        List<L> labList = new ArrayList<>();
        List<Interval<L>> list;
        for(L lab : labels()){ // 遍历每个标签
            list = getIntervals(lab);
            for(Interval<L> i : list){ // 遍历这个属于标签的每个时间段
                if(i.start() <= time && i.end() > time){
                    labList.add(lab); // 添加了这个标签,就可以退出这个标签的循环了
                    break;
                }
            }
        }
        return labList;
    }

    /**
     * @return 最大的结束时间
     */
    public long maxTime(){
        long maxTime = 0;
        for(Interval<L> i : intervalList){
            if(i.end() > maxTime)
                maxTime = i.end();
        }
        return maxTime;
    }

    /**
     * @return 时间冲突比例
     */
    public float calcConflictRatio(){
        long conflictTime = 0;
        for(long time = 0; time < maxTime(); time ++){
            if(labelsForTime(time).size() > 1)
                conflictTime ++;
        }
        return (float) conflictTime / maxTime();
    }

    /**
     * @return 时间空闲比例
     */
    public float calcFreeTimeRatio(){
        long freeTime = 0;
        for(long time = 0; time < maxTime(); time ++){
            if(labelsForTime(time).size() == 0)
                freeTime ++;
        }
        return (float) freeTime / maxTime();
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        CommonIntervalSet<?> that = (CommonIntervalSet<?>)o;
        return Objects.equals(intervalList, that.intervalList);
    }

    @Override
    public int hashCode(){
        return Objects.hash(intervalList);
    }

    /**
     * @return 对象的字符串形式
     */
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(Interval<L> inter: intervalList)
            s.append(inter.label()).append(" = [").append(inter.start()).append(",").append(inter.end()).append(")\n");
        return s.toString();
    }
}