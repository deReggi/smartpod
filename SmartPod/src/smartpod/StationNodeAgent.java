package smartpod;

import com.janezfeldin.Math.Vector2D;
import de.reggi.jade.MessageHelper;
import de.reggi.jade.MyReceiver;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	
	private int peopleCapacity;
	private int peopleOnStation;
	private List<AID> stationList = new ArrayList<AID>();
	private int neededPods = 3;
	
	/**
	 * The currently pending transport requests at the node.
	 */
	public ArrayList<ACLMessage> pendingRequests = new ArrayList<ACLMessage>();

	/***************************************************************************
	 * Constructors
	 **************************************************************************/
	
	/**
	 * Constructor method for the StationNodeAgent.
	 *
	 * @param position Vector2D containing the position of the station
	 * @param podsCapacity maximum allowed pods on the station at a given moment
	 * @param peopleCapacity maximum allowed people waiting on the station at a
	 * given moment
	 */
	public StationNodeAgent(Vector2D position, int podsCapacity, int peopleCapacity)
	{
		super(position, podsCapacity);
	}

	/***************************************************************************
	 * Public methods
	 **************************************************************************/
	
	/**
	 * Method used for adding people to the station.
	 *
	 * @param n integer value representing the number of people to be added, if
	 * the station has the sufficient capacity
	 *
	 * @return true if the adding operation succeeded or false if it failed.
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
	 * @param n integer value representing the number of people to be removed,
	 * if the station has enough people on it.
	 *
	 * @return true if the adding operation succeeded or false if it failed
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
	
	/**
	 * Method fulfills the pending request and removes it from the queue.
	 * 
	 * @param request
	 *		The request needing fulfillment.
	 * @param podAID 
	 *		The agent id of the pod fulfilling the request.
	 */
	public void fulfilTransportRequest(ACLMessage request, AID podAID)
	{
		System.out.printf("%-10s :: fulfilTransportRequest(%s,%s)\n",getLocalName(),podAID.getLocalName(),request.getUserDefinedParameter("destination"));

		pendingRequests.remove(request);
		departingPods.add(podAID);
		registeredPods.remove(podAID);

		AID destinationAID = new AID(request.getUserDefinedParameter("destination"), false);

		addBehaviour(new ParhFindingRequest(this, destinationAID, podAID)
		{
			@Override
			public void handleResult(AID roadAID, double cost, AID podAID, AID destinationAID)
			{
				communicator.requestPodToRoadDeparture(podAID, roadAID, destinationAID);
			}
		});
	}

	/***************************************************************************
	 * JADE setup and behaviors
	 **************************************************************************/
	
	/**
	 * This method gets called when agent is started. It adds the desired
	 * behaviour to the agent.
	 */
	@Override
	protected void setup()
	{
		//adds the desired behaviour to the agent
		addBehaviour(new StationAgentBehaviour(this));
	}

	/**
	 * Behaviour class for StationAgent. It extends CyclicBehaviour.
	 */
	public class StationAgentBehaviour extends CyclicBehaviour
	{

		/**
		 * Constructor for station agent behaviour class.
		 *
		 * @param a the agent to which behaviour is being applied.
		 */
		public StationAgentBehaviour(Agent a)
		{
			super(a);
		}

		/**
		 * Method that performs actions in StationAgentBehaviour class. It gets
		 * called each time Jade platform has spare resources.
		 */
		@Override
		public void action()
		{
			// check departure message box
			ArrayList<ACLMessage> departureMessages = communicator.checkPodDepartureMessages();
			for (ACLMessage msg : departureMessages)
			{
				departingPods.remove(msg.getSender());
			}

			// check arrival message box
			ACLMessage arrivalMessage = communicator.checkPodArrivalRequests();
			if (arrivalMessage != null)
//			ArrayList<ACLMessage> arrivalMessages = communicator.checkPodArrivalRequestMessages();
//			for (ACLMessage arrivalMessage : arrivalMessages)
			{
				AID podAID = arrivalMessage.getSender();

				if (!registeredPods.contains(podAID))
				{
					registeredPods.add(podAID);

					AID destinationAID = new AID(arrivalMessage.getUserDefinedParameter("destination"), false);

					communicator.confirmPodToNodeArrival(arrivalMessage);

					// check whether final destination has been reached
					if (destinationAID.equals(getAID()))
					{
						// the pod has reached the final destination
						System.out.println("\u001b[32mSUCCESS    :: " + podAID.getLocalName() + " has reached destination " + getLocalName() + "\u001b[0m");
					
						if (pendingRequests.size() > 0)
						{
							fulfilTransportRequest(pendingRequests.get(0), podAID);
						}
					}
					else
					{
						departingPods.add(podAID);
						registeredPods.remove(podAID);

						myAgent.addBehaviour(new ParhFindingRequest(myAgent, destinationAID, podAID)
						{
							@Override
							public void handleResult(AID roadAID, double cost, AID podAID, AID destinationAID)
							{
								communicator.requestPodToRoadDeparture(podAID, roadAID, destinationAID);
							}
						});
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
				if (registeredPods.size() > 0)
				{
					AID podAID = registeredPods.get(0);
					fulfilTransportRequest(transportRequest, podAID);
				}
				else
				{
					pendingRequests.add(transportRequest);
					myAgent.addBehaviour(new PodBuyerBehaviour(myAgent));
				}
				
				//testing history logging and predicting...
//				/*podRequested();
//				 Date temp1 = getCurrentDate();
//				 Date temp2 = new Date(getCurrentTime()+86400000);
//				 System.out.println("Current time: "+temp1);
//				 int[] temp = getNumRequestsInTimeFrame(temp1, temp2);
//				
//				
//				 for(int i=0;i<temp.length;i++)
//				 {
//				 System.out.print(temp[i]+", ");
//				 }
//				 System.out.println("");
//				 System.out.println(predictNumRequestsForTimeFrame(temp1,temp2));*/
			}

			// check pod requests
			ACLMessage podQuery = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF));
			if (podQuery != null)
			{
				myAgent.addBehaviour(new PodOfferBehaviour(myAgent, podQuery));
			}
		}
	}

	public class PodBuyerBehaviour extends SequentialBehaviour
	{

		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		double bestPrice = 9999;
		ACLMessage bestOffer = null;
		List<ACLMessage> offers = new ArrayList<ACLMessage>(stationList.size() - 1);

		public PodBuyerBehaviour(Agent a)
		{
			super(a);

			// Enquire pod prices from all the stations
			ParallelBehaviour enquiry = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);

			for (int i = 0; i < stationList.size(); i++)
			{
				if (!myAgent.getAID().equals(stationList.get(i)))
				{
					msg.addReceiver(stationList.get(i));

					enquiry.addSubBehaviour(new MyReceiver(myAgent, -1, template)
					{
						@Override
						public void handle(ACLMessage msg)
						{
							if (msg != null)
							{
								double offer = Double.parseDouble(msg.getUserDefinedParameter("price"));

								System.out.printf("%-10s :: Got offer from %s price %f\n", myAgent.getLocalName(), msg.getSender().getLocalName(), offer);

								if (offer < bestPrice)
								{
									bestPrice = offer;
									bestOffer = msg;
								}
								offers.add(msg);
							}
						}
					});
				}
			}
			addSubBehaviour(enquiry);

			// Request the pod from best offerer.
			addSubBehaviour(new OneShotBehaviour(myAgent)
			{
				@Override
				public void action()
				{
					for (ACLMessage offer : offers)
					{
						ACLMessage reply = offer.createReply();
						if (offer == bestOffer)
						{
							reply.setPerformative(ACLMessage.REQUEST);
							reply.setContent("");
						}
						else
						{
							reply.setPerformative(ACLMessage.REFUSE);
						}
						myAgent.send(reply);
					}
				}
			});
			myAgent.send(msg);
		}
	}

	public class PodOfferBehaviour extends SequentialBehaviour
	{

		ACLMessage msg, reply;
		String convID = MessageHelper.genCID();
		AID firstRoadAID = null;
		double price = 0.0;

		public PodOfferBehaviour(Agent a, ACLMessage msg)
		{
			super(a);
			this.msg = msg;
		}

		@Override
		public void onStart()
		{
			addSubBehaviour(new ParhFindingRequest(myAgent, msg.getSender(), null)
			{
				@Override
				public void handleResult(AID roadAID, double cost, AID podAID, AID destinationAID)
				{
					firstRoadAID = roadAID;
					price = cost + (registeredPods.size() > 0 ? predictNumRequestsForTimeFrame(getCurrentDate(), new Date(getCurrentTime()+3*3600)) / registeredPods.size() : 100);
				}
			});

			addSubBehaviour(new OneShotBehaviour(myAgent)
			{
				@Override
				public void action()
				{
					reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setConversationId(convID);
					reply.addUserDefinedParameter("price", String.valueOf(price));
					myAgent.send(reply);
				}
			});

			addSubBehaviour(new MyReceiver(myAgent, -1, MessageTemplate.MatchConversationId(convID))
			{
				@Override
				public void handle(ACLMessage response)
				{
					if (response != null)
					{
						if (response.getPerformative() == ACLMessage.REQUEST)
						{
//							System.out.printf("\u001b[34m%-10s :: I am king\u001b[0m\n", myAgent.getLocalName());

							AID podAID = registeredPods.get(registeredPods.size() - 1);

							departingPods.add(podAID);
							registeredPods.remove(podAID);

							AID destinationAID = msg.getSender();

							communicator.requestPodToRoadDeparture(podAID, firstRoadAID, destinationAID);
						}
						else if (response.getPerformative() == ACLMessage.REFUSE)
						{
//							System.out.printf("\u001b[34m%-10s :: I R looser\u001b[0m\n", myAgent.getLocalName());
						}
					}
				}
			});
		}
	}
	
	/***************************************************************************
	 * History logging and learning
	 **************************************************************************/
	
	//<editor-fold defaultstate="collapsed" desc="History logging and learning">
	
	//variables for properties
	int numDaysToKeepHistory = 3;
	double[] daysPredisctionWeight =
	{
		1, 2, 3, 4, 5, 6, 7, 8, 9, 10
	};
	//other variables
	ArrayList<Date> requestHistory = new ArrayList<Date>();

	//all required methods
	/**
	 * This method should be called every time the station gets a request for
	 * pod. It adds the request to the history of requests.
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
		long numMillis = numDaysToKeepHistory * 86400000;

		for (int i = 0; i < requestHistory.size(); i++)
		{
			if (getCurrentTime() - requestHistory.get(i).getTime() > numMillis)
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
	 * This method calculates the prediction for the specified time frame. Time
	 * frame should be defined with two Dates that differ only in time of the
	 * day.
	 *
	 * @param startTime Starting time of the specified time frame
	 * @param endTime ending time for specified time frame
	 * @return Returns double that represents the prediction for pod requests in
	 * specified time frame for next day.
	 */
	private double predictNumRequestsForTimeFrame(Date startTime, Date endTime)
	{
//		System.out.println("predictNumRequestForTimeFrame");
		int[] tempArray = getNumRequestsInTimeFrame(startTime, endTime);

		double result = weightedAverage(tempArray, daysPredisctionWeight);
		return result;
	}

	/**
	 * This method returns the array of number of requests between times(class
	 * Date) startTime and endTime for each day the history was recorded.
	 * startTime should always be an earlier date than endTime, they should alse
	 * differ only in time of the day.
	 *
	 * @param startTime Date class representing the start time for the search.
	 * @param endTime Date class representing the end time for the search.
	 * @return Array of integers, that containg the number of request between
	 * time t1 and t2 for each day the history was recorded.
	 */
	private int[] getNumRequestsInTimeFrame(Date startTime, Date endTime)
	{
//		System.out.println("getNumRequestsInTimeFrame");
		removeOutdatedHistory();

		long time = getCurrentTime();

		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		Calendar tempCal = Calendar.getInstance();
		startCal.setTime(startTime);
		endCal.setTime(endTime);

		if (!(endTime.getTime() - startTime.getTime() > 0))
		{
			System.err.println("startTime and endTime doesn't containg valid values! See the getNumRequestsBetweenDates method's javadoc.");
		}

		long timeDiff = endTime.getTime() - startTime.getTime();
		long startTrackingHistoryTime = getCurrentTime() - numDaysToKeepHistory * 86400000;
		while (startTime.getTime() >= startTrackingHistoryTime)
		{
			startTime.setTime(startTime.getTime() - 86400000);
		}
//		startTime.setTime(startTime.getTime()+86400000);

		int[] result = new int[numDaysToKeepHistory];
		for (int i = 0; i < requestHistory.size(); i++)
		{
			for (int j = 0; j < numDaysToKeepHistory; j++)
			{
				System.out.println(new Date(startTime.getTime() + 86400000 * j) + "    " + requestHistory.get(i) + "      " + new Date(startTime.getTime() + 86400000 * j + timeDiff));
				if (isDateInTimeFrame(requestHistory.get(i), new Date(startTime.getTime() + 86400000 * j), new Date(startTime.getTime() + 86400000 * j + timeDiff)))
				{
					result[j]++;
					break;
				}

			}
		}

		return result;
	}

	/**
	 * This method calculates the weighted average for array of integers.
	 *
	 * @param array Array of integers, used to calculate weighted average
	 * @param weights array of doubles that represent weights for array of
	 * integers.
	 * @return returns the weighted average number of integer array.
	 */
	private double weightedAverage(int[] array, double[] weights)
	{
//		System.out.println("weightedAverage");
		double result;

		if (array.length > weights.length)
		{
			System.err.println("Length of the array must be at least the same lenght as array of weights.");
		}

		double sumWeights = 0;
		double sumProducts = 0;

		for (int i = 0; i < array.length; i++)
		{
			sumWeights += weights[i];
			sumProducts += array[i] * weights[i];
		}
		result = sumProducts / sumWeights;
		return result;
	}

	private boolean isDateInTimeFrame(Date checkDate, Date startTime, Date endTime)
	{
		return ((checkDate.getTime() - startTime.getTime()) >= 0 && (endTime.getTime() - checkDate.getTime()) >= 0);
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
	 * @return Integer value representing the maximum number of people.
	 */
	public int getPeopleCapacity()
	{
		return peopleCapacity;
	}

	/**
	 * Method used to set the maximum number of people, that can be on this
	 * station at a given moment.
	 *
	 * @param peopleCapacity Integer value representing the maximum number of
	 * people.
	 */
	public void setPeopleCapacity(int peopleCapacity)
	{
		this.peopleCapacity = peopleCapacity;
	}

	/**
	 * Method that returns the current number of people on this station.
	 *
	 * @return Current number of people waiting on this station.
	 */
	public int getPeopleOnStation()
	{
		return peopleOnStation;
	}

	/**
	 * Method used to set the current number of people waiting on this station.
	 *
	 * @param peopleOnStation Number of people, that are currently waiting on
	 * the station.
	 */
	public void setPeopleOnStation(int peopleOnStation)
	{
		this.peopleOnStation = peopleOnStation;
	}

	/**
	 * Method used to set the station list of all the stations.
	 *
	 * @param stationList The list containing all the stations.
	 */
	public void setStationList(List<AID> stationList)
	{
		this.stationList = stationList;
	}
	
	//</editor-fold>
	
}
