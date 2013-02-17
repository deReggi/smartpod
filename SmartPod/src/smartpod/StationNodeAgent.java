/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartpod;

import com.janezfeldin.Math.Point;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Class for creating StationAgent. It extends NodeAgent.
 *
 * @author Janez Feldin
 */
public class StationNodeAgent extends NodeAgent
{
	//variable declaration for all the agent's properties
    private int podsCapacity;
    private int peopleCapacity;
    private int peopleOnStation;

	/**
	 * Method that retuns the int representing the maximum number of pods, that can be on this station at a given moment.
	 * @return int stations pod capacity
	 */
    public int getPodsCapacity()
    {
        return podsCapacity;
    }

	/**
	 * Mehtod used to set the maximum number of pods, that can be on this station at a given moment.
	 * @param podsCapacity int representing the maximum number of pods.
	 */
    public void setPodsCapacity(int podsCapacity)
    {
        this.podsCapacity = podsCapacity;
    }

	/**
	 * Method that returns the maximum number of people, that can be on this station at a given moment.
	 * @return int representing the maximum number of people
	 */
    public int getPeopleCapacity()
    {
        return peopleCapacity;
    }

	/**
	 * Method used to set the maximum number of people, that can be on this station at a given moment.
	 * @param peopleCapacity int representing the maximum number of people
	 */
    public void setPeopleCapacity(int peopleCapacity)
    {
        this.peopleCapacity = peopleCapacity;
    }

	/**
	 * Method that returns the current number of people on this station.
	 * @return int current number of people waiting on this station
	 */
    public int getPeopleOnStation()
    {
        return peopleOnStation;
    }
	
	/**
	 * Method used to set the current number of people waiting on this station.
	 * @param peopleOnStation int number of people, that are currently waiting on the station.
	 */
    public void setPeopleOnStation(int peopleOnStation)
    {
        this.peopleOnStation = peopleOnStation;
    }

	/**
	 * Method used for adding people to the station.
	 * @param n int number of people to be added, if the station has the sufficient capacity
	 * @return true if the adding operation succeded or false if it failed.
	 */
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

	/**
	 * Method used for removing people off the station.
	 * @param n int number of people to be removed, if the station has enough people on it.
	 * @return true if the adding operation succeded or  false if it failed
	 */
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
	
	/**
	 * Constructor method for the StationNodeAgent.
	 * @param position Point containing the position of the station
	 * @param podsCapacity maximum allowed pods on the station at a given moment
	 * @param peopleCapacity maximum allowed people waiting on the station at a given moment
	 */
    public StationNodeAgent(Point position, int podsCapacity, int peopleCapacity)
    {
        super(position);
        this.podsCapacity = podsCapacity;
        this.peopleCapacity = peopleCapacity;
    }

	/**
	 * This method gets called when agent is started.
	 * It adds the desired behaviour to the agent.
	 */
    @Override
    protected void setup()
    {
		//adds the desired behaviour to the agent
        addBehaviour(new StationAgentBehaviout(this));
    }

	/**
	 * Behaviour class for StationAgent.
	 * It extends CyclicBehaviour.
	 */
    public class StationAgentBehaviout extends CyclicBehaviour
    {
		/**
		 * Constructor for Station's agent behaviour class.
		 * @param a the agent to which behaviour is being applied.
		 */
        public StationAgentBehaviout(Agent a)
        {
            super(a);
        }
		
		/**
		 * Method that performs actions in StationAgentBehaviour class.
		 * It get's called each time Jade platform has spare resources.
		 */
        @Override
        public void action()
        {
        }
    }
}
