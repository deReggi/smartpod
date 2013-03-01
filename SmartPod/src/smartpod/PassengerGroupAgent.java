package smartpod;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;

/**
 * Class that represents a passenger group which will request a pod transfer.
 * 
 * @author Andreas
 */
public class PassengerGroupAgent extends SPAgent
{   
	/***************************************************************************
	 * Variables
	 **************************************************************************/
	
	// agent communicator
	private PassengerGroupCommunicator communicator = new PassengerGroupCommunicator(this);
	
	//variable declaration for all the agent's properties
	private AID		originAID		= null;
	private AID		destinationAID	= null;
	private long	timeout			= 0;
	
	/***************************************************************************
	 * Constructors
	 **************************************************************************/
	
	/**
	 * Constructor for passenger group agent using default parameters.
	 */
    public PassengerGroupAgent()
    {
		this.originAID = new AID("Postaja1",false);
		this.destinationAID = new AID("Postaja2",false);
		this.timeout = 5000;
    }
	
	/**
	 * Constructor for passenger group agent.
	 * 
	 * @param originAID
	 *		The origin station agent id.
	 * @param destinationAID
	 *		The destination station agent id.
	 * @param timeout
	 *		The timeout when the transfer is requested.
	 */
    public PassengerGroupAgent(AID originAID, AID destinationAID, long timeout)
    {
		this.originAID = originAID;
		this.destinationAID = destinationAID;
		this.timeout = timeout;
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
        //adds the behviour to the agent
        addBehaviour(new PassengerGroupAgent.PassengerGroupAgentBehaviour(this,timeout));
    }
    
    
    /**
	 * Behaviour class for NodeAgent.
	 * It extends CyclicBehaviour.
	 */
    public class PassengerGroupAgentBehaviour extends WakerBehaviour
    {
		/**
		 * Constructor for PassengerGroup's agent behaviour class.
		 * @param a the agent to which behaviour is being applied.
		 * @param timeout the timeout after which the behaviour is awakened.
		 */
        public PassengerGroupAgentBehaviour(Agent a, long timeout)
        {
            super(a, timeout);
        }

		/**
		 * This method is invoked when the deadline defined in the constructor 
		 * is reached (or when the timeout specified in the constructor expires).
		 * Subclasses are expected to define this method specifying the action 
		 * that must be performed at that time.
		 */
        @Override
        public void onWake()
        {
			communicator.requestTransport(originAID, destinationAID);
        }
    }
}
