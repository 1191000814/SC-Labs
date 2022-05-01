package src.P3;

import java.util.Objects;

public class Person{
    String name;

    //有参构造函数
    public Person(String name){
        this.name = name;
    }

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