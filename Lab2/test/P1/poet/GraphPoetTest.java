package Lab2.test.P1.poet;

import Lab2.src.P1.poet.GraphPoet;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class GraphPoetTest{
    @Test
    public void testPoem() throws IOException{
        File file1 = new File("src\\Lab2\\test\\P1\\poet\\test1.txt");
        GraphPoet poet1 = new GraphPoet(file1);
        String poem1 = poet1.poem("Seek to explore new and exciting synergies!");
        String actualPoem1 = "Seek to explore strange new life and exciting synergies!";
        Assert.assertEquals(poem1, actualPoem1);

        File file2 = new File("src\\Lab2\\test\\P1\\poet\\test2.txt");
        GraphPoet poet2 = new GraphPoet(file2);
        String poem2 = poet2.poem("Test the system.");
        String actualPoem2 = "Test of the system.";
        Assert.assertEquals(poem2, actualPoem2);
    }
}