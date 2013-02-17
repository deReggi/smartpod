/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartpod;

import com.janezfeldin.Math.Point;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Class for creating pod agent. It extends SPAgent.
 *
 * @author Janez Feldin
 */
public class PodAgent extends SPAgent
{

	//variable declaration for agents properties
	private Point position;
	private Point currentDestination;
	private String currentDestinationNodeName;
	private Point finalDestination;
	private String finalDestinationNodeName;
	private int peopleCapacity;
	private int peopleOnBoard;
	private double priority;
	private boolean arrived = true;

	/**
	 * This method returns the position of this pod.
	 *
	 * @return Point that represents the position of the pod's location.
	 */
	public Point getPosition()
	{
		return position;
	}

	/**
	 * This method is used to set the position of the pod.
	 *
	 * @param position Point that contains the desired position.
	 */
	public void setPosition(Point position)
	{
		this.position = position;
	}

	/**
	 * This method returns the location of the current destination.
	 *
	 * @return Point that represents the current destionation (next station or
	 * junction).
	 */
	public Point getCurrentDestination()
	{
		return currentDestination;
	}

	/**
	 * This method returns the name of the current destination's node.
	 *
	 * @return String that represents the name of the current destionation (next
	 * station or junction).
	 */
	public String getCurrentDestinationNodeName()
	{
		return currentDestinationNodeName;
	}

	/**
	 * This method is used to set the final destination of the pod.
	 *
	 * @param destination the Point that represents the position of the final pod's destination
	 * (final station).
	 */
	public void setFinalDestination(Point destination)
	{
		arrived = false;
		this.finalDestination = destination;
		throw new UnsupportedOperationException("Ni še narejeno, je treba dodat nastavljanje currentDestination in finalDestinationNodeName!!!");
	}

	/**
	 * This method returns the final destination's position.
	 *
	 * @return Point that represents the position of the final destionation
	 * (final station).
	 */
	public Point getFinalDestination()
	{
		return finalDestination;
	}

	/**
	 * This method is used to set the final destination of the pod.
	 *
	 * @param name String that represents the neme of the final pod's destination
	 * (station name).
	 */
	public void setFinalDestinationNodeName(String name)
	{
		arrived = false;
		this.finalDestinationNodeName = name;
		throw new UnsupportedOperationException("Ni še narejeno, je treba dodat nastavljanje currentDestination in finalDestinationNodeName!!!");
	}

	/**
	 * This method returns the final destination's station name.
	 *
	 * @return String that represents the name of the final destionation (final
	 * station).
	 */
	public String getFinalDestinationNodeName()
	{
		return finalDestinationNodeName;
	}

	/**
	 * This method is used to set the pod's maximum capacity for people.
	 *
	 * @param n int that represents the neme of the final pod's destination
	 * (station name).
	 */
	public void setPeopleCapacity(int n)
	{
		this.peopleCapacity = n;
	}

	/**
	 * This method returns the pod's maximum capacity for people.
	 *
	 * @return int that represents the maximum capacity for people.
	 */
	public int getPeopleCapacity()
	{
		return peopleCapacity;
	}

	/**
	 * This method is used to set the number of people currently on board.
	 *
	 * @param n int that represents the current number of people on board.
	 */
	public void setPeopleOnBoard(int n)
	{
		this.peopleOnBoard = n;
	}

	/**
	 * This method returns the number of people currently on board.
	 *
	 * @return int that represents the maximum capacity for people.
	 */
	public int getPeopleOnBoard()
	{
		return peopleOnBoard;
	}

	/**
	 * This method is used to add a passanger on the pod.
	 *
	 * @return boolean that indicates the success of the operation. If the pod is already full, false get's returned.
	 */
	public boolean addPassanger()
	{
		if (peopleOnBoard < peopleCapacity)
		{
			peopleOnBoard++;
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * This method is used to remove a passanger from the people on board the pod.
	 *
	 * @return boolean that indicates the success of the operation. If the pod is already empty, false get's returned.
	 */
	public boolean removePassanger()
	{
		if (peopleOnBoard < 0)
		{
			peopleOnBoard--;
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * This method is used to remove all the passengets currently on board the pod.
	 */
	public void removeAllPassangers()
	{
		peopleOnBoard = 0;
	}

	/**
	 * Constructor for creating PodAgent
	 * @param position the point containing the x and y desired positions of the pod
	 * @param peopleCapacity the int that is the desired maximum pod's capacity for people
	 * @param peopleOnBoard the int that is the current number of people on board the pod
	 */
	public PodAgent(Point position, int peopleCapacity, int peopleOnBoard)
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

	/**
	 * This method gets called when agent is started.
	 * It adds the desired behaviour to the agent.
	 */
	@Override
	protected void setup()
	{




		//adding the desired behaviour to the agent
		this.addBehaviour(new PodAgentBehaviour(this));
	}

	/**
	 * Behaviour class for PodAgent.
	 * It extends CyclicBehaviour.
	 */
	public class PodAgentBehaviour extends CyclicBehaviour
	{
		/**
		 * Constructor for Pod's agent behaviour class.
		 * @param a the agent to which behaviour is being applied.
		 */
		public PodAgentBehaviour(Agent a)
		{
			super(a);
		}

		/**
		 * Method that performs actions in PodAgentBehaviour class.
		 * It get's called each time Jade platform has spare resources.
		 */
		@Override
		public void action()
		{

			//calls the method for moving the PodAgent
			move();
		}

		/**
		 * Method that performs the move for PodAgent
		 */
		private void move()
		{
			if (!arrived)
			{
			}
		}
	}
}
