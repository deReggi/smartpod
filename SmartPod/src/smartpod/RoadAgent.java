package smartpod;

import com.janezfeldin.Math.Vector2D;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

/**
 * Class for creating road agent. It extends SPAgent.
 *
 * @author Janez Feldin
 */
public class RoadAgent extends SPAgent
{
	/***************************************************************************
	 * Variables
	 **************************************************************************/

	// agent communicator
	private RoadCommunicator communicator = new RoadCommunicator(this);
	
	// registered pod list
	private ArrayList<AID> registeredPods = new ArrayList<AID>();
	
	// start and end node AIDs and positions
	private AID			startNode;
	private AID			endNode;
	private Vector2D	startPosition;
	private Vector2D	endPosition;
	
	// road weights
	private double	weight		= 0.0;
	private double	totalWeight	= 0.0;
	
	/**
	 * The AID of the weight update delegate (path finding algorithm)
	 */
	public AID		weightUpdateDelegate;

	/***************************************************************************
	 * Constructors
	 **************************************************************************/

	/**
	 * Constructor for road agent.
	 * 
	 * @param startNode
	 *		String containing the name of the starting node
	 * @param endNode
	 *		String containing the name of the ending node
	 * @param startPosition
	 *		Vector2D of the starting position
	 * @param endPosition
	 *		Vector2D of the end location
	 * @param weight
	 *		The initial road weight (speed constant).
	 */
	public RoadAgent(String startNode, String endNode, Vector2D startPosition, Vector2D endPosition, double weight)
	{
		this.startNode = new AID(startNode,false);
		this.endNode = new AID(endNode,false);
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.weight = weight;
		this.totalWeight = weight;
//		System.out.println("RoadAgent("+startNode+", "+endNode+", "+startPosition+", "+endPosition+", "+weight+")");
	}
	
	/***************************************************************************
	 * Public methods
	 **************************************************************************/

	/**
	 * This method recalculates road weight.
	 * @return returns true if weight has been changed. 
	 */
	public boolean recalculateWeight()
	{
		double oldWeight = totalWeight;
		totalWeight = weight + 0.5*registeredPods.size();
		return (oldWeight != totalWeight);
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
		addBehaviour(new RoadAgentBehaviour(this));
	}

	/**
	 * Behaviour class for RoadAgent.
	 * It extends CyclicBehaviour.
	 */
	public class RoadAgentBehaviour extends CyclicBehaviour
	{
		/**
		 * Constructor for Road's agent behaviour class.
		 * @param a the agent to which behaviour is being applied.
		 */
		public RoadAgentBehaviour(Agent a)
		{
			super(a);
		}

		/**
		 * Method that performs actions in RoadAgentBehaviour class.
		 * It gets called each time Jade platform has spare resources.
		 */
		@Override
		public void action()
		{
			// check pod attach messages
			ArrayList<ACLMessage> attachMessages = communicator.checkPodAttachMessages();
			for (ACLMessage msg : attachMessages)
			{
				registeredPods.add(msg.getSender());
				communicator.informRoadData(msg);
			}
			
			// check pod detach messages
			ArrayList<ACLMessage> detachMessages = communicator.checkPodDetachMessages();
			for (ACLMessage msg : detachMessages)
			{
				registeredPods.remove(msg.getSender());
			}
			
			// inform weight change
			boolean wheightHasChanged = recalculateWeight();
			if (wheightHasChanged)
			{
				communicator.informRoadWeight();
			}
		}
	}
	
	/***************************************************************************
	 * Getters & setters
	 **************************************************************************/

	/**
	 * Method that returns the AID of the node agent at the beginning of the road.
	 * 
	 * @return
	 *		AID of the node agent at the beginning.
	 */
	public AID getStartNode()
	{
		return startNode;
	}

	/**
	 * Method that returns the AID of the node agent at the end of the road.
	 * 
	 * @return
	 *		AID of the node agent at the end.
	 */
	public AID getEndNode()
	{
		return endNode;
	}

	/**
	 * Method that returns the start position of the road.
	 * 
	 * @return
	 *		Vector2D containing the position of the beginning of the road.
	 */
	public Vector2D getStartPosition()
	{
		return startPosition;
	}

	/**
	 * Method that returns the end position of the road.
	 * 
	 * @return 
	 *		Vector2D containing the position of the road's end.
	 */
	public Vector2D getEndPosition()
	{
		return endPosition;
	}
	
	/**
	 * Method that returns the weight of the road.
	 * It is calculated with the constant road weight (speed limit),
	 * current pod capacity and later some other stuff.
	 * 
	 * @return
	 *		The calculated weight.
	 */
	public double getWeight()
	{
		return totalWeight;
	}
	
	/**
	 * Method that returns the weight of the road as a string representation.
	 * @return
	 *		The string representation of the calculated weight.
	 */
	public String getWeightAsString()
	{
		return String.valueOf(totalWeight);
	}
}
