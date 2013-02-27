package smartpod;

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
	private String	originName		= null;
	private String	destinationName	= null;
	private long	timeout			= 0;
	
	/***************************************************************************
	 * Constructors
	 **************************************************************************/
	
	/**
	 * Constructor for passenger group agent using default parameters.
	 */
    public PassengerGroupAgent()
    {
		this.originName = "Postaja1";
		this.destinationName = "Postaja2";
		this.timeout = 5000;
    }
	
	/**
	 * Constructor for passenger group agent.
	 * 
	 * @param originName The name of the origin station.
	 * @param destinationName The name of destination station.
	 * @param timeout The timeout when the transfer is requested.
	 */
    public PassengerGroupAgent(String originName, String destinationName, long timeout)
    {
		this.originName = originName;
		this.destinationName = destinationName;
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
			communicator.requestTransport(originName, destinationName);
        }
    }
}
