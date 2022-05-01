package Lab3.test.startup;

import Lab3.src.interval.Interval;
import Lab3.src.interval.IntervalConflictException;
import Lab3.src.interval.LabelRepeatException;
import Lab3.src.interval.MultiIntervalSet;
import Lab3.src.startup.Similarity;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class SimilarityTest{
    Similarity similarity;
    {
        try{
            similarity = new Similarity(new File("src/Lab3/test/startup/test"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testMatch(){
        Assert.assertEquals(1,similarity.match("happy", "happy"), 0.001);
        Assert.assertEquals(0.5, similarity.match( "happy", "meh"), 0.001);
    }

    @Test
    public void testSimilarity(){
        MultiIntervalSet<String> m1 = new MultiIntervalSet<>();
        MultiIntervalSet<String> m2 = new MultiIntervalSet<>();
        try{
            m1.insert(new Interval<>(0, 5, "happy"));
            m1.insert(new Interval<>( 10, 20, "sad"));
            m1.insert(new Interval<>(20, 25, "meh"));
            m1.insert(new Interval<>(25, 30, "sad"));
            m2.insert(new Interval<>(0 , 5, "meh"));
            m2.insert(new Interval<>(10 , 20, "sad"));
            m2.insert(new Interval<>(20 , 35, "happy"));
        }catch(IntervalConflictException | LabelRepeatException e){
            System.out.println(e.toString());
        }
        Assert.assertEquals(0.42857, similarity.similarity(m1, m2),0.001);
    }
}