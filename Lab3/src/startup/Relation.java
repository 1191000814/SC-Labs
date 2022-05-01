package Lab3.src.startup;

// 两个字符串的关系
public class Relation{
    private final String strA; // 字符串A
    private final String strB; // 字符串B
    private final double similarity; // 关系程度,最多为1

    // AF: AF(strA, strB, similarity) = 字符串A和字符串B的相似度为similarity

    public Relation(String strA, String strB, double similarity){
        this.strA = strA;
        this.strB = strB;
        this.similarity = similarity;
    }

    public void checkRep(){
        assert similarity >= 0 && similarity <= 1;
    }

    public String strA(){
        return strA;
    }

    public String strB(){
        return strB;
    }

    public double similarity(){
        return similarity;
    }

    @Override
    public String toString(){
        return "[" + strA + "," + strB + "]" + "=" + similarity;
    }
}
