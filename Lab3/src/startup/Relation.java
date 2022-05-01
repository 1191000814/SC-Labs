package Lab3.src.startup;

// �����ַ����Ĺ�ϵ
public class Relation{
    private final String strA; // �ַ���A
    private final String strB; // �ַ���B
    private final double similarity; // ��ϵ�̶�,���Ϊ1

    // AF: AF(strA, strB, similarity) = �ַ���A���ַ���B�����ƶ�Ϊsimilarity

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
