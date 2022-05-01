package Lab3.src.ProcessSchedule;

import java.util.Objects;

final public class Process{
    private final long id; // 进程ID
    private final String name; // 进程名称
    private final int minTime; // 最短运行时间
    private final int maxTime; // 最长运行时间

    /**
     * @param id 进程ID
     * @param name 进程名称
     * @param minTime 最小运行时间
     * @param maxTime 最大运行时间
     */
    public Process(long id, String name, int minTime, int maxTime){
        this.id = id;
        this.name = name;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    // checkRep
    public void checkRep(){
        assert id >= 0;
        assert ! name.equals("");
        assert minTime > 0;
        assert maxTime > minTime;
    }

    /**
     * @return 进程ID
     */
    public long id(){
        return id;
    }

    /**
     * @return 进程名称
     */
    public String name(){
        return name;
    }

    /**
     * @return 最小运行时间
     */
    public int minTime(){
        return minTime;
    }

    /**
     * @return 最大运行时间
     */
    public int maxTime(){
        return maxTime;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Process process = (Process)o;
        return id == process.id;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public String toString(){
        return "[进程名称: " + name + ", 进程id: " + id + "]";
    }
}