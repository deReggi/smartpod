package smartpod;

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
	public MessageTemplate podArrivalTemplate	= MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
													MessageTemplate.MatchOntology(ONTOLOGY_POD_NODE_ARRIVAL));
	public MessageTemplate podDepartureTemplate = MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.INFORM),
													MessageTemplate.MatchOntology(ONTOLOGY_POD_NODE_DEPARTURE));
	
	
	
	
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
	 * Sends REQUEST message for the pod departure to the road.
	 * @param pod	the pod involved in the transfer
	 * @param road	the road the pod should transfer to
	 */
	public void proposePodToRoadDeparture(PodAgent pod, RoadAgent road)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setOntology(ONTOLOGY_POD_NODE_DEPARTURE);
		msg.setContent("pod to road transfer proposal");
		msg.addReceiver(pod.getAID());
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
}
