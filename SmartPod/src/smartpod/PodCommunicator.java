package smartpod;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;

/**
 * Class for creating PodCommunicator. It extends Communicator.
 * 
 * @author Andreas
 */
public class PodCommunicator extends Communicator
{
	/**
	 * The message templates for receiving messages.
	 */
	public MessageTemplate podArrivalTemplate	= MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
													MessageTemplate.MatchOntology(ONTOLOGY_POD_NODE_ARRIVAL));
	public MessageTemplate podDepartureTemplate = MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
													MessageTemplate.MatchOntology(ONTOLOGY_POD_NODE_DEPARTURE));
	public MessageTemplate podRoadAttachTemplate = MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.INFORM),
													MessageTemplate.MatchOntology(ONTOLOGY_POD_ROAD_ATTACH));
	
	/**
	 * PodCommunicator constructor.
	 * @param pod the parent agent
	 */
	public PodCommunicator(PodAgent pod)
	{
		this.agent = pod;
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
	 * Checks for received road attach messages.
	 * @return ArrayList of received road attach ACLMessages.
	 */
	public ArrayList<ACLMessage> checkRoadAttachMessages()
	{
		return checkMessageBox(podRoadAttachTemplate);
	}
	
	/**
	 * Sends the INFORM response to pod departure to road request.
	 * @param requestMessage the request message from (source) node.
	 */
	public void acceptPodToRoadDeparture(ACLMessage requestMessage)
	{
		System.out.println("PodCommunicator - acceptPodToRoadDeparture()");
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_POD_NODE_DEPARTURE);
		msg.setContent("accept proposal");
		msg.addReceiver(requestMessage.getSender());
		agent.send(msg);
	}
	
	/**
	 * Sends the INFORM message that the pod has been transfered to the road.
	 * @param roadName the road that pod was transferred to.
	 */
	public void informPodToRoadTransfer(String roadName)
	{
		System.out.println("PodCommunicator - informPodToRoadTransfer("+roadName+")");
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_POD_ROAD_ATTACH);
		msg.setContent("pod attached");
		msg.addReceiver(agent.getAgentByName(roadName).getName());
		agent.send(msg);
	}
	
	/**
	 * Sends the INFORM message that the pod has been transfered to the node.
	 * @param roadName the road that pod was transferred from.
	 */
	public void informPodToNodeTransfer(String roadName)
	{
		System.out.println("PodCommunicator - informPodToNodeTransfer("+roadName+")");
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(ONTOLOGY_POD_ROAD_DETACH);
		msg.setContent("pod detached");
		msg.addReceiver(agent.getAgentByName(roadName).getName());
		agent.send(msg);
	}
	
	/**
	 * Sends the REQUEST message for pod arrival to the node.
	 * @param nodeName the node the pod arrives at.
	 */
	public void requestPodToNodeArrival(String nodeName)
	{
		System.out.println("PodCommunicator - requestPodToNodeArrival("+nodeName+")");
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setOntology(ONTOLOGY_POD_NODE_ARRIVAL);
		msg.setContent("pod to node transfer request");
		msg.addReceiver(agent.getAgentByName(nodeName).getName());
		msg.addUserDefinedParameter("destination", ((PodAgent)agent).getFinalDestinationNodeName());
		agent.send(msg);
	}
}
