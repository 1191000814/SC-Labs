package Lab2.test.P1.graph;

import Lab2.src.P1.graph.ConcreteVerticesGraph;
import org.junit.Assert;
import org.junit.Test;
import Lab2.src.P1.graph.Vertex;

import java.util.*;

// test strategy: 给每个方法写出一个@Test函数用来专门测试该函数,以test + 函数名命名
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    ConcreteVerticesGraph<String> graph = emptyInstance();
    List<Vertex<String>> vertices = new ArrayList<>();

    @Override
    public ConcreteVerticesGraph<String> emptyInstance(){ // 重写方法时返回值可以是原来类的子类
        return new ConcreteVerticesGraph<>();
    }

    @Test
    public void testAdd(){
        graph.add("逍遥津");
        vertices.add(new Vertex<>("逍遥津"));
        Assert.assertEquals(graph.getVertices(), vertices);
    }

    @Test
    public void testSet(){
        graph.add("寿春");
        graph.add("合淝");
        graph.add("丹阳");
        Assert.assertEquals(graph.set("寿春","合淝",1),1);
        Assert.assertEquals(graph.set("寿春","丹阳",2),2);
        Assert.assertEquals(graph.getWeight("寿春","合淝"),1);
        Assert.assertEquals(graph.getWeight("寿春","丹阳"),2);
    }

    @Test
    public void remove(){
        graph.add("庐江");
        graph.add("舒城");
        graph.add("牛渚");
        vertices.add(new Vertex<>("庐江"));
        vertices.add(new Vertex<>("舒城"));
        vertices.add(new Vertex<>("牛渚"));
        Assert.assertEquals(graph.getVertices(), vertices);
        graph.remove("庐江");
        vertices.remove(new Vertex<>("庐江"));
        Assert.assertEquals(graph.getVertices(), vertices);
    }

    @Test
    public void testVertices(){
        graph.add("六安");
        graph.add("濡须");
        Set<String> vertices1 = new HashSet<>();
        vertices1.add("六安");
        vertices1.add("濡须");
        Assert.assertEquals(graph.vertices(), vertices1);
    }

    @Test
    public void testSources(){
        graph.add("西津渡");
        graph.add("嘉兴");
        graph.add("余杭");
        graph.set("西津渡","余杭",1);
        graph.set("嘉兴","余杭",2);
        Map<String, Integer> sources = new HashMap<>();
        sources.put("西津渡", 1);
        sources.put("嘉兴", 2);
        Assert.assertEquals(graph.sources("余杭"), sources);
    }

    @Test
    public void testTarget(){
        graph.add("建业");
        graph.add("乌程");
        graph.add("会稽");
        graph.set("建业","乌程",1);
        graph.set("建业","会稽",2);
        Map<String, Integer> targets = new HashMap<>();
        targets.put("乌程", 1);
        targets.put("会稽", 2);
        Assert.assertEquals(graph.targets("建业"), targets);
    }
}
