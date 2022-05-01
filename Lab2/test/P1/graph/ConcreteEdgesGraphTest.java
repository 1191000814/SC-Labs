package Lab2.test.P1.graph;

import Lab2.src.P1.graph.ConcreteEdgesGraph;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// test strategy: 给每个方法写出一个@Test函数用来专门测试该函数,以test + 函数名命名
public class ConcreteEdgesGraphTest extends GraphInstanceTest{
    ConcreteEdgesGraph<String> graph = emptyInstance();
    Set<String> vertices = new HashSet<>();
    Map<String, Integer> map = new HashMap<>();

    @Override
    public ConcreteEdgesGraph<String> emptyInstance(){ //重写方法时返回值可以是原来类的子类
        return new ConcreteEdgesGraph<>();
    }

    @Test
    public void testAdd(){
        graph.add("成都");
        Set<String> vertices = new HashSet<>();
        vertices.add("成都");
        Assert.assertEquals(graph.vertices(), vertices);
    }

    @Test
    public void testSet(){
        graph.add("汉中");
        graph.add("长安");
        graph.set("汉中","长安",200);
        Assert.assertEquals(graph.getWeight("汉中","长安"), 200);
        Assert.assertEquals(graph.getWeight("绵竹","长安"), -1);
    }

    @Test
    public void testRemove(){
        graph.add("襄阳");
        graph.add("南郡");
        graph.add("樊城");
        graph.add("江陵");
        vertices.add("襄阳");
        vertices.add("南郡");
        vertices.add("樊城");
        vertices.add("江陵");
        Assert.assertEquals(graph.vertices(), vertices);
        graph.remove("江陵");
        vertices.remove("江陵");
        Assert.assertEquals(graph.vertices(), vertices);
    }

    @Test
    public void testVertices(){
        graph.add("洛阳");
        graph.add("许都");
        vertices.add("洛阳");
        vertices.add("许都");
        Assert.assertEquals(graph.vertices(), vertices);
    }

    @Test
    public void testSources(){
        graph.add("南阳");
        graph.add("宛城");
        graph.add("新野");
        graph.add("江夏");
        graph.set("宛城","南阳",1);
        graph.set("新野","南阳",2);
        map.put("宛城", 1);
        map.put("新野", 2);
        Assert.assertEquals(graph.sources("南阳"), map);
        graph.set("江夏","南阳",2);
        graph.set("宛城","南阳",2);
        map.put("宛城", 2);
        map.put("江夏", 2);
        Assert.assertEquals(graph.sources("南阳"), map);
    }

    @Test
    public void testTarget(){
        graph.add("长沙");
        graph.add("武陵");
        graph.add("桂阳");
        graph.add("零陵");
        graph.set("长沙","武陵",1);
        graph.set("长沙","桂阳",2);
        map.put("武陵", 1);
        map.put("桂阳", 2);
        Assert.assertEquals(graph.targets("长沙"), map);
        graph.set("长沙","零陵",2);
        graph.set("长沙","武陵",2);
        map.put("武陵", 2);
        map.put("零陵", 2);
        Assert.assertEquals(graph.targets("长沙"), map);
    }
}