package Lab3.src.DutyRoster;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaoyou
 * 从文件中读取信息,并用正则表达式解析文件,初始化dutyIntervalSet
 */
public class ParserRoster{
    DutyIntervalSet dutyIntervalSet;
    Pattern pattern;
    Matcher matcher;
    Pattern pWord = Pattern.compile("[a-zA-Z]+"); // 匹配名字
    Pattern pDigit = Pattern.compile("[0-9-]+"); // 匹配日期串

    /**
     * @param file 文本文件
     * @return 文本文件转化成的字符串
     * @throws IOException 文件不存在
     */
    public String read(File file) throws IOException{
        byte[] bytes = new byte[1024];
        StringBuilder string = new StringBuilder();
        FileInputStream fis;
        fis = new FileInputStream(file);
        int len;
        while((len = fis.read(bytes)) != -1) // 每次读1024个字节,直到不能读
            string.append(new String(bytes, 0, len));
        return string.toString();
    }

    /**
     * 把正则表达式中作为排班表的初始化内容
     * @param text 文本文件字符串
     */
    public void matchInit(String text){
        String stringMatch = "";

        /*------------------------匹配日期------------------------*/
        pattern = Pattern.compile("Period\\{[0-9,-]*}");
        matcher = pattern.matcher(text);
        if(matcher.find())
            stringMatch = matcher.group().substring(7,matcher.group().length() - 1);
        // stringMatch = 2021-01-10,2021-03-06
        List<Calendar> calendars = parserCalendar(stringMatch); // 把stringMatch转化成日期
        Calendar start = calendars.get(0);
        Calendar end = calendars.get(1);
        System.out.println("开始日期: " + start.get(Calendar.YEAR) + " " + start.get(Calendar.MONTH) + " " + start.get(Calendar.DATE));
        System.out.println("结束日期: " + end.get(Calendar.YEAR) + " " + end.get(Calendar.MONTH) + " " + end.get(Calendar.DATE));
        int duration = daysDifference(start, end); // 计算相差天数
        // 获取日期和总时长,初始化dutyIntervalSet的start和duration
        System.out.println(duration);
        dutyIntervalSet = new DutyIntervalSet(start, duration);

        /*------------------------匹配员工列表------------------------*/
        pattern = Pattern.compile("([a-zA-Z])+\\{([a-z A-Z])+,[0-9-]+}");
        matcher = pattern.matcher(text);
        while(matcher.find())
            dutyIntervalSet.addEmployee(parserEmployee(matcher.group()));
        System.out.println(dutyIntervalSet.employeeSet());

        /*------------------------匹配值班列表------------------------*/
        pattern = Pattern.compile("Roster\\{[\\S\\s]*}"); // 先定位到Roster
        matcher = pattern.matcher(text);
        if(matcher.find())
            stringMatch = matcher.group();
        pattern = Pattern.compile("[a-zA-Z]+\\{[0-9,-]+,[0-9,-]+}");
        matcher = pattern.matcher(stringMatch);
        while(matcher.find())
            parserRoster(matcher.group());
        System.out.println(dutyIntervalSet);
    }

    /**
     * @param s 字符串
     * @return 字符串转化成的整数(0-18位)
     */
    private long parserLong(String s) throws NumberFormatException{
        if(s.length() <= 10)
            return Integer.parseInt(s);
        if(s.length() > 20){
            System.out.println("不能转化20为以上的整数");
            return -1;
        }
        String s1 = s.substring(0, s.length() - 9); // 前九位(或者不到十位)
        String s2 = s.substring(s.length() - 9); // 后九位
        int i1 = Integer.parseInt(s1);
        int i2 = Integer.parseInt(s2);
        return (long) (i1 * Math.pow(10, 9) + i2);
    }

    /**
     * 将格式为"Name{Position,III-IIII-IIII}"的字符串转化为一个职工对象
     * @param s 字符串
     * @return 职工对象
     */
    private Employee parserEmployee(String s){
        Pattern p = Pattern.compile("([a-zA-Z])+\\{([a-z A-Z])+,[0-9-]+}");
        Matcher m = p.matcher(s);
        String name = "", position = "", phoneStr = "";
        long phoneNum = 0;
        if(! m.find()){
            System.out.println("员工信息\"" + s + "\"格式不匹配");
            return null;
        }
        m.reset(); // 重置匹配位置
        m = pWord.matcher(s);
        if(m.find()){
            name = m.group();
            if(name.equals("Period")) // 防止与Period的正则表达式混淆
                return null;
        }
        if(m.find())
            position = m.group();
        m = pDigit.matcher(s);
        if(m.find()){
            phoneStr = m.group().substring(0, 3) + m.group().substring(4, 8) + m.group().substring(9, 13);
            try{
                phoneNum = parserLong(phoneStr);
            }catch(NumberFormatException e){
                System.out.println(name + "的电话号码格式不对");
                System.out.println(e.toString());
                System.exit(0);
            }
        }
        return new Employee(name, position, phoneNum);
    }

    /**
     * 解析格式为"Name{20XX-XX-XX,20XX-XX-XX}"的字符串,安排值班
     * @param s 字符串
     */
    public void parserRoster(String s){
        Pattern p = Pattern.compile("[a-zA-Z]+\\{[0-9,-]+,[0-9,-]+}");
        Matcher m = p.matcher(s);
        if(! m.find()){
            System.out.println("格式不匹配");
            return;
        }
        String s1 = m.group();
        List<Calendar> calendars = parserCalendar(s1.substring(s1.length() - 22, s1.length() - 1)); // 开始日期和结束日期
        int start = daysDifference(dutyIntervalSet.startDate(), calendars.get(0)); // 第几天开始值班(第0天开始)
        int end = daysDifference(dutyIntervalSet.startDate(), calendars.get(1)); // 第几天结束值班
        String name = m.group().substring(0, s1.length() - 23);
        for(Employee e : dutyIntervalSet.employeeSet()){
            if(e.name().equals(name)){ // 在员工集合中找到了这个员工
                dutyIntervalSet.insertDuty(start, end, e);
                return;
            }
        }
        System.out.println("没有" + name + "这个员工");
    }

    /**
     * 将形如"20XX-XX-XX,20XX-XX-XX"类的字符串转化为两个日期类
     * @param s 字符串
     * @return 开始日期,结束日期
     */
    private List<Calendar> parserCalendar(String s){
        Pattern p = Pattern.compile("20[0-9][0-9]-[0-9][0-9]-[0-9][0-9],20[0-9][0-9]-[0-9][0-9]-[0-9][0-9]");
        Matcher m = p.matcher(s);
        if(! m.find()){
            System.out.println("日期\"" + s +  "\"格式不匹配");
            System.exit(0);
        }
        Calendar start = new GregorianCalendar(); // 开始时间
        Calendar end = new GregorianCalendar(); // 结束时间
        String date = m.group();
        start.set(Calendar.YEAR, Integer.parseInt(date.substring(0,4)));
        start.set(Calendar.MONTH, Integer.parseInt(date.substring(5,7)) - 1);
        start.set(Calendar.DATE, Integer.parseInt(date.substring(8,10)));
        end.set(Calendar.YEAR, Integer.parseInt(date.substring(11,15)));
        end.set(Calendar.MONTH, Integer.parseInt(date.substring(16,18)) - 1);
        end.set(Calendar.DATE, Integer.parseInt(date.substring(19,21)));
        List<Calendar> list = new ArrayList<>();
        list.add(start);
        list.add(end);
        return list;
    }

    /**
     * @param start 开始日期
     * @param end 结束日期
     * @return 相差天数
     */
    private int daysDifference(Calendar start, Calendar end){
        if(start.get(Calendar.YEAR) > end.get(Calendar.YEAR) || (start.get(Calendar.YEAR) < end.get(Calendar.YEAR) && start.get(Calendar.MONTH) > end.get(Calendar.MONTH)) ||
                start.get(Calendar.YEAR) < end.get(Calendar.YEAR) && start.get(Calendar.MONTH) < end.get(Calendar.MONTH) && start.get(Calendar.DATE) > end.get(Calendar.DATE)){
            System.out.println("开始日期在结束时期之后");
            System.exit(0);
        }
        Calendar c = makeCalendar(start);
        int duration;
        for(duration = 0; ; duration ++){
            if(c.get(Calendar.YEAR) == end.get(Calendar.YEAR) && c.get(Calendar.MONTH) == end.get(Calendar.MONTH) &&c.get(Calendar.DATE) == end.get(Calendar.DATE))
                break;
            c.add(Calendar.DATE, 1);
        }
        return duration;
    }

    /**
     * 讲一个日期的年月日复制到另一个日期上去
     * @param c 日期对象
     */
    private Calendar makeCalendar(Calendar c){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, c.get(Calendar.MONTH));
        calendar.set(Calendar.DATE, c.get(Calendar.DATE));
        return calendar;
    }
}