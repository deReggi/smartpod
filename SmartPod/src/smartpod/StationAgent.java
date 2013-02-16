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
public class StationAgent extends NodeAgent
{

    private int podsCapacity;
    private int peopleCapacity;
    private int peopleOnStation;

    public int getPodsCapacity()
    {
        return podsCapacity;
    }

    public void setPodsCapacity(int podsCapacity)
    {
        this.podsCapacity = podsCapacity;
    }

    public int getPeopleCapacity()
    {
        return peopleCapacity;
    }

    public void setPeopleCapacity(int peopleCapacity)
    {
        this.peopleCapacity = peopleCapacity;
    }

    public int getPeopleOnStation()
    {
        return peopleOnStation;
    }

    public void setPeopleOnStation(int peopleOnStation)
    {
        this.peopleOnStation = peopleOnStation;
    }

    public boolean addPeopleToStation(int n)
    {
        if (peopleOnStation + n <= peopleCapacity)
        {
            peopleOnStation += n;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean removePeopleFromStation(int n)
    {
        if (peopleOnStation - n >= 0)
        {
            peopleOnStation -= n;
            return true;
        }
        else
        {
            return false;
        }
    }

    public StationAgent(Point position, int podsCapacity, int peopleCapacity)
    {
        super(position);
        this.podsCapacity = podsCapacity;
        this.peopleCapacity = peopleCapacity;
    }

    @Override
    protected void setup()
    {

        addBehaviour(new NodeAgent.NodeAgentBehaviout(this));
    }

    public class NodeAgentBehaviout extends CyclicBehaviour
    {

        public NodeAgentBehaviout(Agent a)
        {
            super(a);
        }

        @Override
        public void action()
        {
        }
    }
}
