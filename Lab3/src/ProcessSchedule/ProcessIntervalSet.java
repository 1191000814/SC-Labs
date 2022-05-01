package Lab3.src.ProcessSchedule;

import Lab3.src.interval.Interval;
import Lab3.src.interval.IntervalConflictException;
import Lab3.src.interval.LabelRepeatException;
import Lab3.src.interval.MultiIntervalSet;

import java.util.*;

/*
针对操作系统的进程调度管理系统，所需完成的功能为:
Step 1 增加一组进程，输入每个进程的ID、名称、最短执行时间、最长执行时间；进程一旦设定无法再修改其信息。
Step 2 当前时刻（设定为0）启动模拟调度，随机选择某个尚未执行结束的进程在CPU上执行（执行过程中其他进程不能被执行），并在该进程最大时间之前的任意时
刻停止执行，如果本次及其之前的累积执行时间已落到[最短执行时间，最长执行时间]的区间内，则该进程被设定为“执行结束”。重复上述过程，直到所有进程都达到
“执行结束”状态。在每次选择时，也可“不执行任何进程”，并在后续随机选定的时间点再次进行进程选择。
Step 3 上一步骤是“随机选择进程”的模拟策略，还可以实现“最短进程优先”的模拟策略：每次选择进程的时候，优先选择距离其最大执行时间差距最小的进程。
Step 4 可视化展示当前时刻之前的进程调度结果，以及当前时刻正在执行的进程。可视化的形式要直观明了，可自行设计。
 */
public class ProcessIntervalSet{
    private final MultiIntervalSet<String> intervalSet = new MultiIntervalSet<>();
    private final Map<Process, Integer> processMap = new HashMap<>(); // 进程列表及运行状况,不包括已经运行结束的进程
    private int presentTime = 0; // 当前时刻

    // constructor
    public ProcessIntervalSet(){}

    public void checkRep(){
        intervalSet.checkRep();
    }

    /**
     * 添加一个进程到进程列表
     *
     * @param process 添加的进程
     * @return 是否添加成功
     */
    public boolean add(Process process){
        if(processMap.containsKey(process)){
            System.out.println("已经添加过该进程");
            return false;
        }
        processMap.put(process, 0);
        checkRep();
        return true;
    }

    /**
     * @param select 选择如何运行一个进程 1:不运行, 2:随机运行, 3:选择最短时间的进程运行
     * @return 是否运行了一段进程
     */
    public boolean execute(int select){
        if(select != 1 && select != 2 && select != 3){
            System.out.println("select不在0-2之间");
            return false;
        }
        if(select == 1){
            System.out.println("本次运行不运行进程,直接退出");
            return false;
        }
        if(processMap.isEmpty()){
            System.out.println("进程列表中没有进程可以执行");
            return false;
        }
        List<Process> list = new ArrayList<>(processMap.keySet());
        Random r = new Random();
        Process process = list.get(r.nextInt(list.size())); // 首先从列表随机抽取一个进程
        if(select == 3){ // 如果需要选出剩余进程时间最短的进程
            for(Process p : list)
                if(p.minTime() - processMap.get(p) < process.minTime() - processMap.get(process))
                    process = p;
        }
        int executeTime = r.nextInt(process.maxTime()) + 1;
        // 随机决定本次运行时间
        try{
            if(processMap.get(process) + executeTime >= process.maxTime()){
                // 进程可以执行到结束并且超过最大时间
                executeTime = process.maxTime() - processMap.get(process);
                intervalSet.insert(new Interval<>(presentTime, presentTime + executeTime, process.toString()));
                presentTime += process.maxTime() - processMap.get(process);
                processMap.remove(process); // 删除进程
                System.out.println(process + " 时间: " + executeTime + " 进程结束");
            }
            else if(processMap.get(process) + executeTime >= process.minTime() && processMap.get(process) + executeTime <= process.maxTime()){
                // 进程可以执行到结束但不超过最大时间
                intervalSet.insert(new Interval<>(presentTime, presentTime + executeTime, process.toString()));
                presentTime += executeTime;
                processMap.remove(process); // 删除进程
                System.out.println(process + " 时间: " + executeTime + " 进程结束");
            }else{ // 进程不能执行到结束
                processMap.put(process, processMap.get(process) + executeTime);
                intervalSet.insert(new Interval<>(presentTime, presentTime + executeTime, process.toString()));
                presentTime += executeTime;
                System.out.println(process + " 时间: " + executeTime + " 进程未结束");
            }
        }catch(IntervalConflictException | LabelRepeatException e){
            System.out.println(e.toString());
        }
        checkRep();
        return true;
    }

    /**
     * 为所有的进程分配执行时间
     * @param select 运行方式 1:不运行, 2:随机运行, 3:选择最短时间的进程运行
     */
    public void runProcesses(int select){
        while(! processMap.isEmpty())
            execute(select);
        checkRep();
    }

    /**
     * @return 还没运行结束的进程与剩余时间的映射
     */
    public Map<Process, Integer> processMap(){
        return new HashMap<>(processMap);
    }

    /**
     * @return 可视化展示当前时刻之前的进程调度结果
     */
    @Override
    public String toString(){
        return intervalSet.toString();
    }
}