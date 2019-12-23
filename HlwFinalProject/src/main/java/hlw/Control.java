package hlw;


import java.util.*;

public class Control
{
  ArrayList<Creature> arrcreas;
  BattleMap map;
  CreatureFactory creafac;
  G1LineUp g1lineups[] = {new G1LineUp1(),new G1LineUp2(),new G1LineUp3()};
  G2LineUp g2lineups[] = {new G2LineUp1(),new G2LineUp2(),new G2LineUp3()};
  Mylock movelock = new Mylock(0);
  Mylock fightlock = new Mylock(0);
  Mylock lifelock = new Mylock(0);
  int group1limit;
  Thread[] threads;
  final int frognum = 10;
  int group1dieofpoi = 0;
  int group2dieofpoi = 0;

  Control(ArrayList<Creature> arr,BattleMap m){
    arrcreas = arr;
    map = m;
    creafac = new CreatureFactory(m,lifelock,movelock,fightlock);
    group1dieofpoi = 0;
    group2dieofpoi = 0;
    initLineupAndRela1();
    initLineupAndRela2();
    initThread();
    initLifeState();
  }
  void initLineupAndRela1()
  {
    if(arrcreas.size() != 0)
      arrcreas.clear();
    Creature hlw1 = creafac.get(1, new Point(3,5),1);
    Creature hlw2 = creafac.get(1, new Point(2,4),2);
    Creature hlw3 = creafac.get(1, new Point(2,6),3);
    Creature hlw4 = creafac.get(1, new Point(1,3),4);
    Creature hlw5 = creafac.get(1, new Point(1,7),5);
    Creature hlw6 = creafac.get(1, new Point(0,2),6);
    Creature hlw7 = creafac.get(1, new Point(0,8),7);
    Creature gnp = creafac.get(1, new Point(0,5),8);
    arrcreas.add(gnp);
    arrcreas.add(hlw1);
    arrcreas.add(hlw2);
    arrcreas.add(hlw3);
    arrcreas.add(hlw4);
    arrcreas.add(hlw5);
    arrcreas.add(hlw6);
    arrcreas.add(hlw7);
    ArrayList<Creature> gnprela = new ArrayList<Creature>();
    gnprela.add(hlw1);
    gnprela.add(hlw2);
    gnprela.add(hlw3);
    gnprela.add(hlw4);
    gnprela.add(hlw5);
    gnprela.add(hlw6);
    gnprela.add(hlw7);
    gnp.setRelation(gnprela);
    ArrayList<Creature> hlwrela = new ArrayList<Creature>();
    ArrayList<Creature> sechlwrela = new ArrayList<Creature>();
    hlwrela.add(gnp);
    sechlwrela.add(gnp);
    sechlwrela.add(hlw4);
    sechlwrela.add(hlw5);
    hlw1.setRelation(hlwrela);
    hlw2.setRelation(sechlwrela);
    hlw3.setRelation(hlwrela);
    hlw4.setRelation(hlwrela);
    hlw5.setRelation(hlwrela);
    hlw6.setRelation(hlwrela);
    hlw7.setRelation(hlwrela);
    group1limit = 8;
    map.setArrLimit1(group1limit);
  }
void initLineupAndRela2()//init after group1
{
  Creature[] Frogs = new Creature[10];
  ArrayList<Creature> scorrela = new ArrayList<Creature>();
  ArrayList<Creature> snakerela = new ArrayList<Creature>();
    for(int i = 0;i < frognum;i++)
    {
      Frogs[i] = creafac.get(i, new Point(7,i),9);
    }
    Creature scor = creafac.get(1, new Point(8,4),10);
    Creature snake = creafac.get(1, new Point(8,5),11);
    for(int i = 0;i < frognum;i++)
    {
      arrcreas.add(Frogs[i]);
      scorrela.add(Frogs[i]);
      snakerela.add(Frogs[i]);
    }
    arrcreas.add(scor);
    arrcreas.add(snake);
    scorrela.add(snake);
    snakerela.add(scor);
    scor.setRelation(scorrela);
    snake.setRelation(snakerela);
}
String liveAreaReduce()
{
    String record = "";
    Random rnd = new Random();
    int xmin = rnd.nextInt(7);
    int ymin = rnd.nextInt(7);
    map.setBorder(xmin, xmin+3, ymin, ymin+3);
    record += ("battle area contracts into x:["+xmin+"-"+(xmin+3)+"]"+" y:["+ymin+"-"+(ymin+3)+"]\n");
    for(int i = 0;i < arrcreas.size();i++)
    {
        Creature crea = arrcreas.get(i);
        if(crea.getLifeState())
        {
            if(!crea.checkBorder())
            {
                record += ("Area: "+crea+" die in poison area!\n");
                if(crea.getGroup())
                   group1dieofpoi++;
                else
                   group2dieofpoi++;
                crea.die();
            }
        }
    }
    return record;
}
void creasLineUp(int group,int lineupid)
{
    ArrayList<Creature> tmparrcreas= new ArrayList<Creature>();
    if(group == 1)
    {
      for(int i = 0;i < group1limit;i++)
      {
        tmparrcreas.add(arrcreas.get(i));
      }
      g1lineups[lineupid].lineup(tmparrcreas);
    }
    else
    {
      for(int i = group1limit;i < arrcreas.size();i++)
      {
        tmparrcreas.add(arrcreas.get(i));
      }
      g2lineups[lineupid].lineup(tmparrcreas);
    }
}
int checkDRAW()
    {
       
        if(group1dieofpoi > group2dieofpoi)
        {
            return 1;
        }
        else if(group1dieofpoi < group2dieofpoi)
        {
            return 2;
        }
        else
        {
            return 0;
        }
  
    }
    int checkWIN()
    {
        int ret = handleAfterFight();
        if(ret >= 1 && ret <= 3)
        {
          threadInterrupt();
          return ret;
        }
        else
          return 0;
    }
    
void initLifeState()
{
  for(int i = 0;i < arrcreas.size();i++)
   { 
     (arrcreas.get(i)).live();
   }
}
void initCreaMoveState()
{
  for(int i = 0;i < arrcreas.size();i++)
   { 
    movelock.x += (arrcreas.get(i)).ariseMove();
   }
  
}
void initCreaFightState()
{
  for(int i = 0;i < arrcreas.size();i++)
   { 
    fightlock.x += (arrcreas.get(i)).ariseFight();
   }
}
void initCreaFightData()
{
  for(int i = 0;i < arrcreas.size();i++)
  { 
    (arrcreas.get(i)).initFightData();
  }
}
void initThread()
{
  threads = new Thread[arrcreas.size()];
  for(int i = 0;i < arrcreas.size();i++)
   { 
      threads[i] = new Thread(arrcreas.get(i));
   }
}

void threadStart()
{
  for(int i = 0;i < arrcreas.size();i++)
   { 
      threads[i].start();
   }
}
void threadInterrupt()
{
  
  for(int i = 0;i < arrcreas.size();i++)
  { 
    (arrcreas.get(i)).creatureInterrupt();
  }
  synchronized(lifelock)
   {
    for(int i = 0;i < arrcreas.size();i++)
    { 
       if(!(arrcreas.get(i)).getLifeState())
       {
         (arrcreas.get(i)).live();
       }
    }
    lifelock.notifyAll();
   }
  synchronized(movelock)
  {
    initCreaMoveState();
    movelock.notifyAll();
  }
}

String normalInfoPrint()
{
    String allrecord = "";
  for(int i = 0;i < arrcreas.size();i++)
   { 
      String tmp = (arrcreas.get(i)).getRecord();
      allrecord = allrecord + tmp;
   }
   return allrecord;
}
String warningInfoPrint()
{
    String allwarningrecord = "";
  for(int i = 0;i < arrcreas.size();i++)
   { 
      String tmp = (arrcreas.get(i)).getWarning();
     allwarningrecord += tmp;
   }
   return allwarningrecord;
}
int lifeCheck()
{
  boolean g1 = false;
  boolean g2 = false;
  for(int i = 0;i < group1limit;i++)
  {
    if((arrcreas.get(i)).getLifeState())
    {
      g1 = true;
      break;
    }
  }
  for(int i = group1limit;i < arrcreas.size();i++)
  {
    if((arrcreas.get(i)).getLifeState())
    {
      g2 = true;
      break;
    }
  }
  if(!g1 && !g2)
    return 3;
  else if(!g1)
    return 2;
  else if(!g2)
    return 1;
  else
    return 0;
}
int handleAfterMove()
{
 // infoPrint();
  //map.prin();
  //System.out.println("move over");
  return 1;
}
int handleAfterFight()
{
//  infoPrint();
  return lifeCheck();
}
}

class Mylock
{
  public int x;
  Mylock(int xx){x = xx;}
}

class Point
{
 public int x;
 public int y;
 public Point(int xx,int yy){x = xx;y = yy;}
 public String toString()
 {
   return "("+x+","+y+")";
 }
}
class Direction
{
  static int direcnum = 4;
  static Point[] dict = {new Point(-1,0),new Point(1,0),new Point(0,-1),new Point(0,1),new Point(-1,1),new Point(1,1),new Point(1,-1),new Point(-1,-1)};
}