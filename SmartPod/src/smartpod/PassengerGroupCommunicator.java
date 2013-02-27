package smartpod;

import jade.lang.acl.ACLMessage;

/**
 * Class for creating PassengerGroupCommunicator. It extends Communicator.
 * 
 * @author Andreas
 */
public class PassengerGroupCommunicator extends Communicator
{
	/**
	 * PassengerGroupCommunicator constructor.
	 * 
	 * @param passangerGroup the parent agent.
	 */
	public PassengerGroupCommunicator(PassengerGroupAgent passangerGroup)
	{
		this.agent = passangerGroup;
	}
	
	/**
	 * Sends the REQUEST message for transport.
	 * 
	 * @param originName The name of the origin station.
	 * @param destinationName the name of the destination station.
	 */
	public void requestTransport(String originName, String destinationName)
	{
		System.out.println("PassengerGroupCommunicator - requestTransport("+originName+","+destinationName+")");
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setOntology(ONTOLOGY_PASSENGER_TRANSPORT);
		msg.setContent("transport request");
		msg.addReceiver(agent.getAgentByName(originName).getName());
		msg.addUserDefinedParameter("destination", destinationName);
		agent.send(msg);
	}
}
