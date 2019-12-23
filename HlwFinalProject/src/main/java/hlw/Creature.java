package hlw;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Runnable;
abstract class Creature implements Runnable
{
  Point creapoi;
  String name;
  char skin;
  int id;
  boolean lifestate = false;
  boolean interruptflag = false;
  int attackpwr = 0;
  int attackmode = 0;
  int attackrange = 0;
  int movestate = 0;
  int fightstate = 0;
  Random rnd = new Random();
  Mylock lifelock;
  Mylock movelock;
  Mylock fightlock;
  BattleMap map;
  boolean groupbool;
  String record = "";
  String warningrecord = "";
  ArrayList<Creature> relationcreas;
  Creature(BattleMap a,char sk,int i,Point poi,Mylock llock,Mylock lock,Mylock flock)
  {
    map = a;
    skin = sk;
    id = i;
    creapoi = poi;
    name = this.getClass().getSimpleName();
    lifelock = llock;
    movelock = lock;
    fightlock = flock;
    map.creatureMove(this,new Point(-1,-1),creapoi);
  }
  boolean equal(char sk,int id)
  {
    if (this.skin == sk && this.id == id)
      return true;
    else
      return false;
  }
  char getSk(){return skin;}
  int getId(){return id;}
  int getMovestate(){return movestate;}
  boolean getGroup(){return groupbool;}
  boolean getLifeState(){return lifestate;}
  void die(){lifestate = false;
    map.creatureMove(this, creapoi,new Point(-1,-1));
  }
  void live(){lifestate = true;}
  void creatureInterrupt(){interruptflag = true;}
  void moveTo(Point nt){map.creatureMove(this, creapoi, nt);creapoi = nt;}
  void setRelation(ArrayList<Creature> rela)
  {
    relationcreas = rela;
  }
  void addRecord(String a)
  {
      record = record + a + "\n";
  }
  void addWarning(String a)
  {
      warningrecord = warningrecord + a + "\n";
  }
  String getRecord()
  {
    String tmp = record;
    record = "";
    return tmp;
  }
  String getWarning()
  {
    String tmp = warningrecord;
    warningrecord = "";
    return tmp;
  }
  public String toString()
  {
    return ""+ name + " " + creapoi;
  }
  boolean checkBorder()
  {
    return map.checkBorder(creapoi);
  }
  int ariseMove()
  {
    if(lifestate)
    {
      movestate = 1;
      return 1;
    }
    else
    {
      return 0;
    }
  }
  int ariseFight()
  {
      if(lifestate)
      {
        fightstate = 1;
        return 1;
      }
      else
      {
        return 0;
      }
  }
  void normalNearFight(Creature a)
  {
    double attpara = (double)attackpwr / (double)(attackpwr + a.attackpwr); 
    double rnddouble = Math.random();
    if(rnddouble <= attpara)
    {
      addRecord("" + this + " defeat " + a);
      a.die();
    }
    else
    {
      addRecord("" + a + " defeat " +this);
      die();
    }
  }
  void normalDistantFight(Creature a)
  {
    double attpara = (double)attackpwr / (double)(attackpwr + a.attackpwr); 
    double rnddouble = Math.random();
    //System.out.println(""+this+"att percent:"+attpara+" choose rnd:"+rnddouble);
    if(rnddouble <= attpara)
    {
      addRecord("" + this + " distant defeat " + a);
      a.die();
    }
    else
    {
    //  addRecord("" + this + " trys to distant attack "+a+" but failed");
    }
  }
  abstract void fight();
  abstract void initFightData();
  abstract void effect(String command);
  public void run()
  {
    while(true)
    {
      synchronized(lifelock)
      {
        while(!lifestate)
        {
          try
          {
      //      System.out.println(""+this+" is wait in live");
            lifelock.wait();
          }catch(Exception e){e.printStackTrace();}
        }
        if(interruptflag)
         break;
      } 

      if(fightstate == 0)
      {
        synchronized(movelock)
        {
            while(movestate == 0|| movelock.x == 0)
            {
              try
              {
      //          System.out.println(""+this+" is wait in move");
                movelock.wait();
              }catch(Exception e){e.printStackTrace();}
            }
            if(movestate == -1)
            {
              movestate = 0;
            }
            else
            {
              Point temppoi;
              temppoi = map.creatureTryMove(this,creapoi);
              if(temppoi.x != -1)
              creapoi = temppoi;
              movestate--;
            }
            movelock.x--;
           // System.out.println(movelock.x);
            if(movelock.x == 0)
              movelock.notifyAll();
        }
        if(interruptflag)
          break;
      }
      
      if(movestate == 0)
      {
        synchronized(fightlock)
        {
          while(fightstate == 0 || fightlock.x == 0)
          {
            try
            {
         //     System.out.println(""+this+" is wait in fight");
              fightlock.wait();
            }catch(Exception e){e.printStackTrace();}
          }
            fight();
            fightlock.x--;
            fightstate--;
         //   System.out.println(fightlock.x);
            if(fightlock.x == 0)
              fightlock.notifyAll();

          }
        }
      }
      //System.out.println(""+this+" is interrupted");
    }

}
class FirstHlw extends Creature
{
  int arisecount = 0;
  FirstHlw(BattleMap a,int i,Point poi,Mylock llock,Mylock lock,Mylock flock)
  {
    super(a,'A',i,poi,llock,lock,flock);
    groupbool = true;
    initFightData();
  }
  @Override
  int ariseMove()
  {
    if(lifestate)
    {
      if(arisecount == 1)
      {
        arisecount = 0;
        movestate = -1;
        return 1;
      }
      else if(arisecount == 0)
      {
        arisecount = 1;
        movestate = 1;
        return 1;
      }
      else
      {
        return 0;
      }
    }
    else
      return 0;
  }
  @Override
  void die()
  {
    lifestate = false;
    map.creatureMove(this, creapoi,new Point(-1,-1));
    if(relationcreas.size() == 0)
      return;
    for(Creature crea:relationcreas)
    {
      crea.effect("HuLuWaDie");
    }
  }
  void effect(String command)
  {
    if(!lifestate)
      return;
    if(command == "LeaderDie")//grandpa die
    {
      attackpwr -= 2;
    }
  }
  void initFightData()
  {
    attackpwr = 12;
    attackmode = 1;
    attackrange = 1;
  }
  void fight()
  {
    if(!lifestate)
      return;
    ArrayList<Creature> arr = map.findEnemy(this,creapoi,attackrange);
    //if there are >= 2 enemies,attackpower will down
    if(arr.size() == 0)
    {
    //  addRecord(""+this+"doesn't battle ");
      return;
    }
    else if(arr.size() > 1)
      attackpwr -= 4;
    for(int i = 0;i < arr.size();i++)
    {
      normalNearFight(arr.get(i));
    }
    if(arr.size() >1)
      attackpwr += 4;
  }
}
class SecondHlw extends Creature
{
  SecondHlw(BattleMap a,int i,Point poi,Mylock llock,Mylock lock,Mylock flock)
  {
    super(a,'B',i,poi,llock,lock,flock);
    groupbool = true;
    initFightData();
  }
  @Override
  void die()
  {
    lifestate = false;
    map.creatureMove(this, creapoi,new Point(-1,-1));
    addWarning("Second hlw die,fourth and fifth attack range decreases");
    if(relationcreas.size() == 0)
      return;
    for(Creature crea:relationcreas)
    {
      crea.effect("HuLuWaDie");
    }
  }
  void effect(String command)
  {
    if(!lifestate)
      return;
    if(command == "LeaderDie")//grandpa die
    {
      attackpwr -= 2;
    }
  }
  void initFightData()
  {
    attackpwr = 8;
    attackmode = 1;
    attackrange = 1;
  }
  void fight()
  {
    if(!lifestate)
      return;
    ArrayList<Creature> arr = map.findEnemy(this,creapoi,attackrange);
    if(arr.size() == 0)
    {
   //   addRecord(""+this+"doesn't battle ");
      return;
    }
    for(int i = 0;i < arr.size();i++)
    {
      normalNearFight(arr.get(i));
    }
  }
}
class ThirdHlw extends Creature
{
  ThirdHlw(BattleMap a,int i,Point poi,Mylock llock,Mylock lock,Mylock flock)
  {
    super(a,'C',i,poi,llock,lock,flock);
    groupbool = true;
    initFightData();
  }
  @Override
  void die()
  {
    lifestate = false;
    map.creatureMove(this, creapoi,new Point(-1,-1));
    if(relationcreas.size() == 0)
      return;
    for(Creature crea:relationcreas)
    {
      crea.effect("HuLuWaDie");
    }
  }
  void effect(String command)
  {
    if(!lifestate)
      return;
    if(command == "LeaderDie")//grandpa die
    {
      attackpwr -= 2;
    }
  }
  void initFightData()
  {
    attackpwr = 10;
    attackmode = 1;
    attackrange = 1;
  }
  void fight()
  {
    if(!lifestate)
      return;
    ArrayList<Creature> arr = map.findEnemy(this,creapoi,attackrange);
    if(arr.size() == 0)
    {
    //  addRecord(""+this+"doesn't battle ");
      return;
    }
    for(int i = 0;i < arr.size();i++)
    {
      normalNearFight(arr.get(i));
    }
  }
}
class FourthHlw extends Creature
{
  FourthHlw(BattleMap a,int i,Point poi,Mylock llock,Mylock lock,Mylock flock)
  {
    super(a,'D',i,poi,llock,lock,flock);
    groupbool = true;
    initFightData();
  }
  @Override
  void die()
  {
    lifestate = false;
    map.creatureMove(this, creapoi,new Point(-1,-1));
    if(relationcreas.size() == 0)
      return;
    for(Creature crea:relationcreas)
    {
      crea.effect("HuLuWaDie");
    }
  }
  void effect(String command)
  {
    if(!lifestate)
      return;
    if(command == "LeaderDie")//grandpa die
    {
      ;
    }
    else if(command == "HuLuWaDie")//second die
    {
      attackrange -= 1;
    }
  }
  void initFightData()
  {
    attackpwr = 1;
    attackmode = 2;
    attackrange = 3;
  }

  void fight()
  {
    if(!lifestate)
      return;
    ArrayList<Creature> arr = map.findEnemy(this,creapoi,attackrange);
    if(arr.size() == 0)
    {
    //  addRecord(""+this+"doesn't battle ");
      return;
    }
    for(int i = 0;i < arr.size();i++)
    {
      if(Math.abs(creapoi.x-(arr.get(i)).creapoi.x) <= 1 && Math.abs(creapoi.y-(arr.get(i)).creapoi.y) <= 1)
        normalNearFight(arr.get(i));
      else 
        normalDistantFight(arr.get(i));
    }
  }
}
class FifthHlw extends Creature
{
  FifthHlw(BattleMap a,int i,Point poi,Mylock llock,Mylock lock,Mylock flock)
  {
    super(a,'E',i,poi,llock,lock,flock);
    groupbool = true;
    initFightData();
  }
  @Override
  void die()
  {
    lifestate = false;
    map.creatureMove(this, creapoi,new Point(-1,-1));
    if(relationcreas.size() == 0)
      return;
    for(Creature crea:relationcreas)
    {
      crea.effect("HuLuWaDie");
    }
  }
  void effect(String command)
  {
    if(!lifestate)
      return;
    if(command == "LeaderDie")//grandpa die
    {
      ;
    }
    else if(command == "HuLuWaDie")//second die
    {
      attackrange-= 1;
    }
  }
  void initFightData()
  {
    attackpwr = 1;
    attackmode = 2;
    attackrange = 3;
  }
  void fight()
  {
    if(!lifestate)
      return;
    ArrayList<Creature> arr = map.findEnemy(this,creapoi,attackrange);
    if(arr.size() == 0)
    {
    //  addRecord(""+this+"doesn't battle ");
      return;
    }
    for(int i = 0;i < arr.size();i++)
    {
      if(Math.abs(creapoi.x-(arr.get(i)).creapoi.x) <= 1 && Math.abs(creapoi.y-(arr.get(i)).creapoi.y) <= 1)
        normalNearFight(arr.get(i));
      else 
        normalDistantFight(arr.get(i));
    }
  }
}
class SixthHlw extends Creature
{
  SixthHlw(BattleMap a,int i,Point poi,Mylock llock,Mylock lock,Mylock flock)
  {
    super(a,'F',i,poi,llock,lock,flock);
    groupbool = true;
   initFightData();
  }
  @Override
  int ariseMove()
  {
    if(lifestate)
    {
      movestate = 2;
      return 2;
    }
    else
    {
      return 0;
    }
  }
  @Override
  void die()
  {
    lifestate = false;
    map.creatureMove(this, creapoi,new Point(-1,-1));
    if(relationcreas.size() == 0)
      return;
    for(Creature crea:relationcreas)
    {
      crea.effect("HuLuWaDie");
    }
  }
  void effect(String command)
  {
    if(!lifestate)
      return;
    if(command == "LeaderDie")//grandpa die
    {
      attackpwr -= 2;
    }
  }
  void initFightData()
  {
    attackpwr = 7;
    attackmode = 1;
    attackrange = 1;
  }
  void fight()
  {
    if(!lifestate)
      return;
    ArrayList<Creature> arr = map.findEnemy(this,creapoi,attackrange);
    if(arr.size() == 0)
    {
  //    addRecord(""+this+"doesn't battle ");
      return;
    }
    for(int i = 0;i < arr.size();i++)
    {
        normalNearFight(arr.get(i));
    }
  }
}
class SeventhHlw extends Creature
{
  SeventhHlw(BattleMap a,int i,Point poi,Mylock llock,Mylock lock,Mylock flock)
  {
    super(a,'G',i,poi,llock,lock,flock);
    initFightData();
    groupbool = true;
  }
  @Override
  void die()
  {
    lifestate = false;
    map.creatureMove(this, creapoi,new Point(-1,-1));
    if(relationcreas.size() == 0)
      return;
    for(Creature crea:relationcreas)
    {
      crea.effect("HuLuWaDie");
    }
  }
  void effect(String command)
  {
    if(!lifestate)
      return;
    if(command == "LeaderDie")//grandpa die
    {
      attackpwr -= 2;
    }
  }
  void initFightData()
  {
    attackpwr = 6;
    attackmode = 1;
    attackrange = 1;
  }
  void fight()
  {
    if(!lifestate)
      return;
    ArrayList<Creature> arr = map.findEnemy(this,creapoi,attackrange);
    if(arr.size() == 0)
    {
    //  addRecord(""+this+"doesn't battle ");
      return;
    }
    for(int i = 0;i < arr.size();i++)
    {
        normalNearFight(arr.get(i));
    }
  }
}
class Grandpa extends Creature
{
  int count = 7;
  Grandpa(BattleMap a,int i,Point poi,Mylock llock,Mylock lock,Mylock flock)
  {
    super(a,'Y',i,poi,llock,lock,flock);
    groupbool = true;
    initFightData();
  }
  @Override
  void die()
  {
    lifestate = false;
    map.creatureMove(this, creapoi,new Point(-1,-1));
    addWarning("Grandpa dies!all hlw power down");
    if(relationcreas.size() == 0)
      return;
    for(Creature crea:relationcreas)
    {
      crea.effect("LeaderDie");
    }
  }
  void effect(String command)
  {
    if(!lifestate)
      return;
    if(command == "HuLuWaDie")//all hlw die
    {
      count--;
      if(count == 0)
      {
        addWarning("all HuLuWa die!Grandpa power up!");
   //     System.out.println("gnp angry!");
        attackpwr = 30;
      }
    }
  }
  void initFightData()
  {
    attackpwr = 2;
    attackmode = 1;
    attackrange = 1;
  }
  void fight()
  {
    if(!lifestate)
      return;
    ArrayList<Creature> arr = map.findEnemy(this,creapoi,attackrange);
    if(arr.size() == 0)
    {
   //   addRecord(""+this+"doesn't battle ");
      return;
    }
    for(int i = 0;i < arr.size();i++)
    {
        normalNearFight(arr.get(i));
    }
  }
}
class Frog extends Creature
{
  Frog(BattleMap a,int i,Point poi,Mylock llock,Mylock lock,Mylock flock)
  {
    super(a,'L',i,poi,llock,lock,flock);
    groupbool = false;
    initFightData();
  }
  @Override
  public String toString()
  {
    return ""+ name + id + " " + creapoi;
  }
  void effect(String command)
  {
    if(!lifestate)
      return;
    if(command == "LeaderDie")//leader die
    {
      attackpwr -= 1;
    }
  }
  void initFightData()
  {
    attackpwr = 6;
    attackmode = 0;
    attackrange = 0;
  }
  void fight()
  {
//    addRecord(""+this);
  }
}

class Scorpion extends Creature
{
  Scorpion(BattleMap a,int i,Point poi,Mylock llock,Mylock lock,Mylock flock)
  {
    super(a,'X',i,poi,llock,lock,flock);
    groupbool = false;
    initFightData();
  }
  @Override
  void die()
  {
    lifestate = false;
    map.creatureMove(this, creapoi,new Point(-1,-1));
    addWarning("Scorpion die!Snake power up but frogs power down");
    if(relationcreas.size() == 0)
      return;
    for(Creature crea:relationcreas)
    {
      crea.effect("LeaderDie");
    }
  }
  void effect(String command)
  {
    if(!lifestate)
      return;
    if(command == "LeaderDie")//another leader die
    {
      attackpwr = 20;
    }
  }
  void initFightData()
  {
    attackpwr = 15;
    attackmode = 0;
    attackrange = 0;
  }
  void fight()
  {
 //   addRecord(""+this);
  }
}
class Snake extends Creature
{
  Snake(BattleMap a,int i,Point poi,Mylock llock,Mylock lock,Mylock flock)
  {
    super(a,'S',i,poi,llock,lock,flock);
    groupbool = false;
    initFightData();
  }
  @Override
  void die()
  {
    lifestate = false;
    map.creatureMove(this, creapoi,new Point(-1,-1));
    addWarning("Snake die!Scorpion power up but frogs power down");
    if(relationcreas.size() == 0)
      return;
    for(Creature crea:relationcreas)
    {
      crea.effect("LeaderDie");
    }
  }
  void effect(String command)
  {
    if(!lifestate)
      return;
    if(command == "LeaderDie")//another leader die
    {
      attackpwr = 10;
    }
  }
  void initFightData()
  {
    attackpwr = 6;
    attackmode = 2;
    attackrange = 2;
  }
  void fight()
  {
    if(!lifestate)
      return;
    ArrayList<Creature> arr = map.findEnemy(this,creapoi,attackrange);
    if(arr.size() == 0)
    {
  //    addRecord(""+this+"doesn't battle ");
      return;
    }
    for(int i = 0;i < arr.size();i++)
    {
      if(Math.abs(creapoi.x-(arr.get(i)).creapoi.x) <= 1 && Math.abs(creapoi.y-(arr.get(i)).creapoi.y) <= 1)
        normalNearFight(arr.get(i));
      else 
        normalDistantFight(arr.get(i));
    }
  }
}