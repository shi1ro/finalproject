package hlw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.lang.Runnable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.image.*;

public class App {
    ArrayList<Creature> arrcreas;
    Map<String,Integer> mapstrint = new HashMap<String,Integer>();

    BattleMap map;
    Control hlwcontrol;
     final int frognum = 10;
     final int group1num = 8;
     final int creanum = 20;
     int runspeed = 500;
     int state = 0;
     int contracttime = 50;
     int roundtime = 0;
     String recordthisround = "";
     String warnings = "";
     StackPane battlesp = new StackPane();
     StackPane bordersp = new StackPane();
     StackPane normaltextsp = new StackPane();
     StackPane warningtextsp = new StackPane();
     StackPane creasp[] = new StackPane[creanum];
     StackPane fireeffsp[] = new StackPane[creanum/2];
     StackPane iceeffsp[] = new StackPane[creanum/2];
     StackPane poisoneffsp[] = new StackPane[creanum];
     StackPane atteffsp[] = new StackPane[creanum/2];

     Group p = new Group();
     Scene scene = new Scene(p);
     Stage stg;
     ImageView imvbattle;
     ImageView imvborder;
     ImageView imvhlw1;
     ImageView imvhlw2;
     ImageView imvhlw3;
     ImageView imvhlw4; 
     ImageView imvhlw5;
     ImageView imvhlw6;
     ImageView imvhlw7;
     ImageView imvgnp; 
     ImageView imvscor;
     ImageView imvsnake;
     ImageView imvfrogs[] = new ImageView[frognum];
     ImageView imvfires[] = new ImageView[creanum/2];
     ImageView imvices[] = new ImageView[creanum/2];
     ImageView imvpoisons[] = new ImageView[creanum];
     ImageView imvattacks[] = new ImageView[creanum/2];

     Text battlenormalrecord = new Text("Welcome!\npress space to begin fight\npress 'L' to load a fight");
     Text battlewarningrecord = new Text("");

     Canvas canvas = new Canvas(1000, 1000);
     GraphicsContext gc = canvas.getGraphicsContext2D();


     BufferedWriter fileout;
     BufferedReader filein;

     String identitify = "hlwbattle record,Made By shi1ro";
     File fo;
     String dirrec;
     void initDirRec()
     {
        dirrec = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        dirrec = dirrec.substring(0, dirrec.lastIndexOf("/",dirrec.length()-2));
        dirrec += "/rec/";
        File dir = new File(dirrec);
        if(!dir.exists())
        {
            dir.mkdir();
        }
     }
     void initImageView()
     {
        imvbattle = new ImageView(new Image(getClass().getResourceAsStream("/pic/battlefield.jpg")));
        imvborder = new ImageView(new Image(getClass().getResourceAsStream("/pic/battlefieldborder.jpg")));
        imvborder.setFitHeight(1000);
        imvborder.setCache(true);
        imvborder.setFitWidth(1000);
        imvhlw1 = new ImageView(new Image(getClass().getResourceAsStream("/pic/hlw1.jpg")));
        imvhlw1.setFitHeight(100);
        imvhlw1.setFitWidth(100);
        imvhlw1.setCache(true);
        imvhlw2 = new ImageView(new Image(getClass().getResourceAsStream("/pic/hlw2.jpg")));
        imvhlw2.setFitHeight(100);
        imvhlw2.setFitWidth(100);
        imvhlw2.setCache(true);
        imvhlw3 = new ImageView(new Image(getClass().getResourceAsStream("/pic/hlw3.jpg")));
        imvhlw3.setFitHeight(100);
        imvhlw3.setFitWidth(100);
        imvhlw3.setCache(true);
        imvhlw4 = new ImageView(new Image(getClass().getResourceAsStream("/pic/hlw4.jpg")));
        imvhlw4.setFitHeight(100);
        imvhlw4.setFitWidth(100);
        imvhlw4.setCache(true);
        imvhlw5 = new ImageView(new Image(getClass().getResourceAsStream("/pic/hlw5.jpg")));
        imvhlw5.setFitHeight(100);
        imvhlw5.setFitWidth(100);
        imvhlw5.setCache(true);
        imvhlw6 = new ImageView(new Image(getClass().getResourceAsStream("/pic/hlw6.jpg")));
        imvhlw6.setFitHeight(100);
        imvhlw6.setFitWidth(100);
        imvhlw6.setCache(true);
        imvhlw7 = new ImageView(new Image(getClass().getResourceAsStream("/pic/hlw7.jpg")));
        imvhlw7.setFitHeight(100);
        imvhlw7.setFitWidth(100);
        imvhlw7.setCache(true);
        imvgnp = new ImageView(new Image(getClass().getResourceAsStream("/pic/gnp.jpg")));
        imvgnp.setFitHeight(100);
        imvgnp.setFitWidth(100);
        imvgnp.setCache(true);
        imvscor = new ImageView(new Image(getClass().getResourceAsStream("/pic/scor.jpg")));
        imvscor.setFitHeight(100);
        imvscor.setFitWidth(100);
        imvscor.setCache(true);
        imvsnake = new ImageView(new Image(getClass().getResourceAsStream("/pic/snake.jpg")));
        imvsnake.setFitHeight(100);
        imvsnake.setFitWidth(100);
        imvsnake.setCache(true);
        for(int i = 0;i < frognum;i++)
        {
            imvfrogs[i] = new ImageView(new Image(getClass().getResourceAsStream("/pic/frog.jpg")));
            imvfrogs[i].setFitHeight(100);
            imvfrogs[i].setFitWidth(100);
            imvfrogs[i].setCache(true);
        }
        for(int i = 0;i < creanum/2;i++)
        {
            imvfires[i] = new ImageView(new Image(getClass().getResourceAsStream("/pic/boom.jpg")));
            imvfires[i].setFitHeight(100);
            imvfires[i].setFitWidth(100);
            imvfires[i].setCache(true);
        }
        for(int i = 0;i < creanum/2;i++)
        {
            imvices[i] = new ImageView(new Image(getClass().getResourceAsStream("/pic/ice.jpg")));
            imvices[i].setFitHeight(100);
            imvices[i].setFitWidth(100);
            imvices[i].setCache(true);
        }
        for(int i = 0;i < creanum;i++)
        {
            imvpoisons[i] = new ImageView(new Image(getClass().getResourceAsStream("/pic/poison.jpg")));
            imvpoisons[i].setFitHeight(100);
            imvpoisons[i].setFitWidth(100);
            imvpoisons[i].setCache(true);
        }
        for(int i = 0;i < creanum/2;i++)
        {
            imvattacks[i] = new ImageView(new Image(getClass().getResourceAsStream("/pic/attack.jpg")));
            imvattacks[i].setFitHeight(100);
            imvattacks[i].setFitWidth(100);
            imvattacks[i].setCache(true);
        }
     }
     void initStackPane()//static init,in order of creatures initplace(i) in arraylist
     {
        battlesp.getChildren().add(imvbattle);
        battlesp.setLayoutX(0);
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        canvas.setVisible(true);
        battlesp.setLayoutY(0);
        bordersp.getChildren().add(imvborder);
        bordersp.setLayoutX(0);
        bordersp.setLayoutY(0);
        bordersp.setVisible(false);
        for(int i = 0;i < creanum;i++)
            creasp[i] = new StackPane();
        creasp[0].getChildren().add(imvgnp);
        creasp[1].getChildren().add(imvhlw1);
        creasp[2].getChildren().add(imvhlw2);
        creasp[3].getChildren().add(imvhlw3);
        creasp[4].getChildren().add(imvhlw4);
        creasp[5].getChildren().add(imvhlw5);
        creasp[6].getChildren().add(imvhlw6);
        creasp[7].getChildren().add(imvhlw7);
        for(int i = group1num;i < group1num+frognum;i++)
        {
            creasp[i].getChildren().add(imvfrogs[i-group1num]);
        }
        creasp[18].getChildren().add(imvscor);
        creasp[19].getChildren().add(imvsnake);
        for(int i = 0;i < creanum;i++)
        {
           creasp[i].setVisible(false);
        }
        for(int i = 0;i < creanum/2;i++)
        {
            fireeffsp[i] = new StackPane();
            fireeffsp[i].getChildren().add(imvfires[i]);
            fireeffsp[i].setVisible(false);
            iceeffsp[i] = new StackPane();
            iceeffsp[i].getChildren().add(imvices[i]);
            iceeffsp[i].setVisible(false);
            atteffsp[i] = new StackPane();
            atteffsp[i].getChildren().add(imvattacks[i]);
            atteffsp[i].setVisible(false);
        }
        for(int i = 0;i < creanum;i++)
        {
            poisoneffsp[i] = new StackPane();
            poisoneffsp[i].getChildren().add(imvpoisons[i]);
            poisoneffsp[i].setVisible(false);
        }
     }
     void initText()
     {
        battlenormalrecord.setFont(Font.font(null,20));
        battlenormalrecord.setStroke(Color.BLACK);
        battlenormalrecord.setLayoutX(1020);
        battlenormalrecord.setLayoutY(20);
        battlewarningrecord.setLayoutX(1020);
        battlewarningrecord.setLayoutY(720);
        battlewarningrecord.setFont(Font.font(null, 15));
        battlewarningrecord.setStroke(Color.RED);

     }
     void initBattleData()
     {
         roundtime = 0;
         recordthisround = "";
         warnings = "";
         contracttime = 50;
         arrcreas = new ArrayList<Creature>();
         map = new BattleMap(arrcreas);
         hlwcontrol = new Control(arrcreas,map);
     }
     
     void imageViewReset()
     {
         for(int i = 0;i < creanum;i++)
         {
            creasp[i].getChildren().get(0).setStyle("-fx-opacity: 1;");
            creasp[i].setVisible(true);
         }
         gc.clearRect(0,0,1000,1000);
     }
     void updateSPPlace()
     {
        for(int i = 0;i < creanum;i++)
        {
            if(!(arrcreas.get(i)).getLifeState())
            {
                creasp[i].getChildren().get(0).setStyle("-fx-opacity: 0.2;");
                continue;
            }
            int x = (arrcreas.get(i)).creapoi.x;
            int y = (arrcreas.get(i)).creapoi.y;
            if(x < 0 || x > 9 || y < 0 || y > 9)
                continue;
            creasp[i].setLayoutX(x * 100);
            creasp[i].setLayoutY(y * 100);
        }   
     }
     void updateNormalRec(String b)
     {
         recordthisround = recordthisround + b;
         Platform.runLater(new Thread(new UpdateTextThread(recordthisround,battlenormalrecord)));
     }
     void updateWarningRec(String b)
     {
         warnings = warnings + b;
         Platform.runLater(new Thread(new UpdateTextThread(warnings,battlewarningrecord)));
     }
    
    
     void effectShow(String battlerecord)
     {
        if(battlerecord.length() == 0)
        {
            try{Thread.sleep(runspeed);}catch(Exception exce){exce.printStackTrace();}
            return;
        }
        int firenum = 0;
        int icenum = 0;
        int poinum = 0;
        int attnum = 0;
        int effnum = 0;
        ArrayList<TestEffectThread> arreffthread = new ArrayList<TestEffectThread>();
        String[] recordarr = battlerecord.split("\n");
        for(String rcd:recordarr)
        {
            String[] rcdunits = rcd.split(" ");
            if(rcd.indexOf("distant") == -1)// not have distant 
            {
                if(rcdunits[0].indexOf("Area") == -1 && rcdunits[2].indexOf("defeat") != -1)//not have area but have defeat
                {
                    char[] tmpchararr = rcdunits[4].toCharArray();
                    int x = tmpchararr[1] - '0';
                    int y = tmpchararr[3] - '0';
                    if(x >= 0 && x <= 9 && y >= 0 && y <= 9)
                        arreffthread.add(new TestEffectThread(atteffsp[attnum++],effnum++,new Point(x * 100,y * 100),runspeed/2));
                }
                else if(rcdunits[0].indexOf("Area") != -1)//near fight
                {
                    char[] tmpchararr = rcdunits[2].toCharArray();
                    int x = tmpchararr[1] - '0';
                    int y = tmpchararr[3] - '0';
                    if(x >= 0 && x <= 9 && y >= 0 && y <= 9)
                        arreffthread.add(new TestEffectThread(poisoneffsp[poinum++],effnum++,new Point(x * 100,y * 100),runspeed/2));
                }
            }
            else //distant
            {
                char[] tmpchararr = rcdunits[5].toCharArray();
                int x = tmpchararr[1] - '0';
                int y = tmpchararr[3] - '0';
                if(x >= 0 && x <= 9 && y >= 0 && y <= 9)
                {
                    if(rcdunits[0].indexOf("FourthHlw") != -1)
                    {
                        arreffthread.add(new TestEffectThread(fireeffsp[firenum++],effnum++,new Point(x * 100,y * 100),runspeed/2));
                    }
                    else if(rcdunits[0].indexOf("FifthHlw") != -1)
                    {
                        arreffthread.add(new TestEffectThread(iceeffsp[icenum++],effnum++,new Point(x * 100,y * 100),runspeed/2));
                    }
                    else if(rcdunits[0].indexOf("Snake") != -1)
                    {
                        arreffthread.add(new TestEffectThread(poisoneffsp[poinum++],effnum++,new Point(x * 100,y * 100),runspeed/2));
                    }
                }
            }
        }
        EffectThread.setThreadNum(arreffthread.size());
        Thread thread[] = new Thread[arreffthread.size()];
        for(int i = 0;i < arreffthread.size();i++)
        {
            thread[i] = new Thread(arreffthread.get(i));
        }
        for(int i = 0;i < arreffthread.size();i++)
        {
            TestEffectThread.mode = 1;
            Platform.runLater(thread[i]);
        }
        try{Thread.sleep(runspeed);}catch(Exception exce){exce.printStackTrace();}
        for(int i = 0;i < arreffthread.size();i++)
        {
            TestEffectThread.mode = 0;
            Platform.runLater(thread[i]);
        }
        try{Thread.sleep(runspeed);}catch(Exception exce){exce.printStackTrace();}
    }
    
    
    void fileOutInit() throws FileNotFoundException,IOException
    {
        fo = new File(dirrec + "record"+System.currentTimeMillis()+".txt");
        fileout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fo)));
        String tmp = identitify + "\n";
        tmp += "Place Initial\n";
        for(Creature crea:arrcreas)
        {
            tmp = tmp + crea + "\n";
        }
        tmp += "Initial End\n";
        fileout.write(tmp);
    } 
    Point strToPoint(String str)
    {
        if(str.length() != 5)
        {
            return new Point(-1,-1);
        }
        char[] tmp = str.toCharArray();
        int x = tmp[1] - '0';
        int y = tmp[3] - '0';
        return new Point(x,y);
    }
   
    boolean fileinPlaceInit() throws FileNotFoundException,IOException //static init in order to arrcreas(i)
    {
        ArrayList<String> tmparrstr = new ArrayList<String>();
        String tmp = "";
        tmp = filein.readLine();
        if(!tmp.equals("Place Initial"))
        {
            return false;
        }
        while(true)
        {
            tmp = filein.readLine();
            if(tmp == null)
                return false;
            else if(tmp.equals("Initial End"))
                break;
            tmparrstr.add(tmp);
        }
        for(int i = 0;i < tmparrstr.size();i++)
        {
            String[] strunits = tmparrstr.get(i).split(" ");
            Integer inttmp = i;
            mapstrint.put(strunits[0],inttmp);
            (arrcreas.get(i)).moveTo(strToPoint(strunits[1]));
        }
        Platform.runLater(()->updateSPPlace());
        return true;
    }
    int fileInParse() throws FileNotFoundException,IOException
    {   
        String iden = filein.readLine();
        if(!iden.equals(identitify))
        {
            updateNormalRec("loading:identity error!\n");
            return -1;
        }
        updateNormalRec("loading:identity pass!\n");
        if(!fileinPlaceInit())
        {
            Platform.runLater(new Thread(new UpdateTextThread(recordthisround,battlenormalrecord)));
            return -1;
        }
        updateNormalRec("loading:place initialize pass!\nfight begin after some time");
        try{Thread.sleep(3000);}catch(Exception exce){exce.printStackTrace();}       
        return 1;
    }
    int fileInOneRound(ArrayList<String> roundarrstr)
    {
        recordthisround = "";
        int roundtime = Integer.parseInt((roundarrstr.get(0).split(" "))[1]);
        recordthisround = recordthisround + roundarrstr.get(0) + "\n";
        if(roundtime != contracttime)
        {
            int recorder = 1;
            String effstr = "";
            String normalrec = "";
            for(;recorder < roundarrstr.size() - 1;recorder++)
            {
                String[] strunits = roundarrstr.get(recorder).split(" ");
                if(roundarrstr.get(recorder).indexOf("move") != -1)
                {
                    int value = mapstrint.get(strunits[0]);
                    Point ntplace = strToPoint(strunits[4]);
                    (arrcreas.get(value)).moveTo(ntplace);
                    normalrec = normalrec + roundarrstr.get(recorder) + "\n";
                }
                else if(roundarrstr.get(recorder).indexOf("defeat") != -1)
                {
                    break;
                }
            }
            Platform.runLater(()->updateSPPlace());
            updateNormalRec(normalrec);
            normalrec = "";
            try{Thread.sleep(runspeed);}catch(Exception exce){exce.printStackTrace();}
            for(;recorder < roundarrstr.size() - 1;recorder++)
            {
                normalrec = normalrec + roundarrstr.get(recorder) + "\n";
                effstr = effstr + roundarrstr.get(recorder) + "\n";
                String[] strunits = roundarrstr.get(recorder).split(" ");
                int value = 0;
                if(roundarrstr.get(recorder).indexOf("distant") == -1)
                {
                    value = mapstrint.get(strunits[3]);
                }
                else
                {
                    value = mapstrint.get(strunits[4]);
                }
                (arrcreas.get(value)).die();
            }
            effectShow(effstr);
            normalrec += (roundarrstr.get(roundarrstr.size() - 1) + "\n");
            updateNormalRec(normalrec);
            updateWarningRec(hlwcontrol.warningInfoPrint());
            Platform.runLater(()->updateSPPlace());
        }
        else
        {
            String normalrec = "";
            String effrec = "";
            int recorder = 2;
            String contractstr[] = roundarrstr.get(1).split(" ");
            int xmin = (contractstr[4].toCharArray())[3] - '0';
            int ymin = (contractstr[5].toCharArray())[3] - '0';
            Platform.runLater(()->gc.strokeRect(xmin * 100,ymin * 100,400,400));
            normalrec += (roundarrstr.get(1) + "\n");
            for(;recorder < roundarrstr.size() - 1;recorder++)
            {
                normalrec = normalrec + roundarrstr.get(recorder) + "\n";
                effrec = effrec + roundarrstr.get(recorder) + "\n";
                String[] strunits = roundarrstr.get(recorder).split(" ");
                int value = 0;
                if(roundarrstr.get(recorder).indexOf("Area") != -1)
                {
                    value = mapstrint.get(strunits[1]);
                    Creature crea = arrcreas.get(value);
                    crea.die();
                    if(crea.getGroup())
                    {
                        hlwcontrol.group1dieofpoi++;
                    }
                    else
                    {
                        hlwcontrol.group2dieofpoi++;
                    }
                }
            }
            effectShow(effrec);
            Platform.runLater(()->updateSPPlace());
            normalrec += (roundarrstr.get(roundarrstr.size() - 1) + "\n");
            updateNormalRec(normalrec);
            updateWarningRec("after round 50,battle area contracts\n"+hlwcontrol.warningInfoPrint());
        }
        try{Thread.sleep(runspeed);}catch(Exception exce){exce.printStackTrace();}
        return 1;
    }
    int fileInBattleAction() throws FileNotFoundException,IOException
    {
        ArrayList<String> tmparrstr = new ArrayList<String>();
        String tmp = "init";
        while(tmp != null)
        {
            tmparrstr.clear();
            recordthisround = "";
            tmp = "init";
            while(tmp.indexOf("end") == -1)
            {
                tmp = filein.readLine();
                if(tmp == null || tmp.equals(""))
                    break;
                tmparrstr.add(tmp);
            }
            if(tmp == null || tmp.equals(""))
                 break;
           fileInOneRound(tmparrstr);
           try{Thread.sleep(runspeed);}catch(Exception exce){exce.printStackTrace();}
        }
        winAndDrawCheck();
        return 1;
    }
    boolean fileInInit()
    {
        try{
            File f = new File(dirrec);
            FileChooser filech = new FileChooser();
            filech.setTitle("Open Resource File");
            filech.setInitialDirectory(f);
            File file = filech.showOpenDialog(stg);
            if(file == null)
                return false;
            filein = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        }
        catch (FileNotFoundException exce) {  
            System.out.println("File is not found!");
            return false;}
        return true;
    }
    boolean fileInClose()
    {
        try 
        {
            filein.close(); 
        } catch (IOException exce) {
            System.out.println("inputStream close IOException:");
            return false;
        }
        return true;
    }
    boolean fileOutClose()
    {
        try 
        {
            fileout.close(); 
        } catch (IOException exce) {
            System.out.println("outputStream close IOException:");
            return false;
        }
        return true;
    }
    void battleInit()
    {
        initBattleData();
        imageViewReset();
        updateSPPlace();
        initText();
    }
    void normalBattleRoundBegin()
    {
        roundtime++;
        recordthisround = "";
        if(roundtime == 1)
          try{Thread.sleep(runspeed * 2);}catch(Exception exce){exce.printStackTrace();}
        updateNormalRec("round "+roundtime+" begin\n");
    }
    int winAndDrawCheck()
    {
        int wincheck = hlwcontrol.checkWIN();
        if(wincheck == 3)
        {
            int drawcheck = hlwcontrol.checkDRAW();
            updateWarningRec("all creatures die of battle contraction!\n");
            if(drawcheck == 2)
            {
                updateWarningRec("before contraction,Snake have more creatures\nSnake WIN\n");
            }
            else if(drawcheck == 1)
            {
                updateWarningRec("before contraction,hlw have more creatures\nhlw WIN\n");
            }
            else
            {
                updateWarningRec("before contraction,both sides have the same creatures\nDRAW!\n");
            }
            return 0;
        }
        else if(wincheck == 2)
        {
          updateWarningRec("SNAKE WIN\n");
          return 0;
        }
        else if(wincheck == 1)
        {
          updateWarningRec("HLW WIN\n");
          return 0;
        }
        else
            return 1;
    }
    int normalBattleContraction()//0 break 1 normal
    {
        String tmprecord = hlwcontrol.liveAreaReduce();
        Platform.runLater(()->gc.strokeRect(hlwcontrol.map.xmin * 100,hlwcontrol.map.ymin * 100,400,400));
        updateNormalRec(tmprecord+"round "+roundtime+" end\n");
        updateWarningRec("after round 50,battle area contracts\n"+hlwcontrol.warningInfoPrint());
        Platform.runLater(()->{updateSPPlace();});
        effectShow(tmprecord);
        try{fileout.write(recordthisround);}catch (IOException exce) {  System.out.println("Read or write Exception!");  }
        return winAndDrawCheck();
    }
    void normalBattleHandleAfterMove()
    {
        hlwcontrol.handleAfterMove();
        updateNormalRec(hlwcontrol.normalInfoPrint());
        Platform.runLater(()->{updateSPPlace();});
        try{Thread.sleep(runspeed);}catch(Exception exce){exce.printStackTrace();}
    }
    int normalBattleHandleAfterFight()
    {
        Platform.runLater(()->{updateSPPlace();});
        String fightrecord = hlwcontrol.normalInfoPrint();
        String warningrec = hlwcontrol.warningInfoPrint();
        effectShow(fightrecord);
        updateNormalRec(fightrecord + "round "+roundtime+" end\n");
        updateWarningRec(warningrec);
        try{fileout.write(recordthisround);}catch (IOException exce) {  System.out.println("Read or write Exception!");  }
        if(winAndDrawCheck() == 0)
            return 0;
        try{Thread.sleep(runspeed * 4);}catch(Exception exce){exce.printStackTrace();}
        if(state == -1)
        {
          return 0;
        }
        return 1;
    }
    void normalBattleOneRoundMove()
    {
        hlwcontrol.initCreaMoveState();
        hlwcontrol.movelock.notifyAll();
        while(hlwcontrol.movelock.x != 0)
        {
          try{
            hlwcontrol.movelock.wait();}catch(Exception e){e.printStackTrace();}
        }
    }
    void normalBattleOneRoundFight()
    {
        hlwcontrol.initCreaFightState();
        hlwcontrol.fightlock.notifyAll();
        while(hlwcontrol.fightlock.x != 0)
        {
          try{
            hlwcontrol.fightlock.wait();}catch(Exception e){e.printStackTrace();}
        }
    }
     void sceneKBEvent()
     {
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code.equals(KeyCode.UP)) 
            {
                if(state > 1 && runspeed > 125)
                    runspeed = runspeed / 2;
            }
            else if (code.equals(KeyCode.DOWN))
            {
                if(state > 1 && runspeed < 2000)
                    runspeed = runspeed * 2;
            }
            else if(code.equals(KeyCode.Q))
            {
                if(state == 1)
                {
                    hlwcontrol.creasLineUp(1, 0);
                }
                updateSPPlace();
            }
            else if(code.equals(KeyCode.W))
            {
                if(state == 1)
                {
                    hlwcontrol.creasLineUp(1, 1);
                }
                updateSPPlace();
            }
            else if(code.equals(KeyCode.E))
            {
                if(state == 1)
                {
                    hlwcontrol.creasLineUp(1, 2);
                }
                updateSPPlace();
            }
            else if(code.equals(KeyCode.A))
            {
                if(state == 1)
                {
                    hlwcontrol.creasLineUp(2, 0);
                }
                updateSPPlace();
            }
            else if(code.equals(KeyCode.S))
            {
                if(state == 1)
                {
                    hlwcontrol.creasLineUp(2, 1);
                }
                updateSPPlace();
            }
            else if(code.equals(KeyCode.D))
            {
                if(state == 1)
                {
                    hlwcontrol.creasLineUp(2, 2);
                }
                updateSPPlace();
            }
            else if(code.equals(KeyCode.SPACE))
            {
                if(state == 0)
                {
                    battleInit();
                    state = 1;
                    battlenormalrecord.setText("press QWE to set hlw's lineup\npress ASD to set frogs lineup\npress space to begin");
                    battlewarningrecord.setText("");
                    return;
                }
                else if(state == 1)
                {
                    state = 2;
                }
                else
                {
                    return;
                }
                battlenormalrecord.setText("Fight is ready to begin!\n");
                battlewarningrecord.setText("");
                try{fileOutInit();}            
                catch (FileNotFoundException exce) {  
                    System.out.println("File is not found!");  
                    state = 0;
                    return;
                 } catch (IOException exce) {  
                    System.out.println("Read or write Exception!");
                    state = 0;
                    return;
                  }
                Thread thread = new Thread() 
                {
                    
                    @Override
                    public void run() 
                    {
                        hlwcontrol.threadStart();
                        while(true)
                        {
                          normalBattleRoundBegin();
                          if(roundtime == contracttime)
                          {
                             if(normalBattleContraction() == 0)
                                break;
                             continue;
                          }
                          synchronized(hlwcontrol.movelock)
                          {
                            normalBattleOneRoundMove();
                          }
                          normalBattleHandleAfterMove();
                          synchronized(hlwcontrol.fightlock)
                          {
                            normalBattleOneRoundFight();
                          }
                          if(normalBattleHandleAfterFight() == 0)
                            break;
                          
                        }
                        hlwcontrol.threadInterrupt();
                        fileOutClose();
                        if(state == -1)
                        {
                            fo.delete();
                        }
                        state = 0;
                    }
                     
                };
                thread.start();
            }
            else if(code.equals(KeyCode.L))
            {
                if(state == 0)
                {
                    state = 3;
                }
                else
                {
                    return;
                }
                if(!fileInInit())
                {
                    state = 0;
                    return;
                }
                battlenormalrecord.setText("");
                battlewarningrecord.setText("");
                battleInit();
                Thread thread = new Thread() 
                {
                    @Override
                    public void run() 
                    {
                        try{
                        fileInParse();
                        fileInBattleAction();
                        fileInClose();
                        }
                        catch (FileNotFoundException exce) {  
                            System.out.println("File is not found!");  
                            state = 0;
                            return;
                        } catch (IOException exce) {  
                            System.out.println("Read or write Exception!");
                            state = 0;
                            return;
                        }
                        state = 0;
                    }
                };
                thread.start();
                return;
            }
        });
     }
     void initUI(Stage stage)
     {
        stg = stage;
        initDirRec();
        initImageView();
        initStackPane();
        initText();
        sceneKBEvent();
        stage.setScene(scene);
        stage.setWidth(1450);//1450
        stage.setHeight(1050);//1050
        p.setTranslateX(0);
        p.setTranslateY(0);
        p.getChildren().addAll(battlesp,canvas);
       for(int i = 0;i < creanum;i++)
           p.getChildren().add(creasp[i]);
       for(int i = 0;i < creanum/2;i++)
           p.getChildren().add(atteffsp[i]);
       for(int i = 0;i < creanum/2;i++)
           p.getChildren().add(fireeffsp[i]);
       for(int i = 0;i < creanum/2;i++)
           p.getChildren().add(iceeffsp[i]);
       for(int i = 0;i < creanum/2;i++)
           p.getChildren().add(poisoneffsp[i]);
       p.getChildren().addAll(battlenormalrecord,battlewarningrecord);
       stage.setOnCloseRequest(
       e -> {
           state = -1;
       });
        stage.show();
     }
    
    
}

class EffectThread implements Runnable
{
    StackPane stkpane;
    Point effpoi;
    static Mylock lock = new Mylock(0);
    static int max = 0;
    int id;
    int runspeed;
    EffectThread(StackPane a,int x,Point place,int runs){stkpane = a;id = x;effpoi = place;runspeed = runs;}
    static void setThreadNum(int a)
    {
        max = a;
        lock.x = max;
    }
    @Override
    public void run()
    {
        if(effpoi.x < 0 || effpoi.x > 9 || effpoi.y < 0 || effpoi.y > 9)
            return;
        stkpane.setLayoutX(effpoi.x);
        stkpane.setLayoutY(effpoi.y);
        synchronized(lock)
        {
            stkpane.setVisible(true);
            lock.x--;
            if(lock.x != 0)
                try{lock.wait();}catch(Exception exce){exce.printStackTrace();}
            else
            {
                lock.x = max;
                try{Thread.sleep(runspeed);}catch(Exception exce){exce.printStackTrace();}
                lock.notifyAll();
            }
            stkpane.setVisible(false);
            System.out.println(""+id+" awake");
        }
    }
        

}
class UpdateTextThread implements Runnable
{
    String str;
    Text txt;
    UpdateTextThread(String a,Text b){str = a;txt = b;}
    public void run()
    {
        txt.setText(str);
    }
}
class TestEffectThread implements Runnable
{
    StackPane stkpane;
    Point effpoi;
    int runspeed;
    int id;
    static int mode = 1;
    TestEffectThread(StackPane a,int x,Point place,int runs){stkpane = a;id = x;effpoi = place;runspeed = runs;}
    public void run()
    {
        if(effpoi.x < 0 || effpoi.x > 900 || effpoi.y < 0 || effpoi.y > 900)
            return;
        stkpane.setLayoutX(effpoi.x);
        stkpane.setLayoutY(effpoi.y);
        if(mode == 1)
            stkpane.setVisible(true);
        else if(mode == 0)
            stkpane.setVisible(false);
        
    }
}