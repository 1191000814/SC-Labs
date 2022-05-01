package test;

import static org.junit.Assert.*;

import org.junit.Test;
import src.P3.FriendshipGraph;
import src.P3.Person;

public class SocialNetworkTest {
    FriendshipGraph graph = new FriendshipGraph();
    FriendshipGraph testGraph = new FriendshipGraph();
    //graph为使用自己创建的函数处理的对象,testGraph为自己手动处理的对象
    Person rachel = new Person("Rachel");
    Person ross = new Person("Ross");
    Person ben = new Person("Ben");
    Person kramer = new Person("Kramer");

    @Test
    /*测试addVertex函数*/
    public void testAddVertex() {
        graph.addVertex(rachel);

        testGraph.pList.add(rachel);
        testGraph.num ++;
        testGraph.distance = new int[testGraph.num][testGraph.num];
        testGraph.distance[0][0] = Integer.MAX_VALUE / 2;
        int[][] newDistance = new int[testGraph.num][testGraph.num];
        for(int i = 0; i < testGraph.num; i++){
            for(int j = 0; j < testGraph.num; j++)
                if(i < testGraph.num-1 && j < testGraph.num-1)
                    newDistance[i][j] = testGraph.distance[i][j];
                else
                    newDistance[i][j] = Integer.MAX_VALUE /2;
        }
        testGraph.distance = newDistance;

        assertEquals(graph, testGraph);
    }

    @Test
    /*测试addEdge函数*/
    public void testEdge(){
        graph.addVertex(rachel);
        testGraph.addVertex(rachel);
        testGraph.addVertex(ross);
        graph.addVertex(ross);

        graph.addEdge(ross, rachel);
        graph.addEdge(rachel, ross);
        testGraph.addEdge(rachel, ross);
        testGraph.addEdge(ross, rachel);

        testGraph.distance[testGraph.pList.indexOf(ross)][testGraph.pList.indexOf(rachel)] = 1;
        testGraph.distance[testGraph.pList.indexOf(rachel)][testGraph.pList.indexOf(ross)] = 1;

        assertEquals(graph, testGraph);
    }

    @Test
    /*测试getDistance函数*/
    public void testGetDistance(){
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);

        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);

        assertEquals(graph.getDistance(rachel, ross), 1); //should print 1
        assertEquals(graph.getDistance(rachel, ben), 2); //should print 2
        assertEquals(graph.getDistance(rachel, rachel) ,0); //should print 0
        assertEquals(graph.getDistance(rachel, kramer), -1); //should print -1
    }
}