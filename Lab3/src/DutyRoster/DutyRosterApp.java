package Lab3.src.DutyRoster;

import java.util.*;

public class DutyRosterApp{
    static Calendar calendar = new GregorianCalendar(); // 开始日期
    static int duration; // 值班时长
    static DutyIntervalSet roster; // 排班系统
    static Scanner sc = new Scanner(System.in); // 输入
    static int select; // 每次作出的选择

    public static void main(String[] args){
        init(); // 初始化整个系统
        selectWay(); // 选择排班方式(手动还是自动)
        if(select == 1)
            autoArrange(); // 自动排班
        else
            manualArrange(); // 手动排班
    }

    /**
     * 初始化
     */
    public static void init(){
        int month, date;
        month = alternative("值班开始月份是:", 1, 12, "月份只能是1-12月,重新输入:");
        int[] daysOfMonth = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        date = alternative(month + "月的多少日:", 1, daysOfMonth[month - 1], "输入的日期不对,重新输入:");
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, date);
        duration = alternative("整个工时是多少天:", 1, Integer.MAX_VALUE,"输入的天数不能为负数");
        System.out.println("开工日期为2021年" + month + "月" + date + "日");
        calendar.add(Calendar.DATE, duration - 1); // 停工日期(最后一天)
        System.out.println("停工日期为" + calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DATE) + "日");
        calendar.add(Calendar.DATE, 1 - duration); // 恢复到开工日期
        roster = new DutyIntervalSet(calendar, duration);
    }

    /**
     * 选择排班方式
     */
    public static void selectWay(){
        alternative("选择自动排班还是手动排班:\n1.自动\t2.手动",1, 2, "没有这个选项");
    }

    /**
     * 自动排班
     */
    public static void autoArrange(){
        System.out.println("暂时没有员工,需要先添加员工");
        addEmployee();
        menu();
        while(select != 1){
            if(select == 2)
                addEmployee();
            else
                delEmployee();
            menu();
        }
        roster.autoInsert();
        System.out.println(roster);
        System.out.println("自动排班完成,退出系统***********");
        System.exit(0);
    }

    /**
     * 手动排班
     */
    public static void manualArrange(){
        while(! roster.checkEveryday(false)){
            if(roster.employeeSet().isEmpty()){
                System.out.println("暂时没有员工,需要先添加员工");
                addEmployee();
            }
            menu();
            if(select == 1) // 安排值班
                arrange();
            else if(select == 2) // 添加员工
                addEmployee();
            else
                delEmployee();
        }
        System.out.println("手动排班完成,退出系统***********");
    }

    /**
     * 展示菜单
     */
    public static void menu(){
        System.out.println("------------------------");
        alternative("下一步你想:\n1.安排值班\t2.添加员工\t3.删除员工", 1, 3, "没有这个选项,请重新选择:");
    }

    /**
     * 对给出的选项作出一次选择
     * @param issue 提示
     * @param min 最小值
     * @param max 最大值
     * @param warning 选择不合理时的警告
     * @return 作出的选择值
     * @throws InputMismatchException 输出的不是整数
     */
    public static int alternative(String issue, int min, int max, String warning) throws InputMismatchException{
        System.out.println(issue);
        boolean inputMatch;
        do{
            String str = sc.next();
            try{
                inputMatch = true;
                select = Integer.parseInt(str);
            }catch(NumberFormatException e){
                System.out.println(e.toString());
                System.out.println("重新输入:");
                inputMatch = false;
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
     * 安排一次值班
     */
    public static void arrange(){
        roster.checkEveryday(true);
        List<Employee> list = new ArrayList<>(roster.employeeSet());
        System.out.println("选择排班的员工:");
        int size = list.size();
        for(int i = 0; i < size; i++)
            System.out.print(i + 1 + "." + list.get(i).name() + "\t");
        alternative("", 1, size,"没有这个选项,请重新选择:");
        Employee employee = list.get(select - 1);
        long start, end;
        start = alternative(employee.name() + "从第几天开始值班:", 1, duration, "值班的开始时间不能为负数或者超过总天数");
        end = alternative(employee.name() + "值班到第几天:", (int) start, duration,"值班的结束时间不能小于开始天数或者超过总天数");
        if(roster.insertDuty(start, end, employee))
            System.out.println("安排成功!*************");
        else
            System.out.println("安排失败..............");
    }

    /**
     * 添加员工
     */
    public static void addEmployee(){
        String name, position;
        int phoneNum;
        System.out.println("添加员工的姓名:");
        name = sc.next();
        System.out.println("职位:");
        position = sc.next();
        phoneNum = alternative("电话号码:", 0, Integer.MAX_VALUE, "电话号码只能是数字");
        if(roster.addEmployee(new Employee(name, position, phoneNum)))
            System.out.println("添加成功!************");
        else
            System.out.println("添加失败,该员工已经存在............");
    }

    /**
     * 删除员工
     */
    public static void delEmployee(){
        String name, position;
        int phoneNum;
        System.out.println("要删除员工的姓名:");
        name = sc.next();
        System.out.println("职位:");
        position = sc.next();
        phoneNum = alternative("电话号码:", 0, Integer.MAX_VALUE, "电话号码只能是数字");
        Employee employee = new Employee(name, position, phoneNum);
        if(roster.delEmployee(employee))
            System.out.println("删除成功************");
        else
            System.out.println("删除失败,找不到该员工的信息............");
    }
}