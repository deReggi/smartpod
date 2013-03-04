package smartpod;

import jade.core.AID;
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
	 * @param passangerGroup
	 *		The parent agent.
	 */
	public PassengerGroupCommunicator(PassengerGroupAgent passangerGroup)
	{
		this.agent = passangerGroup;
	}
	
	/**
	 * Sends the REQUEST message for transport.
	 * 
	 * @param originAID
	 *		The origin station agent id.
	 * @param destinationAID
	 *		The destination station agent id.
	 */
	public void requestTransport(AID originAID, AID destinationAID)
	{
		System.out.printf("%-10s :: requestTransport(%s,%s)\n",agent.getLocalName(),originAID.getLocalName(),destinationAID.getLocalName());
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setOntology(ONTOLOGY_PASSENGER_TRANSPORT);
		msg.setContent("transport request");
		msg.addReceiver(originAID);
		msg.addUserDefinedParameter("destination", destinationAID.getLocalName());
		agent.send(msg);
	}
}
