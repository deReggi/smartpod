package smartpod;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author Andreas
 */
public class PathFindingCommunicator extends Communicator
{
	/**
	 * The message templates for receiving messages.
	 */
	private MessageTemplate roadWeightUpdateTemplate = 
			MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchOntology(ONTOLOGY_ROAD_WEIGHT_UPDATE));
	private MessageTemplate pathFindRequestTemplate =
			MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchOntology(ONTOLOGY_PATH_FINDING));
	/**
	 * PathFindingCommunicator constructor.
	 * 
	 * @param
	 *		pathFindingAgent the parent agent
	 */
	public PathFindingCommunicator(PathFindingAgent pathFindingAgent)
	{
		this.agent = pathFindingAgent;
	}
	
	/**
	 * Checks for road weight update messages.
	 * 
	 * @return
	 *		The ACLMessage received.
	 */
	public ACLMessage checkRoadWeightUpdates()
	{
		return agent.receive(roadWeightUpdateTemplate);
	}
	
	/**
	 * Checks for received path finding requests.
	 * 
	 * @return
	 *		The ACLMessage received.
	 */
	public ACLMessage checkPathFindingRequests()
	{
		return agent.receive(pathFindRequestTemplate);
	}
	
	/**
	 * Sends the INFORM response of path finding result.
	 * 
	 * @param requestMessage
	 *		The request message.
	 * @param roadToTake
	 *		The next road agent id.
	 * @param pathCost
	 *		The total cost of the path.
	 */
	public void informPathFindingResult(ACLMessage requestMessage, AID roadToTake, double pathCost)
	{
		System.out.printf("%-10s :: informPathFindingResult(%s)\n",agent.getLocalName(),roadToTake.getLocalName());
		ACLMessage msg = requestMessage.createReply();
		msg.setPerformative(ACLMessage.INFORM);
		msg.addUserDefinedParameter("road_to_take", roadToTake.getLocalName());
		msg.addUserDefinedParameter("path_cost", String.valueOf(pathCost));
		agent.send(msg);
	}
}
