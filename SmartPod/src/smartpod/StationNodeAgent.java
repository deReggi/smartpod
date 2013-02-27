package smartpod;

import com.janezfeldin.Math.Vector2D;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

/**
 * Class for creating StationAgent. It extends NodeAgent.
 *
 * @author Janez Feldin
 */
public class StationNodeAgent extends NodeAgent
{
	/***************************************************************************
	 * Variables
	 **************************************************************************/
	
	// agent communicator
	private NodeCommunicator communicator = new NodeCommunicator(this);
	
	//variable declaration for all the agent's properties
    private int podsCapacity;
    private int peopleCapacity;
    private int peopleOnStation;
	
	/***************************************************************************
	 * Constructors
	 **************************************************************************/
	
	/**
	 * Constructor method for the StationNodeAgent.
	 * 
	 * @param position 
	 *		Vector2D containing the position of the station
	 * @param podsCapacity 
	 *		maximum allowed pods on the station at a given moment
	 * @param peopleCapacity 
	 *		maximum allowed people waiting on the station at a given moment
	 */
    public StationNodeAgent(Vector2D position, int podsCapacity, int peopleCapacity)
    {
        super(position);
        this.podsCapacity = podsCapacity;
        this.peopleCapacity = peopleCapacity;
    }
	
	/***************************************************************************
	 * Public methods
	 **************************************************************************/
	
	/**
	 * Method used for adding people to the station.
	 * 
	 * @param n	
	 *		integer value representing the number of people to be added, 
	 *		if the station has the sufficient capacity
	 * 
	 * @return 
	 *		true if the adding operation succeeded or false if it failed.
	 */
    public boolean addPeopleToStation(int n)
    {
        if (peopleOnStation + n <= peopleCapacity)
        {
            peopleOnStation += n;
            return true;
        }
        else
        {
            return false;
        }
    }

	/**
	 * Method used for removing people off the station.
	 * 
	 * @param n 
	 *		integer value representing the number of people to be removed, 
	 *		if the station has enough people on it.
	 * 
	 * @return 
	 *		true if the adding operation succeeded or  false if it failed
	 */
    public boolean removePeopleFromStation(int n)
    {
        if (peopleOnStation - n >= 0)
        {
            peopleOnStation -= n;
            return true;
        }
        else
        {
            return false;
        }
    }
	
	/***************************************************************************
	 * JADE setup and behaviors
	 **************************************************************************/

	/**
	 * This method gets called when agent is started.
	 * It adds the desired behaviour to the agent.
	 */
    @Override
    protected void setup()
    {
		//adds the desired behaviour to the agent
        addBehaviour(new StationAgentBehaviour(this));
    }

	/**
	 * Behaviour class for StationAgent.
	 * It extends CyclicBehaviour.
	 */
    public class StationAgentBehaviour extends CyclicBehaviour
    {
		/**
		 * Constructor for Station's agent behaviour class.
		 * 
		 * @param a 
		 *		the agent to which behaviour is being applied.
		 */
        public StationAgentBehaviour(Agent a)
        {
            super(a);
        }
		
		/**
		 * Method that performs actions in StationAgentBehaviour class.
		 * It gets called each time Jade platform has spare resources.
		 */
        @Override
        public void action()
        {
			// check departure message box
			ArrayList<ACLMessage> departureMessages = communicator.checkPodDepartureMessages();
			for (ACLMessage msg : departureMessages)
			{
//				System.out.println("com-node : "+msg.getContent());
				
				departingPods.remove(msg.getSender().getLocalName());
			}
			
			// check arrival message box
			ArrayList<ACLMessage> arrivalMessages = communicator.checkPodArrivalRequestMessages();
			for (ACLMessage msg : arrivalMessages)
			{
//				System.out.println("com-node : "+msg.getContent());
				
				String podName = msg.getSender().getLocalName();

				if (!registeredPods.contains(podName))
				{
					registeredPods.add(podName);

					String destination = msg.getUserDefinedParameter("destination");

					communicator.confirmPodToNodeArrival(msg);

					// check whether final destination has been reached
					if (destination.equals(getLocalName()))
					{
						// the pod has reached the final destination
						System.out.println("=======\nSUCCESS :: "+podName+" has reached destination!\n=======");
					}
					else
					{
						communicator.requestPathFinding(podName, destination);
					}
				}
				else
				{
					System.out.println("com-node : pod already registered");
				}
			}
			
			// check transport request message
			ACLMessage transportRequest = communicator.receiveMessage(communicator.transportRequestTemplate);
			if (transportRequest != null)
			{
				String podName = registeredPods.get(0);
				
				departingPods.add(podName);
				registeredPods.remove(podName);
				
				String destination = transportRequest.getUserDefinedParameter("destination");
				
				communicator.requestPathFinding(podName, destination);
			}
			
			// check path finding result message box
			ArrayList<ACLMessage> pathFinding = communicator.checkPathFindingResultMessages();
			for (ACLMessage msg : pathFinding)
			{
//				System.out.println("com-node : "+msg.getContent());
				
				String podName = msg.getUserDefinedParameter("pod");
				String roadName = msg.getUserDefinedParameter("road_to_take");
				String destination = msg.getUserDefinedParameter("destination");
				communicator.requestPodToRoadDeparture(podName, roadName, destination);
			}
        }
    }
	
	/***************************************************************************
	 * Getters & setters
	 **************************************************************************/
	//<editor-fold defaultstate="collapsed" desc="Getters & setters">
	
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

	/**
	 * Method that returns the maximum number of people, that can be on this 
	 * station at a given moment.
	 * 
	 * @return 
	 *		Integer value representing the maximum number of people
	 */
    public int getPeopleCapacity()
    {
        return peopleCapacity;
    }

	/**
	 * Method used to set the maximum number of people, that can be on this 
	 * station at a given moment.
	 * 
	 * @param peopleCapacity 
	 *		Integer value representing the maximum number of people
	 */
    public void setPeopleCapacity(int peopleCapacity)
    {
        this.peopleCapacity = peopleCapacity;
    }

	/**
	 * Method that returns the current number of people on this station.
	 * 
	 * @return 
	 *		Current number of people waiting on this station
	 */
    public int getPeopleOnStation()
    {
        return peopleOnStation;
    }
	
	/**
	 * Method used to set the current number of people waiting on this station.
	 * 
	 * @param peopleOnStation
	 *		Number of people, that are currently waiting on the station.
	 */
    public void setPeopleOnStation(int peopleOnStation)
    {
        this.peopleOnStation = peopleOnStation;
    }
	//</editor-fold>
}
