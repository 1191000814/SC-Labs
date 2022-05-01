package Lab2.src.P1.graph;

/*
Edge类为不可变类,不可变类有如下属性:
1.不要提供任何可以修改对象属性的方法(没有setXXX方法)
2.用final修饰类保证类不会被扩展,没有子类
3.使所有的属性都是private 并且 final
4.防止别的对象访问不可变类的属性
5.确保对任何可变组件的互斥访问
6.如果有指向可变对象的属性,一定要确保,这个可变对象不能被其他类访问和修改(最好不要在不可变类中添加指向可变类的属性)
 */

/**
 * TODO specification
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 *
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
public final class Edge<L> {
    private final L source;
    private final L target;
    private final int weight;

    // Abstraction function:
    //     AF(source, target, weight) = a edge from 'source' to 'target', whose length is 'weight'
    // Representation invariant:
    //     source and target are both not empty, weight > 0
    // Safety from rep exposure:
    //     The Edge is a immutable class according to the designing specification

    // constructor
    public Edge(L start, L target, int weight){
        this.source = start;
        this.target = target;
        this.weight = weight;
    }

    // checkRep
    public void checkRep(){
        assert source != null;
        assert target != null;
        assert weight > 0;
    }

    public L source(){
        return source;
    }

    public L target(){
        return target;
    }

    public int weight(){
        return weight;
    }

    @Override
    public String toString(){
        return "[" + source + "," + target + "]";
    }

    /**
     * observer
     * @param vertex 图中的顶点名
     * @return 该点是否是该边的起点或终点
     */
    public boolean contain(L vertex){
        return (source.equals(vertex) || target.equals(vertex));
    }
}