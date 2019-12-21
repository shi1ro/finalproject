package hlw;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
public class ControlTest //creature and map synchronization test
{
    ArrayList<Creature> arrcreas = new ArrayList<Creature>();
    BattleMap map = new BattleMap(arrcreas);
    Control hlwcontrol = new Control(arrcreas,map);

     @Test
    public void ContractTest()
    {
        hlwcontrol.liveAreaReduce();
        boolean flag = true;
        for(Creature crea:arrcreas)
        {
            if((crea.checkBorder() && !crea.getLifeState()) || (!crea.checkBorder() && crea.getLifeState()))//in border but die/out of border but alive
            {
                flag = false;
            }
        }
        assertTrue(flag);
        System.out.println("Control contraction pass!");
    }
    
}