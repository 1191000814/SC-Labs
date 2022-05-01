package Lab3.src.interval;

import java.util.Objects;

public class Interval<L> implements Comparable<Interval<L>>{
    private final long start;
    private final long end;
    private final L label;

    /**
     * 构造方法
     * @param start 开始时间
     * @param end 结束时间
     * @param label 标签
     */
    public Interval(long start, long end, L label){
        this.start = start;
        this.end = end;
        this.label = label;
    }

    // checkRep
    public void checkRep(){
        assert start >= 0; // 开始时间大于0
        assert end > start; // 结束时间大于开始时间
        assert label != null; // 标签不为空
    }

    /**
     * @return 开始时间
     */
    public long start(){
        return start;
    }

    /**
     * @return 结束时间
     */
    public long end(){
        return end;
    }

    /**
     * @return 标签
     */
    public L label(){
        return label;
    }

    /**
     * @param i 另一个时间段
     * @return 两者是否有时间重叠
     */
    public boolean conflict(Interval<L> i){
        i.checkRep();
        return this.start < i.end && this.end > i.start;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Interval<?> interval = (Interval<?>)o;
        return start == interval.start && end == interval.end && Objects.equals(label, interval.label);
    }

    @Override
    public int hashCode(){
        return Objects.hash(start, end, label);
    }

    /**
     * @param i 另一个标签
     * @return 两个标签的前后关系
     */
    @Override
    public int compareTo(Interval<L> i){
        if(this.start != i.start)
            return (int) (this.start - i.start);
        if(this.end != i.end)
            return (int) (this.end - i.end);
        return 0;
    }
}