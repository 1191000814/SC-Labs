package src.P3;

public class SocialNetwork{
    public static void main(String[] args){
        FriendshipGraph graph = new FriendshipGraph();

        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");
        Person ben = new Person("Ben");
        Person kramer = new Person("Kramer");

        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);

        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);

        System.out.println(graph.getDistance(rachel, ross)); //should print 1
        System.out.println(graph.getDistance(rachel, ben)); //should print 2
        System.out.println(graph.getDistance(rachel, rachel)); //should print 0
        System.out.println(graph.getDistance(rachel, kramer)); //should print -1
    }
}
