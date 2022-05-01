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
public class ConcreteEdgesGraph<L> implements Graph<L>{
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();

    // Abstraction function:
    //     AF(vertices, edges) = the directed graph made of vertex in vertices and edge in edges
    // Representation invariant:
    //     every Edge object in edges, its member value source and target must in vertices, the weight > 0
    // Safety from rep exposure:
    //     all the field is private and final, though the field vertices and edges is mutable, but all the methods
    //     which return the fields use defensive copies

    // constructor
    public ConcreteEdgesGraph(){}

    // checkRap
    public void checkRep(){
        for(Edge<L> edge: edges){
            edge.checkRep();
            assert vertices.contains(edge.source());
            assert vertices.contains(edge.target());
            assert edge.weight() > 0;
        }
    }

    /**
     * 根据源点和终点确定在图中的边长
     *
     * observer
     * @param source 源点
     * @param target 终点
     * @return 距离,如无边则返回-1
     */
    public int getWeight(L source, L target){
        for(Edge<L> edge: edges)
            if(edge.source().equals(source) && edge.target().equals(target))
                return edge.weight();
        return -1;
    }

    /**
     * 向图中添加一个节点
     *
     * mutator
     * @param vertex label for the new vertex
     * @return 是否添加成功
     */
    @Override
    public boolean add(L vertex){
        if(vertices.contains(vertex))
            return false;
        vertices.add(vertex);
        checkRep();
        return true;
    }

    /**
     * 在图中设置一条边的长度
     *
     * mutator
     * @param source label of the source vertex
     * @param target label of the target vertex
     * @param weight nonnegative weight of the edge
     * @return 如果添加成功返回边长,否则返回-1
     */
    @Override
    public int set(L source,L target,int weight){
        if(! vertices.contains(source) || ! vertices.contains(target) || weight <= 0){
            // 两个顶点有一个不存在,或者边值为非正数
            checkRep();
            return -1;
        }
        for(int i = 0; i < edges.size(); i++){
            // 已经存在这条边,只是修改这条边的长度,注意edge是immutable的,所以只能先remove再add
            if(edges.get(i).source().equals(source) && edges.get(i).target().equals(target) && edges.get(i).weight() != weight){
                edges.remove(edges.get(i));
                edges.add(new Edge<>(source, target, weight));
                checkRep();
                return weight;
            }
        }
        // 不存在这条边,加入这条边的对象
        edges.add(new Edge<>(source,target,weight));
        checkRep();
        return weight;
    }

    /**
     * 移除图中某个点,以及包含该点的所有边
     *
     * mutator
     * @param vertex label of the vertex to remove
     * @return 是否成功移除
     */
    @Override
    public boolean remove(L vertex){
        if(!vertices.contains(vertex))
            return false;
        vertices.remove(vertex);
        edges.removeIf(edge -> edge.contain(vertex));
        checkRep();
        return true;
    }

    /**
     * 获取图中所有的点
     *
     * observer
     * @return 所有点的Set集合
     */
    @Override
    public Set<L> vertices(){
        // defensive copies
        return new HashSet<>(vertices);
    }

    /**
     * 获取指向指定点的所有顶点(源顶点)以及对应边的长度
     *
     * observer
     * @param target a label
     * @return 源顶点以及对应的边的集合
     */
    @Override
    public Map<L, Integer> sources(L target){
        Map<L, Integer> sources = new HashMap<>();
        for(Edge<L> edge: edges)
            if(edge.target().equals(target))
                sources.put(edge.source(), edge.weight());
        return sources;
    }

    /**
     * 获取由指定点指向的所有顶点(目标顶点)以及对应边的长度
     *
     * observer
     * @param source a label
     * @return 目标顶点以及对应的边的集合
     */
    @Override
    public Map<L, Integer> targets(L source){
        Map<L, Integer> targets = new HashMap<>();
        for(Edge<L> edge: edges)
            if(edge.source().equals(source))
                targets.put(edge.target(), edge.weight());
        return targets;
    }

    // toString
    @Override
    public String toString(){
        StringBuilder graph = new StringBuilder();
        graph.append(vertices).append('\n');
        for(Edge<L> edge: edges)
            graph.append(edge.source()).append(" -> ").append(edge.target()).append(" : ").append(edge.weight()).append('\n');
        return graph.toString();
    }
}