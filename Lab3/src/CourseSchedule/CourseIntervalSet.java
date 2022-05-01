package Lab3.src.CourseSchedule;

import Lab3.src.interval.CommonIntervalSet;
import Lab3.src.interval.Interval;
import Lab3.src.interval.IntervalConflictException;
import Lab3.src.interval.LabelRepeatException;

import java.util.*;

/*
Step 1 设定学期开始日期（年月日）和总周数（例如 18）
Step 2 增加一组课程，每门课程的信息包括：课程 ID、课程名称、教师名字、地点、周学时数（偶数）
Step 3 手工选择某个课程、上课时间（只能是 8-10 时、10-12 时、13-15 时、15-17 时、19-21 时），为其安排一次课，每次课的时间长度为2小时；可重
复安排，直到达到周学时数目时该课程不能再安排
Step 4 上步骤过程中，随时可查看哪些课程没安排、当前每周的空闲时间比例
Step 5 因为课程是周期性的，用户可查看本学期内任意一天的课表结果。
 */
public class CourseIntervalSet{
    private final List<CommonIntervalSet<Course>> list = new ArrayList<>();
    // 一周七天每天的课表,list是size为7的数组,每个元素表示每周中一天的课程安排
    private final Calendar begin;
    private final int weekNum;

    // AF: AF(list, begin, weekNum) = list记录每天的课表,开学日期为begin, 总周数是weekNum
    // RI: week > 0
    // Safety from rep exposure: 所有的属性都是private final,所有的get方法都使用了防御性复制
    /**
     * @param begin 开学日期
     * @param weekNum 总周数
     */
    public CourseIntervalSet(Calendar begin, int weekNum){
        // 每周七天每天都分配一个空课表,周日是第一天,所以索引是0,周一到周六就是1-6
        this.begin = begin;
        this.weekNum = weekNum;
        for(int i = 0; i < 7; i++)
            list.add(new CommonIntervalSet<>());
        checkRep();
    }

    // chengRep
    public void checkRep(){
        for(CommonIntervalSet<Course> i : list)
            i.checkRep();
        assert weekNum > 0;
    }

    /**
     * @return 开始日期
     */
    public Calendar begin(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, begin.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, begin.get(Calendar.MONTH));
        calendar.set(Calendar.DATE, begin.get(Calendar.DATE));
        return calendar;
    }

    /**
     * @return 总周数
     */
    public int weekNum(){
        return weekNum;
    }

    /**
     * 插入一门课程
     * @param courseInterval 插入的课程及时间段
     * @return 是否插入成功
     */
    public boolean insertCourse(Interval<Course> courseInterval){
        if(courseInterval.end() - courseInterval.start() != 2){
            System.out.println("一节课的时间不等于2学时");
            return false;
        }
        if(courseInterval.start() != 8 && courseInterval.start() != 10 && courseInterval.start() != 13 && courseInterval.start() != 15 && courseInterval.start() != 19){
            System.out.println("上课时间不符合规定");
            return false;
        }
        List<Interval<Course>> list1;
        int dayOfWeek = courseInterval.label().daysOfWeek(); // 星期几的课
        CommonIntervalSet<Course> intervalSet;
        for(Course course: list.get(dayOfWeek).labels()){
            // 找有无时间重复的课,若有,则开课的周数必须不能有重叠
            list1 = list.get(dayOfWeek).getIntervals(course); // 这一天课程为course的课程时间段对象
            for(Interval<Course> i: list1)
                if(i.start() == courseInterval.start()) // 两门课的开课时间相同
                    if(! (i.label().startWeek() > courseInterval.label().endWeek() || i.label().endWeek() < courseInterval.label().startWeek())){
                        // 两门课的开课周数有重复
                        System.out.println("两门课的开课时间重复了");
                        return false;
                    }
        }
        intervalSet = list.get(dayOfWeek);
        // 先在intervalSet添加这个课程,再用intervalSet替换list中对应的值
        try{
            intervalSet.insert(courseInterval);
        }catch(LabelRepeatException | IntervalConflictException e){
            e.printStackTrace();
        }
        list.set(dayOfWeek, intervalSet);
        checkRep();
        return true;
    }

    /**
     * @param week      哪一周
     * @param dayOfWeek 一周的哪一天
     * @param start     课程的开始时间
     * @return 课程
     */
    public Course findCourse(int week, int dayOfWeek, long start){
        for(Course course: list.get(dayOfWeek).labels()) // 这一天的所有课程
            for(Interval<Course> i: list.get(dayOfWeek).getIntervals(course)) // 遍历这天所有的某一门课
                if(i.start() == start && week >= course.startWeek() && week <= course.endWeek())
                    return course;
        return null; // 这个时间点没有课
    }

    /**
     * 查看已经安排的课程
     */
    public void seeArranged(){
        for(int i = 0; i < 7; i ++){
            if(! list.get(i).labels().isEmpty())
                System.out.println(list.get(i).toString());
        }
    }

    /**
     * 查看某一周的课程安排
     * @param week 周数
     */
    public void seeWeek(int week){
        String[] weekName = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Map<Integer, String> order = new HashMap<>();
        order.put(8, "1,2节");
        order.put(10, "3,4节");
        order.put(13, "5,6节");
        order.put(15, "7,8节");
        order.put(19, "9,10节");
        String s;
        Course c;
        for(int dayOfWeek = 0; dayOfWeek < 7; dayOfWeek++){
            System.out.print(weekName[dayOfWeek] + " ");
            for(int start: new int[]{8, 10, 12, 15, 19}){
                if(findCourse(week, dayOfWeek, start) != null){
                    c = findCourse(week, dayOfWeek, start);
                    s = c.toString().substring(2); // 截取除了前面周数以外的toString部分
                    System.out.print(order.get(start) + s + "\t");
                }
            }
            System.out.println();
        }
    }

    /**
     * @return 当前课程安排下的空闲时段比例
     */
    public float freeScale(){
        int total = 18 * 7 * 5; // 可以用来排课的时间
        int arrange = 0;
        for(int week = 0; week < 18; week++)
            for(int dayOfWeek = 0; dayOfWeek < 7; dayOfWeek++)
                for(int start: new int[]{8, 10, 12, 15, 19})
                    if(findCourse(week, dayOfWeek, start) != null)
                        arrange++;
        return 1 - (float) arrange / total;
    }
}