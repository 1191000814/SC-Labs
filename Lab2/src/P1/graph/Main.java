package Lab2.src.P1.graph;
// 模拟客户端
public class Main{
    public static void main(String[] args){
        // ConcreteEdgesGraph
        ConcreteEdgesGraph<String> edgesGraph = new ConcreteEdgesGraph<>();
        edgesGraph.add("咸阳");
        edgesGraph.add("大梁");
        edgesGraph.add("新郑");
        edgesGraph.add("邯郸");
        edgesGraph.set("咸阳","新郑",100);
        edgesGraph.set("咸阳","大梁",200);
        edgesGraph.set("咸阳","邯郸",300);
        edgesGraph.set("新郑","大梁",150);
        edgesGraph.set("大梁","邯郸",200);
        edgesGraph.set("咸阳","" ,100);
        edgesGraph.set("咸阳", "大梁",175);
        System.out.println(edgesGraph.getWeight("咸阳","大梁"));
        System.out.println(edgesGraph.vertices());
        System.out.println(edgesGraph.sources("大梁"));
        System.out.println(edgesGraph.targets("咸阳"));
        edgesGraph.remove("新郑");
        System.out.println(edgesGraph);

        // ConcreteVerticesGraph
        ConcreteVerticesGraph<String> verticesGraph = new ConcreteVerticesGraph<>();
        verticesGraph.add("蓟城");
        verticesGraph.add("临淄");
        verticesGraph.add("郢都");
        verticesGraph.add("寿春");
        verticesGraph.set("蓟城","临淄",300);
        verticesGraph.set("临淄","郢都",500);
        verticesGraph.set("临淄","寿春",400);
        verticesGraph.set("寿春","郢都",200);
        System.out.println(verticesGraph.getVertices());
        System.out.println(verticesGraph.getWeight("蓟城","临淄"));
        System.out.println(verticesGraph.sources("郢都"));
        System.out.println(verticesGraph.targets("临淄"));
        System.out.println(verticesGraph.vertices());
        verticesGraph.remove("蓟城");
        System.out.println(verticesGraph);
    }
}
