package smartpod;

import com.janezfeldin.Math.Vector2D;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        super(position,podsCapacity);
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
		 * Constructor for station agent behaviour class.
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
				
				departingPods.remove(msg.getSender());
			}
			
			// check arrival message box
			ACLMessage arrivalMessage = communicator.checkPodArrivalRequests();
			if (arrivalMessage != null)
//			ArrayList<ACLMessage> arrivalMessages = communicator.checkPodArrivalRequestMessages();
//			for (ACLMessage arrivalMessage : arrivalMessages)
			{
//				System.out.println("com-node : "+arrivalMessage.getContent());
				
				AID podAID = arrivalMessage.getSender();

				if (!registeredPods.contains(podAID))
				{
					registeredPods.add(podAID);

					AID destinationAID = new AID(arrivalMessage.getUserDefinedParameter("destination"),false);

					communicator.confirmPodToNodeArrival(arrivalMessage);

					// check whether final destination has been reached
					if (destinationAID.equals(getAID()))
					{
						// the pod has reached the final destination
						System.out.println("\u001b[32mSUCCESS    :: "+podAID.getLocalName()+" has reached destination "+getLocalName()+"\u001b[0m");
					}
					else
					{
						departingPods.add(podAID);
						registeredPods.remove(podAID);
						communicator.requestPathFinding(podAID, destinationAID);
					}
				}
				else
				{
					System.err.println("com-node : pod already registered");
				}
			}
			
			// check transport request message
			ACLMessage transportRequest = communicator.checkPassengerTransportRequests();
			if (transportRequest != null)
			{
				AID podAID = registeredPods.get(0);
				
				departingPods.add(podAID);
				registeredPods.remove(podAID);
				
				AID destinationAID = new AID(transportRequest.getUserDefinedParameter("destination"),false);
				
				communicator.requestPathFinding(podAID, destinationAID);
			}
			
			// check path finding result message box
			ArrayList<ACLMessage> pathFinding = communicator.checkPathFindingResultMessages();
			for (ACLMessage msg : pathFinding)
			{
//				System.out.println("com-node : "+msg.getContent());
				
				AID podAID = new AID(msg.getUserDefinedParameter("pod"),false);
				AID roadAID = new AID(msg.getUserDefinedParameter("road_to_take"),false);
				AID destinationAID = new AID(msg.getUserDefinedParameter("destination"),false);
				communicator.requestPodToRoadDeparture(podAID, roadAID, destinationAID);
				
				
				//testing history logging and predicting...
				/*podRequested();
				Date temp1 = getCurrentDate();
				Date temp2 = new Date(getCurrentTime()+86400000);
				System.out.println("Current time: "+temp1);
				int[] temp = getNumRequestsInTimeFrame(temp1, temp2);
				
				
				for(int i=0;i<temp.length;i++)
				{
					System.out.print(temp[i]+", ");
				}
				System.out.println("");
				System.out.println(predictNumRequestsForTimeFrame(temp1,temp2));*/
				
			}
        }
    }
	
	/***************************************************************************
	 * History logging and learning
	 **************************************************************************/
	//<editor-fold defaultstate="collapsed" desc="History logging and learning">
	//variables for properties
	int numDaysToKeepHistory = 3;
	double[] daysPredisctionWeight = {1,2,3,4,5,6,7,8,9,10};
	//other variables
	ArrayList<Date> requestHistory = new ArrayList<Date>();
	
	//all required methods
	/**
	 * This method should be called every time the station gets a request for pod.
	 * It adds the request to the history of requests.
	 */
	private void podRequested()
	{
		requestHistory.add(getCurrentDate());
	}
	
	/**
	 * This method removes the outdated history in requestHistory ArrayList.
	 */
	private void removeOutdatedHistory()
	{
		//numDaysToKeepHistory represented in milliseconds
		long numMillis = numDaysToKeepHistory*86400000;
		
		for(int i=0;i<requestHistory.size();i++)
		{
			if( getCurrentTime()-requestHistory.get(i).getTime()>numMillis)
			{
				requestHistory.remove(i--);
			}
			else
			{
				break;
			}
		}
	}
	
	/**
	 * This method calculates the prediction for the specified time frame. Time frame should be defined with two Dates that differ only in time of the day.
	 * @param startTime Starting time of the specified time frame
	 * @param endTime ending time for specified time frame
	 * @return Returns double that represents the prediction for pod requests in specified time frame for next day.
	 */
	private double predictNumRequestsForTimeFrame(Date startTime,Date endTime)
	{
//		System.out.println("predictNumRequestForTimeFrame");
		int[] tempArray = getNumRequestsInTimeFrame(startTime, endTime);
		
		double result = weightedAverage(tempArray, daysPredisctionWeight);
		return result;
	}
	
	/**
	 * This method returns the array of number of requests between times(class Date) startTime and endTime for each day the history was recorded. startTime should always be an earlier date than endTime, they should alse differ only in time of the day.
	 * @param startTime Date class representing the start time for the search.
	 * @param endTime Date class representing the end time for the search.
	 * @return Array of integers, that containg the number of request between time t1 and t2 for each day the history was recorded.
	 */
	private int[] getNumRequestsInTimeFrame(Date startTime,Date endTime)
	{
//		System.out.println("getNumRequestsInTimeFrame");
		removeOutdatedHistory();
		
		long time = getCurrentTime();
		
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		Calendar tempCal = Calendar.getInstance();
		startCal.setTime(startTime);
		endCal.setTime(endTime);
		
		if ( !(endTime.getTime()-startTime.getTime() > 0))
		{
			System.err.println("startTime and endTime doesn't containg valid values! See the getNumRequestsBetweenDates method's javadoc.");
		}
		
		long timeDiff = endTime.getTime()-startTime.getTime();
		long startTrackingHistoryTime = getCurrentTime()-numDaysToKeepHistory*86400000;
		while(startTime.getTime()>= startTrackingHistoryTime)
		{
			startTime.setTime(startTime.getTime()-86400000);
		}
//		startTime.setTime(startTime.getTime()+86400000);
		
		int[] result = new int[numDaysToKeepHistory];
		for(int i= 0;i<requestHistory.size();i++)
		{
			for(int j=0;j<numDaysToKeepHistory;j++)
			{
				System.out.println(new Date(startTime.getTime()+86400000*j)+"    "+requestHistory.get(i)+"      "+new Date(startTime.getTime()+86400000*j+timeDiff));
				if ( isDateInTimeFrame(requestHistory.get(i), new Date(startTime.getTime()+86400000*j), new Date(startTime.getTime()+86400000*j+timeDiff)))
				{
					result[j]++;
					break;
				}
				
			}
		}

		return  result;
	}
	
	/**
	 * This method calculates the weighted average for array of integers.
	 * @param array Array of integers, used to calculate weighted average
	 * @param weights array of doubles that represent weights for array of integers.
	 * @return returns the weighted average number of integer array.
	 */
	private double weightedAverage(int[] array,double[] weights)
	{
//		System.out.println("weightedAverage");
		double result;
		
		if ( array.length > weights.length)
		{
			System.err.println("Length of the array must be at least the same lenght as array of weights.");
		}
		
		double sumWeights = 0;
		double sumProducts = 0;
		
		for(int i=0;i<array.length;i++)
		{
			sumWeights += weights[i];
			sumProducts += array[i]*weights[i];
		}
		result = sumProducts/sumWeights;
		return result;
	}
	
	private boolean isDateInTimeFrame(Date checkDate,Date startTime,Date endTime)
	{
		return ((checkDate.getTime()-startTime.getTime())>=0&&(endTime.getTime()-checkDate.getTime())>=0);
	}
	
	//</editor-fold>
	
	/***************************************************************************
	 * Getters & setters
	 **************************************************************************/
	//<editor-fold defaultstate="collapsed" desc="Getters & setters">
	
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
