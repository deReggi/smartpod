package smartpod;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;

/**
 * Class for creating NodeCommunicator. It extends Communicator.
 * 
 * @author Andreas
 */
public class NodeCommunicator extends Communicator
{	
	/**
	 * The message templates for receiving messages.
	 */
	public MessageTemplate podArrivalTemplate =
			MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchOntology(ONTOLOGY_POD_NODE_ARRIVAL));
	public MessageTemplate podDepartureTemplate = 
			MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchOntology(ONTOLOGY_POD_NODE_DEPARTURE));
	public MessageTemplate pathFindResultTemplate = 
			MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchOntology(ONTOLOGY_PATH_FINDING));
	
	
	
	
	/**
	 * NodeCommunicator constructor.
	 * @param node the parent agent.
	 */
	public NodeCommunicator(NodeAgent node)
	{
		this.agent = node;
	}
	
	/**
	 * Checks for received pod departure messages.
	 * @return ArrayList of received pod departure ACLMessages.
	 */
	public ArrayList<ACLMessage> checkPodDepartureMessages()
	{
		return checkMessageBox(podDepartureTemplate);
	}
	
	/**
	 * Checks for received pod arrival messages.
	 * @return ArrayList of received pod arrival ACLMessages.
	 */
	public ArrayList<ACLMessage> checkPodArrivalRequestMessages()
	{
		return checkMessageBox(podArrivalTemplate);
	}
	
	/**
	 * Checks for received path finding result messages.
	 * @return ArrayList of received path finding result ACLMessages.
	 */
	public ArrayList<ACLMessage> checkPathFindingResultMessages()
	{
		return checkMessageBox(pathFindResultTemplate);
	}
	
	/**
	 * Sends REQUEST message for the pod departure to the road.
	 * @param pod	the pod involved in the transfer
	 * @param road	the road the pod should transfer to
	 */
	public void proposePodToRoadDeparture(AID pod, AID road)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setOntology(ONTOLOGY_POD_NODE_DEPARTURE);
		msg.setContent("pod to road transfer proposal");
		msg.addReceiver(pod);
		msg.addUserDefinedParameter("road", road.getLocalName());
		agent.send(msg);
	}
	
	/**
	 * Sends the CONFIRM message for accepting pod arrival to node request.
	 * @param requestMessage
	 */
	public void confirmPodToNodeArrival(ACLMessage requestMessage)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.setOntology(ONTOLOGY_POD_NODE_ARRIVAL);
		msg.setContent("pod to node transfer confirm");
		msg.addReceiver(requestMessage.getSender());
		agent.send(msg);
	}
	
	/**
	 * Sends the REQUEST message to the path finding agent.
	 * @param podName the traveling pod.
	 * @param destinationNodeName the name of the destination node.
	 */
	public void requestPathFinding(String podName, String destinationNodeName)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setOntology(ONTOLOGY_PATH_FINDING);
		msg.setContent("path finding request");
		msg.addReceiver(((NodeAgent)agent).pathFindingAgent);
		msg.addUserDefinedParameter("pod", podName);
		msg.addUserDefinedParameter("destination", destinationNodeName);
		agent.send(msg);
	}
}
