package hlw;

import java.util.ArrayList;

public interface LineUp
{
    public void lineup(ArrayList<Creature> arrcreas);
}
interface G1LineUp extends LineUp
{

}
interface G2LineUp extends LineUp
{

}

class G1LineUp1 implements G1LineUp
{
    public void lineup(ArrayList<Creature> arrcreas)
    {
        arrcreas.get(0).moveTo(new Point(1,5));
        arrcreas.get(1).moveTo(new Point(3,5));
        arrcreas.get(2).moveTo(new Point(2,4));
        arrcreas.get(3).moveTo(new Point(2,6));
        arrcreas.get(4).moveTo(new Point(1,3));
        arrcreas.get(5).moveTo(new Point(1,7));
        arrcreas.get(6).moveTo(new Point(0,2));
        arrcreas.get(7).moveTo(new Point(0,8));
    }
}
class G1LineUp2 implements G1LineUp
{
    public void lineup(ArrayList<Creature> arrcreas)
    {
        arrcreas.get(0).moveTo(new Point(1,5));
        for(int i = 1;i < arrcreas.size();i++)
        {
            arrcreas.get(i).moveTo(new Point(3,i));
        }
    }
}
class G1LineUp3 implements G1LineUp
{
    public void lineup(ArrayList<Creature> arrcreas)
    {
        arrcreas.get(0).moveTo(new Point(1,5));
        boolean flag = true;
        for(int i = 1;i < arrcreas.size();i++)
        {
            if(flag)
            {
                arrcreas.get(i).moveTo(new Point(3,i));
            }
            else
            {
                arrcreas.get(i).moveTo(new Point(2,i));
            }
            flag = !flag;
        }
    }
}

class G2LineUp1 implements G2LineUp
{
    public void lineup(ArrayList<Creature> arrcreas)
    {
        int size = arrcreas.size();
        boolean flag = true;
        for(int i = 0;i < size - 2;i++)
        {
            if(flag)
            {
                arrcreas.get(i).moveTo(new Point(6,i));
            }
            else
            {
                arrcreas.get(i).moveTo(new Point(7,i));
            }
            flag = !flag;
        }
        arrcreas.get(size - 2).moveTo(new Point(8,4));
        arrcreas.get(size - 1).moveTo(new Point(8,5));
    }
}
class G2LineUp2 implements G2LineUp
{
    public void lineup(ArrayList<Creature> arrcreas)
    {
        int size = arrcreas.size();
        for(int i = 0;i < size - 2;i++)
        {
            arrcreas.get(i).moveTo(new Point(7,i));
        }
        arrcreas.get(size - 2).moveTo(new Point(8,4));
        arrcreas.get(size - 1).moveTo(new Point(8,5));
    }
}
class G2LineUp3 implements G2LineUp
{
    public void lineup(ArrayList<Creature> arrcreas)
    {
        arrcreas.get(0).moveTo(new Point(5,5));
        arrcreas.get(1).moveTo(new Point(6,4));
        arrcreas.get(2).moveTo(new Point(6,6));
        arrcreas.get(3).moveTo(new Point(7,3));
        arrcreas.get(4).moveTo(new Point(7,5));
        arrcreas.get(5).moveTo(new Point(7,7));
        arrcreas.get(6).moveTo(new Point(8,2));
        arrcreas.get(7).moveTo(new Point(8,8));
        arrcreas.get(8).moveTo(new Point(9,1));
        arrcreas.get(9).moveTo(new Point(9,9));
        arrcreas.get(10).moveTo(new Point(8,4));
        arrcreas.get(11).moveTo(new Point(8,5));
    }
}