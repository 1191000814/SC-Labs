package Lab2.src.P1.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * TODO specification
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 *
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
public final class Vertex<L> {
    private final L name;
    private final Map<L, Integer> map = new HashMap<>();

    // Abstraction function:
    //     AF(name, map) = the vertex named 'name', The distance between it and other vertices is mapped to 'map'
    // Representation invariant:
    //     name != null, every value in map must > 0
    // Safety from rep exposure:
    //     all the field is private and final, though the fields may be mutable, but all the methods
    //     which return the fields use defensive copies

    // constructor
    public Vertex(L name){
        this.name = name;
    }

    // checkRep
    public void checkRep(){
        assert name != null;
        for(L key: map.keySet())
            assert map.get(key) > 0;
    }

    public L name(){
        return name;
    }

    public Map<L, Integer> map(){
        // defensive copies
        return new HashMap<>(map);
    }

    public int getWeight(L target){
        return map.get(target);
    }

    public void setWeight(L target, int weight){
        this.map.put(target, weight);
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder(String.valueOf(name));
        s.append(" [");
        for(L s1: map.keySet())
            s.append(s1).append(" : ").append(map.get(s1)).append(", ");
        s.append("]");
        return s.toString();
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Vertex<?> vertex = (Vertex<?>)o;
        return Objects.equals(name, vertex.name);
    }

    @Override
    public int hashCode(){
        return Objects.hash(name);
    }
}