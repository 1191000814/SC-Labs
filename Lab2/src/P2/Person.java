package Lab2.src.P2;

import java.util.Objects;

public class Person{
    // AF AF(name) = 名字为name的人
    // RI name 不是null或者空字符串
    // Safety from rep exposure: 所有属性都是private和final没有任何get方法
    private final String name;

    //有参构造函数
    public Person(String name){
        this.name = name;
    }

    //重写了equals函数,当name相等时对象相等
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Person person = (Person)o;
        return Objects.equals(name,person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
