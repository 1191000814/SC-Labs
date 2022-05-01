/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package Lab2.src.P1.graph;

import java.util.*;

/**
 * An implementation of Graph.
 *
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {
    private final List<Vertex<L>> vertices = new ArrayList<>();

    // Abstraction function:
    //     AF(vertices) = the directed graph made of vertices in the list named 'vertices'
    // Representation invariant:
    //     for the Vertex objects in 'vertices', the key of map in which must be contained in 'vertices',and the value > 0
    // Safety from rep exposure:
    //     all the field is private and final, though the field vertices is mutable, but all the methods which return the
    //     field use defensive copies

    // constructor
    public ConcreteVerticesGraph(){}

    // checkRap
    public void checkRep(){
        for(Vertex<L> v: vertices){
            v.checkRep();
            for(L name: v.map().keySet()){
                assert vertices.contains(new Vertex<>(name));
                assert v.map().get(name) > 0;
            }
        }
    }

    /**
     * constructor
     * @return 复制返回图中的vertices数组
     */
    public List<Vertex<L>> getVertices(){
        // defensive copies
        return new ArrayList<>(vertices);
    }

    /**
     * observer
     * @param source 源点
     * @param target 终点
     * @return 两点之间的距离,如无路径或者其中一点不存在则返回-1,两点相同则返回0
     */
    public int getWeight(L source, L target){
        if(source.equals(target))
            return 0;
        for(Vertex<L> vertex: vertices){
            try{
                if(vertex.name().equals(source))
                    return vertex.map().get(target);
            }catch(NullPointerException e){
                return -1;
            }
        }
        return -1;
    }

    /**
     * 向图中添加一个点
     *
     * mutator
     * @param vertex label for the new vertex
     * @return 是否添加成功
     */
    @Override
    public boolean add(L vertex){
        for(Vertex<L> v: vertices)
            if(v.name().equals(vertex))
                return false;
        vertices.add(new Vertex<>(vertex));
        checkRep();
        return true;
    }

    /**
     * 设置图中某条边的长度
     *
     * mutator
     * @param source label of the source vertex
     * @param target label of the target vertex
     * @param weight nonnegative weight of the edge
     * @return 该边的长度
     */
    @Override
    public int set(L source, L target, int weight){
        for(Vertex<L> vertex: vertices)
            if(vertex.name().equals(source)){
                vertex.setWeight(target, weight);
                checkRep();
                return weight;
            }
        checkRep();
        return -1;
    }

    /**
     * 移除图中的一条边
     *
     * mutator
     * @param vertex label of the vertex to remove
     * @return 是否移除成功
     */
    @Override
    public boolean remove(L vertex){
        checkRep();
        return vertices.remove(new Vertex<>(vertex));
    }

    /**
     * observer
     * @return 图中所有边组成的集合
     */
    @Override
    public Set<L> vertices(){
        Set<L> vertices = new HashSet<>();
        for(Vertex<L> v: this.vertices)
            vertices.add(v.name());
        // 这个只是添加this.name,而不是this,getVertices方法才是添加Vertex对象
        return vertices;
    }

    /**
     * observer
     * @param target a label
     * @return 所有能到达目标点的源点集合与对应长度组成的Map
     */
    @Override
    public Map<L, Integer> sources(L target){
        boolean isExist = false;
        for(Vertex<L> v : vertices) // 图中是否有target这个点
            if(v.name().equals(target)){
                isExist = true;
                break;
            }
        if(! isExist)
            return null;
        Map<L, Integer> sources = new HashMap<>();
        for(Vertex<L> v: vertices)
            if(v.map().get(target) != null){
                sources.put(v.name(), v.getWeight(target));
            }
        return sources;
    }

    /**
     * observer
     * @param source a label
     * @return 所有能由指定点达到的目标点集合与对应长度组成的Map
     */
    @Override
    public Map<L, Integer> targets(L source){
        boolean isExist = false;
        for(Vertex<L> v : vertices)
            if(v.name().equals(source)){
                isExist = true;
                break;
            }
        if(! isExist)
            return null;
        Map<L, Integer> targets = new HashMap<>();
        for(Vertex<L> v: vertices){
            if(v.name().equals(source)){
                targets.putAll(v.map());
                break;
            }
        }
        return targets;
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(Vertex<L> v: vertices)
            s.append(v.name()).append(" -> ").append(v.map()).append('\n');
        return s.toString();
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        ConcreteVerticesGraph<?> graph = (ConcreteVerticesGraph<?>)o;
        return Objects.equals(vertices, graph.vertices);
    }

    @Override
    public int hashCode(){
        return Objects.hash(vertices);
    }
}