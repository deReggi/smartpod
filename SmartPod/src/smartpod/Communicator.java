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
	public final String ONTOLOGY_ROAD_WEIGHT_UPDATE	= "road_weight_update";
	public final String ONTOLOGY_PATH_FINDING		= "path_finding";
		
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
