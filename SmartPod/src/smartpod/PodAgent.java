/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartpod;

import com.janezfeldin.Math.Point;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

/**
 *
 * @author Janez Feldin
 */
public class PodAgent extends UISAgent
{

    private Point position;
    private Point currentDestination;
    private String currentDestinationNodeName;
    private Point finalDestination;
    private String finalDestinationNodeName;
    private int peopleCapacity;
    private int peopleOnBoard;
    private double priority;
    
    private boolean arrived = true;

    public Point getPosition()
    {
        return position;
    }

    public void setPosition(Point position)
    {
        this.position = position;
    }

    public Point getCurrentDestination()
    {
        return currentDestination;
    }

    public String getCurrentDestinationNodeName()
    {
        return currentDestinationNodeName;
    }

    public void setFinalDestination(Point destination)
    {
        arrived = false;
        this.finalDestination = destination;
        throw new UnsupportedOperationException("Ni še narejeno, je treba dodat nastavljanje currentDestination in finalDestinationNodeName!!!");
    }

    public Point getFinalDestination()
    {
        return finalDestination;
    }

    public void setFinalDestinationNodeName(String name)
    {
        arrived = false;
        this.finalDestinationNodeName = name;
        throw new UnsupportedOperationException("Ni še narejeno, je treba dodat nastavljanje currentDestination in finalDestinationNodeName!!!");
    }
    public String getFinalDestinationNodeName()
    {
        return finalDestinationNodeName;
    }
    
    public void setPeopleCapacity(int n)
    {
        this.peopleCapacity = n;
    }
    public int getPeopleCapacity()
    {
        return peopleCapacity;
    }
    
    public void setPeopleOnBoard(int n)
    {
        this.peopleOnBoard = n;
    }
    public int getPeopleOnBoard()
    {
        return peopleOnBoard;
    }
    
    public boolean addPassanger()
    {
        if ( peopleOnBoard < peopleCapacity)
        {
            peopleOnBoard++;
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean removePassanger()
    {
        if ( peopleOnBoard < 0)
        {
            peopleOnBoard--;
            return true;
        }
        else
        {
            return false;
        }
    }
    public void removeAllPassangers()
    {
        peopleOnBoard = 0;
    }
    
    public PodAgent(Point position,int peopleCapacity,int peopleOnBoard)
    {
        super();
        this.position = position;
        this.peopleCapacity = peopleCapacity;
        this.peopleOnBoard = peopleOnBoard;
        this.currentDestinationNodeName = "";
        this.finalDestinationNodeName = "";
        this.currentDestination = position;
        this.finalDestination = position;
    }

    @Override
    protected void setup()
    {





        this.addBehaviour(new PodAgentBehaviour(this));
    }


    public class PodAgentBehaviour extends CyclicBehaviour
    {


        public PodAgentBehaviour(Agent a)
        {
            super(a);
        }

        @Override
        public void action()
        {

            //pokliče funkcijo za premik agenta
            move();
        }

        private void move()
        {
            if (!arrived)
            {
            }
        }
    }
}
