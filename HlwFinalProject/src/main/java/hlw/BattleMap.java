package hlw;

import java.util.ArrayList;
import java.util.Random;

public class BattleMap
{
  int mapx = 10;
  int mapy = 10;
  int xmin = 0;
  int xmax = 9;
  int ymin = 0;
  int ymax = 9;
  char[][]chmap = new char[mapx][mapy];
  int [][]idmap = new int[mapx][mapy];
  ArrayList<Creature> arrcreas;
  int arrlimit1 = 0;
  BattleMap(ArrayList<Creature> a)
  {
    for(int i = 0;i < mapx;i++)
    for(int j = 0;j < mapy;j++)
    {
        chmap[i][j] = 'O';
        idmap[i][j] = -1;
    }
    arrcreas = a;
  }
  void setBorder(int x1,int x2,int y1,int y2){xmin = x1;xmax = x2;ymin = y1;ymax = y2;}
  void setArrLimit1(int a){arrlimit1 = a;}
  int findCrea(char sk,int id)
  {
    for(int i = 0;i < arrcreas.size();i++)
    {
      if(arrcreas.get(i).equal(sk,id))
        return i;
    }
    return -1;
  }
  ArrayList<Creature> findEnemy(Creature crea,Point poii,int attackrange)
  {
    ArrayList<Creature> temparr = new ArrayList<Creature>();
      for(int i = poii.x - attackrange;i <= poii.x + attackrange;i++)
      {
        for(int j = poii.y - attackrange;j <= poii.y + attackrange;j++)
        {
          int enemy = 0;
          if(checkBorder(new Point(i,j)) && chmap[i][j] != 'O' && (enemy = findCrea(chmap[i][j], idmap[i][j])) != -1 && (arrcreas.get(enemy)).getGroup() != crea.getGroup() && (arrcreas.get(enemy)).getLifeState() == true)
          {
            temparr.add(arrcreas.get(enemy));
          }
        }
    }
    return temparr;
  }
  boolean checkBorder(Point poi)
  {
    if(xmin <= poi.x && poi.x <= xmax && ymin <= poi.y && poi.y <= ymax)
      return true;
    else
      return false; 
  }
int distance(Point poi1,Point poi2)
  {
    return Math.abs(poi1.x - poi2.x) + Math.abs(poi1.y - poi2.y);
  }
  Point creatureTryMove(Creature crea,Point poii)
  { 
    int blocknums = 0;
    Random rnd = new Random();
    int rndmax = rnd.nextInt(Direction.direcnum);
    boolean direccheck[] = {true,true,true,true};
    int targetdirec = 0;
    int ntx = 0;
    int nty = 0;
    for(int i = 0;i < Direction.direcnum;i++)
    {
      ntx = poii.x + Direction.dict[i].x;
      nty = poii.y + Direction.dict[i].y;
      if(!checkBorder(new Point(ntx,nty)) || chmap[ntx][nty] != 'O')
      {
        direccheck[i] = false;
        blocknums++;
      }
    }
    if(blocknums == Direction.direcnum)
    {
      crea.addRecord("" + crea + " blocked! not move");
      return new Point(-1,-1);
    }
    targetdirec = 0;
    int i = 0;
    while(i <= rndmax)
    {
      targetdirec++;
      if(targetdirec == Direction.direcnum)
        targetdirec = 0;
      if(direccheck[targetdirec])
      {
          i++;
      }
    }
    ntx = poii.x + Direction.dict[targetdirec].x;
    nty = poii.y + Direction.dict[targetdirec].y;
    crea.addRecord(crea+" move to ("+ntx + "," + nty + ")");
    creatureMove(crea,poii,new Point(ntx,nty));
    return new Point(ntx,nty);

  }
  void creatureMove(Creature crea,Point poi,Point ntpoi)
  {
    if(poi.x != -1 && poi.y != -1)
    {
      chmap[poi.x][poi.y] = 'O';
      idmap[poi.x][poi.y] = -1;
    }
    if(ntpoi.x != -1 && ntpoi.y != -1)
    {
      chmap[ntpoi.x][ntpoi.y] = crea.getSk();
      idmap[ntpoi.x][ntpoi.y] = crea.getId();
    }
  }
  public void prin()
    {
        for(int i = 0;i < mapx;i++)
        {
            for(int j = 0;j < mapy;j++)
            {
              if(chmap[i][j] != 'O')
                  System.out.print("" + chmap[i][j] + idmap[i][j] + " ");
              else
                  System.out.print(chmap[i][j] + "  ");
              if(j == mapy/2 - 1)
                  System.out.print(" ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }
}