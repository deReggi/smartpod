package smartpod;

import com.janezfeldin.Math.Vector2D;
import de.reggi.jade.MessageHelper;
import de.reggi.jade.MyReceiver;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.util.ArrayList;

/**
 * Class for creating node agent.
 * It extends SPAgent.
 * @author Janez Feldin
 */
public class NodeAgent extends SPAgent
{
	/***************************************************************************
	 * Variables
	 **************************************************************************/
	/**
	 * Agent communicator.
	 */
	protected NodeCommunicator communicator = new NodeCommunicator(this);
	
	/**
	 * The currently registered pods at the node.
	 */
	public ArrayList<AID> registeredPods = new ArrayList<AID>();
	/**
	 * The currently registered pods in the process of departing the node.
	 */
	public ArrayList<AID> departingPods = new ArrayList<AID>();
	/**
	 * The AID of the path finding agent.
	 */
	protected AID pathFindingAgent;
	/**
	 * The position of the node.
	 */
    protected Vector2D position;
	/**
	 * Maximum allowed pods on the station at a given moment.
	 */
    protected int podsCapacity;
	
	/***************************************************************************
	 * Constructors
	 **************************************************************************/
    
	/**
	 * Constructor for node agent.
	 * 
	 * @param position 
	 *		Vector2D that contains the desired position.
	 * @param podsCapacity
	 *		Maximum allowed pods on the station at a given moment.
	 */
    public NodeAgent(Vector2D position,int podsCapacity)
    {
        this.position = position;
		this.podsCapacity = podsCapacity;
    }
	
	/***************************************************************************
	 * JADE setup and behaviors
	 **************************************************************************/
	
	/**
	 * Path finding request behaviour.
	 * It consists of request behaviour and response behaviour.
	 * The result can be gathered by overriding the handle(...) method.
	 */
	public class ParhFindingRequest extends SequentialBehaviour
	{

		private String convID;
		private AID destinationAID = null;
		private AID podAID = null;

		public ParhFindingRequest(Agent a, AID destinationAID, AID podAID)
		{
			super(a);
			this.destinationAID = destinationAID;
			this.podAID = podAID;
		}

		@Override
		public void onStart()
		{
			convID = MessageHelper.genCID();
			addSubBehaviour(new OneShotBehaviour(myAgent)
			{
				@Override
				public void action()
				{
					communicator.requestPathFinding(convID, destinationAID);
				}
			});
			addSubBehaviour(new MyReceiver(myAgent, -1, MessageTemplate.MatchConversationId(convID))
			{
				@Override
				public void handle(ACLMessage response)
				{
					AID roadAID = new AID(response.getUserDefinedParameter("road_to_take"), false);
					double pathCost = Double.parseDouble(response.getUserDefinedParameter("path_cost"));

					handle(roadAID, pathCost, podAID, destinationAID);
				}
			});
		}

		public void handle(AID roadAID, double cost, AID podAID, AID destinationAID)
		{
			/* should override */
		}
	}
    
	/***************************************************************************
	 * Getters & setters
	 **************************************************************************/
	
	/**
	 * This method returns the position of this node.
	 * 
	 * @return
	 *		Vector2D that represents the position of the node location.
	 */
    public Vector2D getPosition()
    {
        return position;
    }
	
	/**
	 * Method used for setting the node's position.
	 * 
	 * @param position 
	 *		Vector2D that contains the desired position.
	 */
    public void setPosition(Vector2D position)
    {
        this.position = position;
    }
	
	/**
	 * Method that returns the integer value representing the maximum number 
	 * of pods, that can be on this station at a given moment.
	 * 
	 * @return
	 *		Integer value representing stations pod capacity
	 */
    public int getPodsCapacity()
    {
        return podsCapacity;
    }

	/**
	 * Method used to set the maximum number of pods, that can be on this 
	 * station at a given moment.
	 * 
	 * @param podsCapacity
	 *		Integer value representing the maximum number of pods.
	 */
    public void setPodsCapacity(int podsCapacity)
    {
        this.podsCapacity = podsCapacity;
    }
}
