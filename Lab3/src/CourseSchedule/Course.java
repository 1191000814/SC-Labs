package Lab3.src.CourseSchedule;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 课程类
 */
final public class Course{
    private final long id; // 课程id
    private final String courseName; // 课程姓名
    private final String teacherName; // 教室姓名
    private final String classroom; // 教室位置
    private final int startWeek; // 开始周
    private final int endWeek; // 结束周
    private final int dayOfWeek; // 在周几上课

    /**
     * @param id 课程id
     * @param courseName 课程名称
     * @param teacherName 教师名称
     * @param classroom 教室名称
     * @param startWeek 开课周
     * @param endWeek 结课周
     * @param dayOfWeek 一周哪一天上课
     */
    public Course(long id, String courseName, String teacherName, String classroom, int startWeek, int endWeek, int dayOfWeek){
        this.id = id;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.classroom = classroom;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.dayOfWeek = dayOfWeek;
    }

    // checkRep
    public void checkRep(){
        assert id > 0;
        assert startWeek > 0;
        assert endWeek > startWeek;
        assert dayOfWeek >= 0 && dayOfWeek <= 6;
    }

    /**
     * @return 在一周的哪一天上课
     */
    public int daysOfWeek(){
        return dayOfWeek;
    }

    /**
     * @return 课程id
     */
    public long id(){
        return id;
    }

    /**
     * @return 课程名称
     */
    public String courseName(){
        return courseName;
    }

    /**
     * @return 教师名称
     */
    public String teacherName(){
        return teacherName;
    }

    /**
     * @return 教室名称
     */
    public String classroom(){
        return classroom;
    }

    /**
     * @return 开始周
     */
    public int startWeek(){
        return startWeek;
    }

    /**
     * @return 结课周
     */
    public int endWeek(){
        return endWeek;
    }

    @Override
    public String toString(){
        String[] weekName = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        return weekName[dayOfWeek] + " " + courseName + " " + teacherName + " " + classroom + " " + "[" + startWeek + "-" + endWeek + "周]";
    }
}
