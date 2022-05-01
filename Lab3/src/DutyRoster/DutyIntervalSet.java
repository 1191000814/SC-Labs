package Lab3.src.DutyRoster;

import Lab3.src.interval.*;

import java.util.*;

/*
针对排班管理系统，所需完成的功能为：
Step 1 设定排班开始日期、结束日期，具体到年月日即可。
Step 2 增加一组员工，包括他们各自的名字、职务、手机号码，并可随时删除某些员工。如果某个员工已经被编排进排班表，那么他不能被删除，必须将其排班信息删
掉之后才能删除该员工。员工信息一旦设定则无法修改。
Step 3 可手工选择某个员工、某个时间段（以“日”为单位，最小1天，可以是多天），向排班表增加一条排班记录，该步骤可重复执行多次。在该过程中，用户可随时
检查当前排班是否已满（即所有时间段都已被安排了特定员工值班）、若未满，则展示给用户哪些时间段未安排、未安排的时间段占总时间段的比例。
Step 4 除了上一步骤中手工安排，也可采用自动编排的方法，随机生成排班表。
Step 5 可视化展示任意时刻的排班表。可视化要直观明了，可自行设计。
 */
public class DutyIntervalSet{
    private final LinearIntervalSet<Employee> intervalSet = new LinearIntervalSet<>(); // 值班表
    private final Set<Employee> employeeSet = new HashSet<>(); // 还未安排的员工集合
    private final Calendar startDate; // 整个值班开始日期
    private final long duration; // 持续天数

    // AF: AF(intervalSet, employeeSet, startDate, duration)
    // = 值班表为intervalSet,职工集合为employeeSet,开始日期是startDate, 持续天数是duration
    // RI: intervalSet满足LinearIntervalSet的checkRep, duration > 0
    // Safety from rep exposure: 所有的属性都是private final,所有的get方法都使用了防御性复制
    /**
     * 构造函数
     * @param startDate 开工日期
     * @param duration 总时长
     */

    public DutyIntervalSet(Calendar startDate, long duration){
        this.startDate = startDate;
        this.duration = duration;
    }

    // checkRep
    public void checkRep(){
        intervalSet.checkRep();
        assert duration > 0;
    }

    /**
     * @return 开工日期
     */
    public Calendar startDate(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, startDate.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, startDate.get(Calendar.MONTH));
        calendar.set(Calendar.DATE, startDate.get(Calendar.DATE));
        return calendar;
    }

    /**
     * @return 总时长
     */
    public long duration(){
        return duration;
    }

    /**
     * 添加员工
     * @param employee 员工
     * @return 是否添加成功
     */
    public boolean addEmployee(Employee employee){
        boolean b = employeeSet.add(employee);
        checkRep();
        return b;
    }

    /**
     * 删除员工
     * @param employee 员工
     * @return 是否删除成功
     */
    public boolean delEmployee(Employee employee){
        if(employeeSet.remove(employee)){
            // 如果在未安排的员工集合中直接移除即可
            checkRep();
            return true;
        }
        boolean b = intervalSet.remove(employee);
        // 否则在值班表中移除,并移除相关值班记录
        checkRep();
        return b;
    }

    /**
     * 插入一条值班记录
     *
     * @param employee 员工
     * @param start 开始时间
     * @param end 结束时间
     * @return 是否插入成功
     */
    public boolean insertDuty(long start, long end, Employee employee){
        if(! employeeSet.contains(employee)){
            System.out.println("没有该员工的信息");
            return false;
        }
        try{
            intervalSet.insert(new Interval<>(start, end + 1, employee));
            // 结束时间是end + 1,因为天数是非连续的,而insert函数中使用是'左闭右开'区间
        }catch(IntervalConflictException | LabelRepeatException e){
            System.out.println(e.toString());
            return false;
        }
        employeeSet.remove(employee); // 添加成功即可删除其信息
        checkRep();
        return true;
    }

    /**
     * 对还没有开始分配的值班表进行自动分配值班表(随机分配)
     */
    public void autoInsert(){
        if(employeeSet.isEmpty()){
            System.out.println("没有员工,无法分配值班");
            return;
        }
        for(long day = 0; day < duration; day ++){
            if(checkTheDay(day) != null){
                System.out.println("已经开始手动分配了,无法自动分配");
                return;
            }
        }
        List<Employee> list = new ArrayList<>(employeeSet);
        Employee employee;
        // HashSet不具有随机性,使用list数组的的随机索引实现
        Random r = new Random();
        int num = r.nextInt((int) (Math.min(employeeSet.size(), duration))) + 1;
        int index = r.nextInt(list.size()); // 伪随机数index决定此次值班的员工
        // 伪随机数num决定由一共由多少个人来值班,num的取值范围是[1,a], a是天数和人数中较小的值
        List<Integer> dayList = new ArrayList<>();
        // 数组dayList决定了在第几天将要换人值班,换句话说,决定了值班人员的start
        // dayList有num - 1个数,将天数随机分成num份
        employee = list.get(index);
        if(num == 1){ // 如果num == 1,只要安排一个人值班
            insertDuty(0, duration - 1, employee);
            return;
        }
        for(int i = 0; i < num - 1; i ++)
            dayList.add(r.nextInt((int) duration));
        dayList.sort(Comparator.comparingInt(o -> o));
        // 利用随机索引数组,无序分配员工
        for(int i = 0; i < num; i ++){ // 分配第num次值班
            index = r.nextInt(list.size());
            if(i == 0){ // 第一个值班的员工要从第一天开始
                insertDuty(0, dayList.get(0) - 1, list.get(index));
                list.remove(index); // 每个员工值班后马上在list中删除
            }
            else if(i == num - 1){ //最后一个值班的员工要值班到最后一天结束
                insertDuty(dayList.get(num - 2), duration - 1, list.get(index));
                list.remove(index);
            }
            else{
                insertDuty(dayList.get(i - 1), dayList.get(i) - 1, list.get(index));
                list.remove(index);
            }
        }
        checkRep();
    }

    /**
     * 检查当前排班是否已满（即所有时间段都已被安排了特定员工值班）
     * 若未满,则展示给用户哪些时间段未安排、未安排的时间段占总时间段的比例
     * @param info 是否输出附加信息
     *
     * @return 当前值班表是否已经安排满
     */
    public boolean checkEveryday(boolean info){
        Set<Long> notArranged = new LinkedHashSet<>(); // 没有安排的日期集合(第几天)
        Calendar calendar = startDate;
        for(long day = 1; day <= duration; day ++){
            if(checkTheDay(day) == null) // 没有人值班
                notArranged.add(day);
        }
        if(notArranged.isEmpty()){
            System.out.println("排班已经全部安排满");
            return true;
        }
        if(info){
            System.out.println("未安排的天数有:");
            for(long day: notArranged){
                calendar.add(Calendar.DATE, 1);
                System.out.print(day + " " + calendar.get(Calendar.YEAR) + " " + (calendar.get(Calendar.MONTH) + 1));
                System.out.println(" " + calendar.get(Calendar.DATE));
            }
            System.out.println("已安排了全部天数的" + 100 * (1 - ((float)notArranged.size() / duration)) + "%");
        }
        return false;
    }

    /**
     * @param day 某一天
     * @return 这天是谁值班,若无人则返回null
     */
    public Employee checkTheDay(long day){
        for(Employee employee : intervalSet.labels())
            if(day >= intervalSet.getInterval(employee).start() && day < intervalSet.getInterval(employee).end())
                return employee;
        return null;
    }

    /**
     * @return 当前未安排的员工集合
     */
    public Set<Employee> employeeSet(){
        return new HashSet<>(employeeSet);
    }

    /**
     * @return 当前的值班表调度情况
     */
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        Calendar calendar = startDate(); // 复制开始日期
        for(long day = 0; day < duration; day ++){
            s.append(calendar.get(Calendar.YEAR)).append("年").append(calendar.get(Calendar.MONTH) + 1);
            s.append("月").append(calendar.get(Calendar.DATE)).append("日 ");
            if(checkTheDay(day) != null)
                s.append(checkTheDay(day)).append("\n");
            else
                s.append("无人值班\n");
            calendar.add(Calendar.DATE, 1);
        }
        return s.toString();
    }
}