package Lab3.src.CourseSchedule;

import Lab3.src.interval.Interval;

import java.util.*;

public class CourseScheduleApp{
    static Calendar termBegin = new GregorianCalendar(); // 开学日期
    static int weeks; // 总周数
    static CourseIntervalSet schedule; // 排课管理系统
    static int select; // 每次做的选择
    static Scanner sc = new Scanner(System.in);
    static Map<String, Integer> frequency = new HashMap<>(); // 每周的上课次数

    public static void main(String[] args){
        init();
        arrange();
    }

    /**
     * 初始化
     */
    public static void init(){
        int month, date;
        int[] daysOfMonth = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        month = alternative("开学月份是:", 1, 12, "月份只能是1-12月");
        date = alternative("开学在" + month + "月的哪天:", 1, daysOfMonth[month - 1], "输入的日期不对");
        termBegin.set(Calendar.YEAR, 2021);
        termBegin.set(Calendar.MONTH, month - 1);
        termBegin.set(Calendar.DATE, date);
        weeks = alternative("一共有多少周", 10, 20, "周数只能在10-20之间");
        System.out.println("开学日期为2021年" + month + "月" + date + "日");
        schedule = new CourseIntervalSet(termBegin, weeks);
        frequency.put("计算机系统", 2);
        frequency.put("形式语言与自动机", 2);
        frequency.put("马克思原理概论", 2);
        frequency.put("体育", 1);
        frequency.put("软件构造", 2);
        frequency.put("信息安全概论", 2);
        frequency.put("形势与政策", 1);
    }

    /**
     * 按照菜单进行操作
     */
    public static void arrange(){
        while(true){
            if(finished()){
                System.out.println("课程全部安排完成,退出程序");
                System.exit(0);
            }
            menu();
            if(select == 1) // 添加课程
                addCourse();
            else if(select == 2){ // 查看排课进度
                viewSchedule();
                System.out.println("还有" + 100 * schedule.freeScale() + "%的空闲时段可以用来排课");
            }else // 查询周课表
                referWeek();
        }
    }

    /**
     * 展示菜单
     */
    public static void menu(){
        System.out.println("------------------------");
        alternative("下一步你想:\n1.安排课程\t2.查看排课进度\t3.查询课表", 1, 3, "没有这个选项,请重新选择:");
    }

    /**
     * 添加一门课程
     */
    public static void addCourse(){
        String name, teacherName, classroom;
        int id, startWeek, endWeek, dayOfWeek, start, end;
        int i = 1;
        List<String> list = new ArrayList<>(); // 还没安排完的课程列表
        for(String s : frequency.keySet())
            if(frequency.get(s) > 0)
                list.add(s);
        for(String s : list) // 列出未安排的课程
            System.out.print((i ++) + "." + s + "\t");
        System.out.println();

        name = list.get(alternative("选择课程:", 1, list.size(), "没有这个选项") - 1);
        id = alternative("课程id:", 0, Integer.MAX_VALUE, "课程id只能是数字");

        System.out.println("教师名称:");
        teacherName = sc.next();

        System.out.println("教室位置:");
        classroom = sc.next();

        startWeek = alternative("开始周:", 1,weeks, "开课周数只能在1-" + weeks + "周之间");
        endWeek = alternative("结束周:", startWeek, weeks, "结课周数只能在" + startWeek + "-" + weeks + "周之间:");
        dayOfWeek = alternative("在一周的第几天上课(0-6):", 0, 6, "只能在周日到周六之间上课");
        start = alternative("几点开始上课:", 8, 19, "上课时间是8-19时");
        end = start + 2;

        Course course = new Course(id, name, teacherName, classroom, startWeek, endWeek, dayOfWeek);
        if(schedule.insertCourse(new Interval<>(start, end, course))){
            System.out.println("添加成功************");
            frequency.put(name, frequency.get(name) - 1); // 这门课还要安排的次数减1
        }
        else
            System.out.println("添加失败............");
    }

    /**
     * 查看已经安排的课程进度
     */
    public static void viewSchedule(){
        for(String s : frequency.keySet())
            if(frequency.get(s) > 0)
                System.out.println(s + " : 还有" + frequency.get(s) + "次排课");
        System.out.println("========================");
        schedule.seeArranged();
    }

    /**
     * 查询周课表
     */
    public static void referWeek(){
        int week = alternative("需要查询哪一周的课表:", 1, weeks, "没有这一周");
        schedule.seeWeek(week);
    }

    /**
     * 作出一次选择
     *
     * @param issue 选择前的提示
     * @param min 选择的最小值
     * @param max 选择的最大值
     * @param warning 选择错误的警告
     * @return 作出的选择值
     */
    public static int alternative(String issue, int min, int max, String warning){
        System.out.println(issue);
        boolean inputMatch;
        do{
            String str = sc.next();
            try{
                inputMatch = true;
                select = Integer.parseInt(str);
            }catch(NumberFormatException e){
                // InputMatchException是不能捕捉的,所以改成捕捉NumberFormatException
                System.out.println(e.toString());
                System.out.println("重新输入:");
                inputMatch = false; // 如果捕捉到异常,inputMatch就为false
            }
            if(inputMatch){
                if(select < min || select > max)
                    System.out.println(warning);
                else
                    break;
            }
        }while(true);
        return select;
    }

    /**
     * @return 课程是否全部安排完成
     */
    public static boolean finished(){
        for(String s : frequency.keySet())
            if(frequency.get(s) > 0)
                return false;
        return true;
    }
}