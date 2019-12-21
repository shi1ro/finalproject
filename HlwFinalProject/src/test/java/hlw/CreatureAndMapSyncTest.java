package hlw;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
public class CreatureAndMapSyncTest //creature and map synchronization test
{
    ArrayList<Creature> arrcreas = new ArrayList<Creature>();
    BattleMap map = new BattleMap(arrcreas);
    Mylock tmp = new Mylock(1);

     @Test
    public void testCreatureMove()
    {
        Creature hlw1 = new FirstHlw(map, 1, new Point(5,5), tmp, tmp, tmp);//don't test thread run,lock don't need to initialize
        assertTrue(map.chmap[5][5] == hlw1.getSk());
        hlw1.moveTo(new Point(5,6));
        assertTrue(map.chmap[5][6] == hlw1.getSk());
        Creature hlw2 = new SecondHlw(map, 1, new Point(5,5), tmp, tmp, tmp);
        assertTrue(map.chmap[5][5] == hlw2.getSk());
        System.out.println("Creature Move Test pass!");
    }
    @Test
    public void testCreatureDie()
    {
        Creature hlw1 = new FirstHlw(map, 1, new Point(5,5), tmp, tmp, tmp);
        hlw1.live();
        Creature gnp = new Grandpa(map, 1, new Point(5,4), tmp, tmp, tmp);
        ArrayList<Creature> tmparr = new ArrayList<Creature>();
        int tmp = hlw1.attackpwr;
        tmparr.add(hlw1);
        gnp.setRelation(tmparr);
        gnp.die();
        assertTrue(map.chmap[5][4] == 'O');
        assertTrue(gnp.creapoi.x == 5 && gnp.creapoi.y == 4);//map place change,but also need to leave a photo in die place
        assertTrue(tmp != hlw1.attackpwr);
        assertTrue(!gnp.lifestate);
        System.out.println("Creature Die Test Pass!");
    }
    @Test
    public void testCreatureMoveBlock()
    {
        Creature hlw1 = new FirstHlw(map, 1, new Point(9,0), tmp, tmp, tmp);
        Creature frog1 = new Frog(map, 1, new Point(8,0), tmp, tmp, tmp);
        Creature frog2 = new Frog(map, 1, new Point(9,1), tmp, tmp, tmp);
        map.creatureTryMove(hlw1, hlw1.creapoi);
        assertTrue(hlw1.creapoi.x == 9 && hlw1.creapoi.y == 0);
        System.out.println("Creature Move Block Test Pass!");
    }

}