package Lab3.src.DutyRoster;

import java.util.Objects;

public final class Employee{
    private final String name; // 姓名
    private final String position; // 职位
    private final long phoneNum; // 电话号码

    // constructor
    public Employee(String name, String position, long phoneNum){
        this.name = name;
        this.position = position;
        this.phoneNum = phoneNum;
    }

    public void checkRep(){
        assert ! name.equals("");
        assert ! position.equals("");
        assert phoneNum > 0;
    }

    /**
     * @return 姓名
     */
    public String name(){
        return name;
    }

    /**
     * @return 职位
     */
    public String position(){
        return position;
    }

    /**
     * @return 电话号码
     */
    public long phoneNum(){
        return phoneNum;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee)o;
        return phoneNum == employee.phoneNum && Objects.equals(name, employee.name) && Objects.equals(position, employee.position);
    }

    @Override
    public int hashCode(){
        return Objects.hash(name, position, phoneNum);
    }

    @Override
    public String toString(){
        return name + " " + position + " " + phoneNum;
    }
}