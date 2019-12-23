package hlw;
public class CreatureFactory
{
    BattleMap map;
    Mylock ll;
    Mylock ml;
    Mylock fl;
    CreatureFactory(BattleMap ma,Mylock l,Mylock m,Mylock f)
    {
        map = ma;
        ll = l;
        ml = m;
        fl = f;
    }
    public Creature get(int id,Point poi,int order)
    {
        switch(order)
        {
            case 1:return new FirstHlw(map,id,poi,ll,ml,fl); 
            case 2:return new SecondHlw(map,id,poi,ll,ml,fl); 
            case 3:return new ThirdHlw(map,id,poi,ll,ml,fl); 
            case 4:return new FourthHlw(map,id,poi,ll,ml,fl); 
            case 5:return new FifthHlw(map,id,poi,ll,ml,fl); 
            case 6:return new SixthHlw(map,id,poi,ll,ml,fl); 
            case 7:return new SeventhHlw(map,id,poi,ll,ml,fl); 
            case 8:return new Grandpa(map,id,poi,ll,ml,fl); 
            case 9:return new Frog(map,id,poi,ll,ml,fl);
            case 10:return new Scorpion(map,id,poi,ll,ml,fl);
            case 11:return new Snake(map,id,poi,ll,ml,fl);
            default:return new Frog(map,id,poi,ll,ml,fl);
        }
    }
}