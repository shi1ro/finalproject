/*package hlw;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;

public class ResourceTest //creature and map synchronization test
{
    void newpic(String str)
    {
        try
        {
        File tmp = new File(str);
        if(!tmp.exists())
        {
            System.out.println(str + " picture lose!");
            assertTrue(false);
        }
        }
        catch(Exception e)
        {
            System.out.println("IO ERROR");
            assertTrue(false);
        }
    }
     @Test
    public void ImageTest()
    {
    /*    File directory = new File("");
        String mainpath = "";
        try
        {
            mainpath += directory.getCanonicalPath() ;
        }
        catch(Exception e)
        {
            System.out.println("IO ERROR!");
            assertTrue(false);
        }*/
   /*     String mainpath = "File:/"+this.getClass().getResource("").getPath()+"/pic/";
        newpic(mainpath+"battlefield.jpg");
        newpic(mainpath+"battlefieldborder.jpg");
        newpic(mainpath+"attack.jpg");
        newpic(mainpath+"boom.jpg");
        newpic(mainpath+"frog.jpg");
        newpic(mainpath+"gnp.jpg");
        newpic(mainpath+"hlw1.jpg");
        newpic(mainpath+"hlw2.jpg");
        newpic(mainpath+"hlw3.jpg");
        newpic(mainpath+"hlw4.jpg");
        newpic(mainpath+"hlw5.jpg");
        newpic(mainpath+"hlw6.jpg");
        newpic(mainpath+"hlw7.jpg");
        newpic(mainpath+"ice.jpg");
        newpic(mainpath+"poison.jpg");
        newpic(mainpath+"scor.jpg");
        newpic(mainpath+"snake.jpg");
        assertTrue(true);
        System.out.println("picture resource test pass!");
    }*/
/*    @Test

    public void directoryRecTest()
    {
        String mainpath = this.getClass().getResource("").getPath()+"/rec/";
        try
        {
            File tmp = new File(mainpath);
            if(!(tmp.exists()) || !(tmp.isDirectory()))
            {
                System.out.println("Rec file folder not exist!please create a Rec file folder in classpath.!");
                assertTrue(false);     
            }
        }
        catch(Exception e)
        {
            System.out.println("IO ERROR!");
            assertTrue(false);
        }
        assertTrue(true);
        System.out.println("Rec file folder exist!test pass!");
    }
}*/