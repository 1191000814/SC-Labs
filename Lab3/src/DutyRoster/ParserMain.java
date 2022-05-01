package Lab3.src.DutyRoster;

import java.io.File;
import java.io.IOException;

public class ParserMain{
    public static void main(String[] args){
        ParserRoster parser = new ParserRoster();
        String s = "";
        try{
            s = parser.read(new File("src/Lab3/src/DutyRoster/test8.txt"));
        }catch(IOException e){
            e.printStackTrace();
        }
        parser.matchInit(s);
    }
}