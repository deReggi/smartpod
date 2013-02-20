package smartpod;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;

/**
 * The base Communicator class.
 * 
 * @author Andreas
 */
public class Communicator
{
	/**
	 * The message ontology.
	 */
	public final String ONTOLOGY_POD_NODE_ARRIVAL	= "pod_node_arrival";
	public final String ONTOLOGY_POD_NODE_DEPARTURE	= "pod_node_departure";
	public final String ONTOLOGY_POD_ROAD_ATTACH	= "pod_road_attach";
	public final String ONTOLOGY_POD_ROAD_DETACH	= "pod_road_detach";
	
	// not yet ussed
	public final String ONTOLOGY_NODE_POD	= "node_pod";
	public final String ONTOLOGY_POD_NODE	= "pod_node";
	public final String ONTOLOGY_POD_ROAD	= "pod_road";
	
	public final String ONTOLOGY_NODE_NODE	= "co_nn";
	public final String ONTOLOGY_NODE_ENV	= "co_ne";
	public final String ONTOLOGY_ENV_NODE	= "co_en";
	public final String ONTOLOGY_ENV_ROAD	= "co_er";
	public final String ONTOLOGY_ROAD_ENV	= "co_re";
	
	public final String ONTOLOGY_POD_ENV	= "pod_environment";
	public final String ONTOLOGY_ROAD_POD	= "road_pod";
	public final String ONTOLOGY_ROAD_NODE	= "road_node";
	public final String ONTOLOGY_NODE_ROAD	= "node_road";
		
	/**
	 * The parent agent.
	 */
	public SPAgent agent = null;
	
	/**
	 * Checks for received messages with given template.
	 * @param messageTemplate the MessageTemplate to use. If null all messages will be supplied.
	 * @return ArrayList of received ACLMessages.
	 */
	public ArrayList<ACLMessage> checkMessageBox(MessageTemplate messageTemplate)
	{
		ArrayList<ACLMessage> messages = new ArrayList<ACLMessage>();
		ACLMessage msg = receiveMessage(messageTemplate);
		while (msg != null)
		{
			messages.add(msg);
			msg = receiveMessage(messageTemplate);
		}
		return messages;
	}
	
	/**
	 * Receives an ACL message from the agent message queue.
	 * @param template the MessageTemplate to use.
	 * @return A new ACL message, or null if no message is present.
	 */
	public ACLMessage receiveMessage(MessageTemplate messageTemplate)
	{
		if (messageTemplate != null)
		{
			return agent.receive(messageTemplate);
		}
		return agent.receive();
	}	
}
