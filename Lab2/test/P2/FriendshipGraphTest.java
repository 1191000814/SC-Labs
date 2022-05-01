package Lab2.test.P2;

import Lab2.src.P2.FriendshipGraph;
import Lab2.src.P2.Person;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

// test strategy: 给每个方法写出一个@Test函数用来专门测试该函数,以test + 函数名命名
public class FriendshipGraphTest {
    FriendshipGraph graph = new FriendshipGraph();
    Set<Person> vertices = new HashSet<>();

    Person rachel = new Person("Rachel");
    Person ross = new Person("Ross");
    Person ben = new Person("Ben");
    Person kramer = new Person("Kramer");

    @Test
    // 测试addVertex和vertices函数
    public void testAddVertex(){
        graph.addVertex(rachel);
        graph.addVertex(ross);
        vertices.add(rachel);
        vertices.add(ross);

        Assert.assertEquals(graph.vertices(), vertices);
    }

    @Test
    // 测试addEdge和getWeight函数
    public void testEdge(){
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addEdge(rachel, ross);

        Assert.assertEquals(graph.getWeight(rachel, ross), 1);
        Assert.assertEquals(graph.getWeight(rachel, rachel), 0);
        Assert.assertEquals(graph.getWeight(rachel, ben), -1);
        Assert.assertEquals(graph.getWeight(rachel, kramer), -1);
    }

    @Test
    /*测试getDistance函数*/
    public void testGetDistance(){
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);

        graph.addEdge(rachel, ross);
        graph.addEdge(ross, ben);
        graph.addEdge(rachel, ben);
        graph.addEdge(ben, kramer);
        graph.addEdge(kramer, rachel);
        assertEquals(graph.getDistance(rachel, ross), 1);
        assertEquals(graph.getDistance(rachel, ben), 1);
        assertEquals(graph.getDistance(rachel, rachel), 0);
        assertEquals(graph.getDistance(rachel, kramer), 2);
    }
}
