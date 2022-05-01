package Lab3.src.ProcessSchedule;

import Lab3.src.interval.MultiIntervalSet;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ProcessScheduleApp{
    static ProcessIntervalSet dispatcher = new ProcessIntervalSet();
    static Scanner sc = new Scanner(System.in);
    static int select;

    public static void main(String[] args){
        while(true){
            if(dispatcher.processMap().isEmpty()){
                System.out.println("没有进程可以运行,请先添加进程");
                addProcess();
            }
            menu();
            if(select == 1)
                execute();
            else if(select == 2)
                addProcess();
            else if(select == 3)
                System.out.println(dispatcher.toString());
            else if(select == 4){
                for(Process p : dispatcher.processMap().keySet())
                    System.out.println(p + ": " + dispatcher.processMap().get(p) + " " + p.minTime() + "~" + p.maxTime());
            }
            else{
                if(! dispatcher.processMap().isEmpty()){
                    alternative("还有进程未结束,是否终止程序\n1.仍然终止\t2.继续运行\n>", 1, 2, "没有这个选项请重新选择>");
                    if(select == 1)
                        System.exit(0);
                }
            }
            if(dispatcher.processMap().isEmpty()){
                alternative("当前进程已经全部结束,是否终止程序\n1.终止\t2.继续运行\n>", 1, 2, "没有这个选项请重新选择>");
                if(select == 1)
                    System.exit(0);
            }
        }
    }

    /**
     * 弹出菜单
     */
    public static void menu(){
        System.out.println("------------------------");
        alternative("下一步你想\n1.运行进程\t2.添加进程\t3.查看已运行的进程\t4.查看未运行的进程\t5.终止程序\n>", 1, 5, "没有这个选项,请重新选择>");
    }

    public static void execute(){
        select = alternative("以何种方式运行一个进程\n1.不运行进程\t2.随机选择\t3.选择距离结束时间最短的进程\n>", 1, 3, "没有这个选项,请重新选择>");
        // 随机选择进程执行
        if(dispatcher.execute(select))
            System.out.println("执行成功************");
        else
            System.out.println("执行失败............");
    }

    public static void addProcess(){
        int id, minTime, maxTime;
        String name;
        id = alternative("输入这个进程的id>", 0, Integer.MAX_VALUE, "id必须是正数>");
        System.out.print("进程名称>");
        name = sc.next();
        minTime = alternative("最短运行时间>", 0, Integer.MAX_VALUE, "最短时间必须是正整数>");
        maxTime = alternative("最长运行时间>", minTime + 1, Integer.MAX_VALUE, "最长时间必须是大于最短时间的正整数>");
        if(dispatcher.add(new Process(id, name, minTime, maxTime)))
            System.out.println("添加成功************");
        else
            System.out.println("添加失败,程序中不能存在id相同的进程............");
    }

    public static int alternative(String issue, int min, int max, String warning) throws InputMismatchException{
        System.out.print(issue);
        boolean inputMatch;
        do{
            String str = sc.next();
            try{
                inputMatch = true;
                select = Integer.parseInt(str);
            }catch(NumberFormatException e){
                System.out.println(e.toString());
                System.out.print("重新输入>");
                inputMatch = false;
            }
            if(inputMatch){
                if(select < min || select > max)
                    System.out.print(warning);
                else
                    break;
            }
        }while(true);
        return select;
    }
}