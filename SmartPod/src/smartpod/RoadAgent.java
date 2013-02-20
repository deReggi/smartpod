package smartpod;

import com.janezfeldin.Math.Point;
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
	// agent communicator
	private RoadCommunicator communicator = new RoadCommunicator(this);
	
	//variable declarations for road's properties
	public ArrayList<AID> registeredPods = new ArrayList<AID>();
	public AID		weightUpdateDelegate;
	public double	weight		= 0.0;
	public String	startNode	= "";
	public String	endNode		= "";
	public Point	startPosition;
	public Point	endPosition;
	public String	roadBelongingType = "";//(inbound ali outgoing) inbound - the road belongs to the node at the end; outgoing - road belongs to the node at the start

	/**
	 * Constructor for road agent.
	 * @param startNode String containing the name of the starting node
	 * @param endNode String containing the name of the ending node
	 * @param startPosition Point of the starting position
	 * @param endPosition Point of the end location
	 * @param roadBelongingType String containing two possible values: inbound/outgoing. inbound - the road belongs to the node at the end; outgoing - road belongs to the node at the start.
	 */
	public RoadAgent(String startNode, String endNode, Point startPosition, Point endPosition, String roadBelongingType)
	{
		this.startNode = startNode;
		this.endNode = endNode;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.roadBelongingType = roadBelongingType;
	}
	
	/**
	 * Method that returns the name of the node at the beginning of the road.
	 * @return String containing the name of the node at the beginning.
	 */
	public String getStartNode()
	{
		return startNode;
	}

	/**
	 * Method that returns the name of the node at the end of the road.
	 * @return String containing the name of the node at the end.
	 */
	public String getEndNode()
	{
		return endNode;
	}

	/**
	 * Method that returns the start position of the road.
	 * @return Point containing the position of the beginning of the road.
	 */
	public Point getStartPosition()
	{
		return startPosition;
	}

	/**
	 * Method that returns the end position of the road.
	 * @return Point containing the position of the road's end.
	 */
	public Point getEndPosition()
	{
		return endPosition;
	}

	/**
	 * Method that returns the node's name to which the road belongs.
	 * @return String that contains the name of the node to which the road belongs.
	 */
	public String getParentNode()
	{
		if (roadBelongingType.equals("inbound"))
		{
			return endNode;
		}
		else
		{
			return startNode;
		}
	}

	/**
	 * This method sets the name of the road's starting node.
	 *
	 * @param startNode String representing the name of node at the beginning of the road.
	 */
	public void setStartNode(String startNode)
	{
		this.startNode = startNode;
		throw new UnsupportedOperationException("Še ne dela, poišči lokacijo od vozlišča z imaneom" + startNode);
	}

	/**
	 * This method sets the name of the road's ending node.
	 * @param endNode String representing the name of node at the end of the road.
	 */
	public void setEndNode(String endNode)
	{
		this.endNode = endNode;
		throw new UnsupportedOperationException("Še ne dela, poišči lokacijo od vozlišča z imaneom" + endNode);
	}

	/**
	 * This method is used to set the starting position of the road.
	 * @param startPosition Point containing the desired starting position.
	 */
	public void setStartPosition(Point startPosition)
	{
		this.startPosition = startPosition;
		throw new UnsupportedOperationException("Še ne dela, poišči še ime vozlišča na lokaciji" + startPosition);
	}
	
	/**
	 * This method is used to set the ending position of the road.
	 * 
	 * @param endPosition Point containing the desired ending position.
	 */
	public void setEndPosition(Point endPosition)
	{
		this.endPosition = endPosition;
		throw new UnsupportedOperationException("Še ne dela, poišči še ime vozlišča na lokaciji" + endPosition);
	}
	
	/**
	 * This method is used to set the method that is used to determine to which node the road belongs.
	 * 
	 * @param roadBelongingType String containing either "inbound" or "outgoing". inbound - the road belongs to the node at the end; outgoing - road belongs to the node at the start
	 */
	public void setRoadBelongingType(String roadBelongingType)
	{
		this.roadBelongingType = roadBelongingType;
	}
	
	/**
	 * This method recalculates road weight.
	 * @return returns true if weight has been changed. 
	 */
	public boolean recalculateWeight()
	{
		double oldWeight = weight;
		weight = 0.5*registeredPods.size();
		return (oldWeight != weight);
	}

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
				System.out.println("com-road : "+msg.getContent());
				registeredPods.add(msg.getSender());
			}
			
			// check pod detach messages
			ArrayList<ACLMessage> detachMessages = communicator.checkPodDetachMessages();
			for (ACLMessage msg : detachMessages)
			{
				System.out.println("com-road : "+msg.getContent());
				registeredPods.remove(msg.getSender());
			}
			
			// check remaining messages
			ArrayList<ACLMessage> messages = communicator.checkMessageBox(null);
			for (ACLMessage msg : messages)
			{
				System.out.println("com-road : "+msg.getContent());
			}
			
			// inform weight change
			boolean wheightHasChanged = recalculateWeight();
			if (wheightHasChanged)
			{
				communicator.informRoadWeight();
			}
		}
	}
}
