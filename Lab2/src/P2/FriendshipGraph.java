package Lab2.src.P2;

import Lab2.src.P1.graph.ConcreteVerticesGraph;
import Lab2.src.P1.graph.Vertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FriendshipGraph{
    // AF: AF(graph) = 词亲和图
    // RI: graph中的元素满足在ConCreteVerticesGraph中的RI即可
    // Safety from rep exposure: 每个返回可变类对象的方法都使用了防御性复制
    // 类ConcreteVerticesGraph的checkRap方法足以检查成员变量graph的合法性
    ConcreteVerticesGraph<Person> graph = new ConcreteVerticesGraph<>();

    // constructor
    public FriendshipGraph(){}

    /**
     * observer
     * @return FriendshipGraph中的点组成的集合
     */
    public Set<Person> vertices(){
        // defensive copies
        return new HashSet<>(graph.vertices());
    }

    /**
     * 向图中增加一个点
     *
     * mutator
     * @param p 增加的点对象
     */
    public void addVertex(Person p){
        graph.add(p);
    }

    /**
     * 向图中增加一条边
     *
     * mutator
     * @param p1 源点
     * @param p2 终点
     */
    public void addEdge(Person p1, Person p2){
        graph.set(p1, p2, 1);
    }

    /**
     * observer
     * @param pSource 源点
     * @param pTarget 终点
     * @return 源点到终点的直接距离
     */
    public int getWeight(Person pSource, Person pTarget){
        return graph.getWeight(pSource, pTarget);
    }

    /**
     * 用Dijkstra算法求最短路径
     *
     * observer
     * @param pBegin 对象1
     * @param pEnd 对象2
     * @return 两个对象之间最短距离
     */
    public int getDistance(Person pBegin, Person pEnd){
        if(! graph.vertices().contains(pBegin) || ! graph.vertices().contains(pEnd))
            return -1;
        if(pBegin.equals(pEnd)) // 两点相同,距离为0
            return 0;
        Set<Person> unchecked = new HashSet<>(); // 未被选中的点集合
        Set<Person> checked = new HashSet<>(); // 已经选中的点集合
        Map<Person, Integer> lestDistance = new HashMap<>(); // 起始点与其他点的最短距离的映射
        checked.add(pBegin); // 开始点添加到已选集合

        for(Vertex<Person> v: graph.getVertices()) // 其他点添加到未选集合,没有边相连接的点不添加
            if(! v.name().equals(pBegin) && (! graph.sources(v.name()).isEmpty() || ! graph.targets(v.name()).isEmpty()))
                unchecked.add(v.name());

        Person current; // 当前变量
        Person next = unchecked.iterator().next(); // 下一个要找的点

        if(! unchecked.contains(pEnd)) // 如果目标点不在unchecked中,则无路径可达,返回-1
            return -1;

        for(Person p: unchecked){ // 初始化最短距离映射
            try{
                if(graph.getWeight(pBegin, p) != -1)
                    lestDistance.put(p, graph.getWeight(pBegin, p));
                else // 如果两点之间没有路径则设置为无穷大,不设置为最大整数是为了防止溢出
                    lestDistance.put(p, Integer.MAX_VALUE / 2);
            }catch(NullPointerException e){
                lestDistance.put(p, Integer.MAX_VALUE / 2);
            }
        }

        while(! unchecked.isEmpty()){ // 直到所有点均被选择才结束循环

            for(Person p: unchecked) // 选中unchecked中离checked集合距离最小的点作为下一个点next
                if(minDistance(checked, p) < minDistance(checked, next))
                    next = p;

            current = next;
            unchecked.remove(current); // 移除下个点
            checked.add(current); // 将当前点添加至checked
            if(unchecked.iterator().hasNext())
                next = unchecked.iterator().next(); // 随机从未选中的点中选择一个为next

            for(Person p: unchecked) // 更新unchecked中各个顶点与起点的距离
                if(graph.getWeight(current, p) != -1 && lestDistance.get(current) + graph.getWeight(current, p) < lestDistance.get(p))
                    lestDistance.put(p, lestDistance.get(current) + graph.getWeight(current, p));
        }

        return lestDistance.get(pEnd) >= Integer.MAX_VALUE / 2 ? -1 : lestDistance.get(pEnd);
    }

    /**
     * 选出一个点集合中离一个点最近的距离
     *
     * observer
     * @param pSet 点集合
     * @param p 点
     * @return 点集合中里该点最近的距离
     */
    private int minDistance(Set<Person> pSet, Person p){
        int distance = Integer.MAX_VALUE;
        for(Person p0: pSet)
            if(graph.getWeight(p0, p) < distance)
                distance = graph.getWeight(p0, p);
        return distance == -1 ? Integer.MAX_VALUE / 2 : distance;
    }
}