package smartpod;

import com.janezfeldin.Math.Vector2D;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

/**
 * Class for creating pod agent. It extends SPAgent.
 *
 * @author Janez Feldin
 */
public class PodAgent extends SPAgent
{
	/***************************************************************************
	 * Variables
	 **************************************************************************/
	
	// agent communicator
	private PodCommunicator communicator = new PodCommunicator(this);

	//variable declaration for agents properties
	private Vector2D position;
	private Vector2D currentDestination;
	private String currentDestinationNodeName;
	private Vector2D finalDestination;
	private String finalDestinationNodeName;
	private int peopleCapacity;
	private int peopleOnBoard;
	private double priority;
	
	private String currentNode;
	private boolean onTheRoad = false;
	private double currentRoadLength = 0.0;
	private Vector2D currentSource;
	private long currentTime = 0;
	private double traveledPercentage = 0.0;
	private String currentRoadName = null;
	
	/***************************************************************************
	 * Constructors
	 **************************************************************************/

	/**
	 * Constructor for creating PodAgent
	 * 
	 * @param currentNode
	 *		The current node name.
	 * @param position
	 *		The Vector2D containing the x and y desired positions of the pod.
	 * @param peopleCapacity
	 *		Integer value that is the desired maximum pod's capacity for people.
	 * @param peopleOnBoard
	 *		Integer value that is the current number of people on board the pod.
	 */
	public PodAgent(String currentNode, Vector2D position, int peopleCapacity, int peopleOnBoard)
	{
		super();
		this.currentNode = currentNode;
		this.position = position;
		this.peopleCapacity = peopleCapacity;
		this.peopleOnBoard = peopleOnBoard;
		this.currentDestinationNodeName = currentNode;
		this.finalDestinationNodeName = currentNode;
		this.currentDestination = position;
		this.finalDestination = position;
	}
	
	
	/***************************************************************************
	 * Public methods
	 **************************************************************************/
	
	/**
	 * This method is used to add a passenger on the pod.
	 *
	 * @return
	 *		Boolean that indicates the success of the operation.
	 *		If the pod is already full, false gets returned.
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
	 * This method is used to remove a passenger from the people on board the pod.
	 *
	 * @return
	 *		Boolean that indicates the success of the operation.
	 *		If the pod is already empty, false gets returned.
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
	 * This method is used to remove all the passengers currently on board the pod.
	 */
	public void removeAllPassangers()
	{
		peopleOnBoard = 0;
	}
	
	/***************************************************************************
	 * JADE setup and behaviors
	 **************************************************************************/
	
	/**
	 * This method gets called when agent is started.
	 * It adds the desired behaviour to the agent.
	 */
	@Override
	protected void setup()
	{
		//adding the desired behaviour to the agent
		this.addBehaviour(new PodAgentBehaviour(this));
		
		// send the arrival message to starting node
		communicator.requestPodToNodeArrival(currentNode);
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
		 * It gets called each time Jade platform has spare resources.
		 */
		@Override
		public void action()
		{
			// check departure message box
			ArrayList<ACLMessage> departureMessages = communicator.checkPodDepartureMessages();
			for (ACLMessage msg : departureMessages)
			{
//				System.out.println("com-pod : "+msg.getContent());
				finalDestinationNodeName = msg.getUserDefinedParameter("destination");

				communicator.acceptPodToRoadDeparture(msg);
				
				currentRoadName = msg.getUserDefinedParameter("road");
				communicator.informPodToRoadTransfer(currentRoadName);
			}
			
			// check arrival message box
			ArrayList<ACLMessage> arrivalMessages = communicator.checkPodArrivalRequestMessages();
			for (ACLMessage msg : arrivalMessages)
			{
//				System.out.println("com-pod : "+msg.getContent());
				
				if (currentRoadName != null)
				{
					communicator.informPodToNodeTransfer(currentRoadName);
					currentRoadName = null;
				}
				
				currentNode = currentDestinationNodeName;
								
				onTheRoad = false;
			}
			
			// check road attach message box
			ArrayList<ACLMessage> roadAttachMessages = communicator.checkRoadAttachMessages();
			for (ACLMessage msg : roadAttachMessages)
			{
//				System.out.println("com-pod : "+msg.getContent());
				if (onTheRoad)
				{
					// this should never happen!
					System.out.println("com-pod : ERROR: already on the road!");
					break;
				}
				
				currentDestinationNodeName = msg.getUserDefinedParameter("end_node");
				
				// calculate road length
				currentSource		= new Vector2D(msg.getUserDefinedParameter("start_position"));
				currentDestination	= new Vector2D(msg.getUserDefinedParameter("end_position"));
				currentRoadLength	= currentSource.dist(currentDestination);
				
				currentTime = getCurrentTime();
				
				onTheRoad = true;				
				// add the movement behaviour to the agent
				this.myAgent.addBehaviour(new MovementBehaviour(myAgent, 1));
			}
		}
	}
	
	/**
	 * Behaviour class for PodAgent.
	 * It extends TickerBehaviour.
	 */
	public class MovementBehaviour extends TickerBehaviour
	{
		private boolean arrived = false;

		/**
		 * Constructor for Pod's movement behaviour class.
		 * @param a the agent to which behaviour is being applied.
		 * @param period the period of the tick.
		 */
		public MovementBehaviour(Agent a, long period)
		{
			super(a, period);
		}
		
		@Override
		protected void onTick()
		{
			if (!arrived)
			{
				// @todo neka funkcija hitrosti
				double v = 0.1;

				// the time needed for the whole journey with the given speed v
				double journeyTime = currentRoadLength/v;

				// the time passed since last update
				long previousTime = currentTime;
				currentTime = getCurrentTime();
				long elapsedTime = currentTime - previousTime;

				// the time percentage of the journey
				double elapsedPercentage = elapsedTime/journeyTime;
				
//				System.out.println(getAID().getLocalName()+" elapsedTime = "+elapsedTime+"\telapsedPercentage = "+elapsedPercentage);
				
				// the travel vector representing the previously traveled distance
				Vector2D travelVector = (new Vector2D(position)).sub(currentSource);
				
				// the previously traveled distance
				double travleDistance = travelVector.mag();
				
				// the percentage compared to total road length
				double distancePercentage = travleDistance/currentRoadLength;
				
				// new percentage as the sum of previous and new
				double positionPercentage = distancePercentage + elapsedPercentage;
				
//				System.out.println("\tpercentage: "+positionPercentage);
				
				// check arrival
				if (positionPercentage >= 1.0)
				{
					positionPercentage = 1.0;
					arrived = true;
				}

				// the new position
				position = (new Vector2D(currentDestination)).sub(currentSource).mul(positionPercentage).add(currentSource);
				
//				System.out.println(getAID().getLocalName()+" position = "+position.stringRepresentation());
			}
			else
			{
				this.myAgent.removeBehaviour(this);
				communicator.requestPodToNodeArrival(currentDestinationNodeName);
			}
		}
		
	}
	
	/***************************************************************************
	 * Getters & setters
	 **************************************************************************/
	//<editor-fold defaultstate="collapsed" desc="Getters & setters">
	
	/**
	 * This method returns the position of this pod.
	 *
	 * @return
	 *		Vector2D that represents the position of the pod's location.
	 */
	public Vector2D getPosition()
	{
		return position;
	}

	/**
	 * This method is used to set the position of the pod.
	 *
	 * @param position
	 *		Vector2D that contains the desired position.
	 */
	public void setPosition(Vector2D position)
	{
		this.position = position;
	}

	/**
	 * This method returns the location of the current destination.
	 *
	 * @return
	 *		Vector2D that represents the current destination
	 *		(next station or junction).
	 */
	public Vector2D getCurrentDestination()
	{
		return currentDestination;
	}

	/**
	 * This method returns the name of the current destination's node.
	 *
	 * @return
	 *		String that represents the name of the current destination
	 *		(next station or junction).
	 */
	public String getCurrentDestinationNodeName()
	{
		return currentDestinationNodeName;
	}

	/**
	 * This method is used to set the final destination of the pod.
	 *
	 * @param destination
	 *		The Vector2D that represents the position of the final pod's 
	 *		destination (final station).
	 */
	public void setFinalDestination(Vector2D destination)
	{
		this.finalDestination = destination;
		throw new UnsupportedOperationException("Ni še narejeno, je treba dodat nastavljanje currentDestination in finalDestinationNodeName!!!");
	}

	/**
	 * This method returns the final destination's position.
	 *
	 * @return
	 *		Vector2D that represents the position of the final destination
	 *		(final station).
	 */
	public Vector2D getFinalDestination()
	{
		return finalDestination;
	}

	/**
	 * This method is used to set the final destination of the pod.
	 *
	 * @param name
	 *		String that represents the name of the final pod's destination
	 *		(station name).
	 */
	public void setFinalDestinationNodeName(String name)
	{
		this.finalDestinationNodeName = name;
		throw new UnsupportedOperationException("Ni še narejeno, je treba dodat nastavljanje currentDestination in finalDestinationNodeName!!!");
	}

	/**
	 * This method returns the final destination's station name.
	 *
	 * @return
	 *		String that represents the name of the final destination
	 *		(final station).
	 */
	public String getFinalDestinationNodeName()
	{
		return finalDestinationNodeName;
	}

	/**
	 * This method is used to set the pod's maximum capacity for people.
	 *
	 * @param n
	 *		Integer value that represents the name of the final pod's 
	 *		destination (station name).
	 */
	public void setPeopleCapacity(int n)
	{
		this.peopleCapacity = n;
	}

	/**
	 * This method returns the pod's maximum capacity for people.
	 *
	 * @return
	 *		Integer value that represents the maximum capacity for people.
	 */
	public int getPeopleCapacity()
	{
		return peopleCapacity;
	}

	/**
	 * This method is used to set the number of people currently on board.
	 *
	 * @param n
	 *		Integer value that represents the current number of people on board.
	 */
	public void setPeopleOnBoard(int n)
	{
		this.peopleOnBoard = n;
	}

	/**
	 * This method returns the number of people currently on board.
	 *
	 * @return
	 *		Integer value that represents the maximum capacity for people.
	 */
	public int getPeopleOnBoard()
	{
		return peopleOnBoard;
	}
	// </editor-fold>
}
