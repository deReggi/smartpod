/**
 * To create the agent from JADE GUI you must use one of the two forms of
 * arguments.
 * 
 * 1.) Full name:
 *		"full", OriginNodeName1, DestinationNodeName1, #timeout1, OriginNodeName2, ...
 * e.g.:
 *		full, Postaja1, Postaja2, 500, Postaja2, Postaja6, 5000
 * 
 * 2.) Short name:
 *		#origin1 #destination1 #timeout1, #origin2 #destination2 #timeout2, ...
 * e.g.:
 *		1 2 500, 2 6 5000
 * 
 * 
 */
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
	private boolean createdByJADE	= false;
	
	/***************************************************************************
	 * Constructors
	 **************************************************************************/

	/**
	 * Constructor for passenger group agent fur JADE GUI usage.
	 */
    public PassengerGroupAgent()
    {
		System.out.println("\u001b[34mINFO  :: PassengerGroupAgent created with JADE GUI");
		createdByJADE = true;
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
		if (createdByJADE)
		{
			Object[] args = getArguments();
			if (args != null && args.length > 0)
			{
				String first = args[0].toString();
				if (first.equals("full"))
				{
					int numberOfRequests = args.length/3;
					if (args.length%3 == 1)
					{
						for (int i = 0; i < numberOfRequests; i++)
						{
							AID origin		= new AID(args[i*3+1].toString(),false);
							AID destination = new AID(args[i*3+2].toString(),false);
							long waketime	= Integer.parseInt(args[i*3+3].toString());

							addBehaviour(new PassengerGroupAgent.RequestTransportBehaviour(this,waketime,origin,destination));
						}
					}
					else
					{
						System.err.println("ERROR :: PassengerGroupAgent JADE GUI bad arguments");
						this.doDelete();
					}
				}
				else
				{
					for (int i = 0; i < args.length; i++)
					{
						String requestData = args[i].toString();
						
						String []components = requestData.split(" ");
						if (components.length == 3)
						{
							int  o = Integer.parseInt(components[0]);
							int  d = Integer.parseInt(components[1]);
							long t = Integer.parseInt(components[2]);

							AID origin		= new AID("Postaja"+o,false);
							AID destination = new AID("Postaja"+d,false);

							addBehaviour(new PassengerGroupAgent.RequestTransportBehaviour(this,t,origin,destination));
						}
						else
						{
							System.err.println("ERROR :: PassengerGroupAgent JADE GUI bad arguments");
						}
					}
				}
			}
			else
			{
				System.err.println("ERROR :: PassengerGroupAgent JADE GUI bad arguments");
				this.doDelete();
			}
		}
		else
		{
			addBehaviour(new PassengerGroupAgent.RequestTransportBehaviour(this,timeout,originAID,destinationAID));
		}
    }
    
    
    /**
	 * Behaviour class for requesting transport.
	 * It extends WakerBehaviour.
	 */
    public class RequestTransportBehaviour extends WakerBehaviour
    {
		private AID		origin		= null;
		private AID		destination	= null;
		
		/**
		 * Constructor for PassengerGroup's agent request transport behaviour class.
		 * 
		 * @param a
		 *		The agent to which behaviour is being applied.
		 * @param timeout
		 *		The timeout after which the behaviour is awakened.
		 * @param origin
		 *		The origin of the requested transport.
		 * @param destination  
		 *		The destination of the requested transport.
		 */
        public RequestTransportBehaviour(Agent a, long timeout, AID origin, AID destination)
        {
			super(a, timeout);

			this.origin = origin;
			this.destination = destination;
        }

		/**
		 * This method is invoked when the deadline defined in the constructor 
		 * is reached (or when the timeout specified in the constructor expires).
		 */
        @Override
        public void onWake()
        {
			communicator.requestTransport(this.origin, this.destination);
        }
    }
}
