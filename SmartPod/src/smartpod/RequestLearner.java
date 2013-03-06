/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartpod;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Janez Feldin
 */
public class RequestLearner
{
	private NodeAgent parentAgent;
	private int initialPodRequestsForPrediction;
	
	
	public RequestLearner(NodeAgent agent)
	{
		parentAgent = agent;
	}
	
	
	/**
	 * *************************************************************************
	 * History logging and learning
	 *************************************************************************
	 */
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
		requestHistory.add(parentAgent.getCurrentDate());
	}

	/**
	 * This method should be called when we want to set request in the past. It
	 * adds the request to the history of requests with desired date.
	 */
	private void podRequested(Date date)
	{
		requestHistory.add(date);
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
			if (parentAgent.getCurrentTime() - requestHistory.get(i).getTime() > numMillis)
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
	 *
	 * @return Returns double that represents the prediction for pod requests in
	 * specified time frame for next day.
	 */
	public double predictNumRequestsForTimeFrame(Date startTime, Date endTime)
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
	 *
	 * @return Array of integers, that containg the number of request between
	 * time t1 and t2 for each day the history was recorded.
	 */
	private int[] getNumRequestsInTimeFrame(Date startTime, Date endTime)
	{
//		System.out.println("getNumRequestsInTimeFrame");
		removeOutdatedHistory();

		long time = parentAgent.getCurrentTime();

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
		long startTrackingHistoryTime = parentAgent.getCurrentTime() - numDaysToKeepHistory * 86400000;
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
//				System.out.println("Checking dates: "+new Date(startTime.getTime() + 86400000 * j) + "    " + requestHistory.get(i) + "      " + new Date(startTime.getTime() + 86400000 * j + timeDiff));
				if (isDateInTimeFrame(requestHistory.get(i), new Date(startTime.getTime() + 86400000 * j), new Date(startTime.getTime() + 86400000 * j + timeDiff)))
				{
					System.out.println("Date ok za postajo "+parentAgent.getLocalName()+": "+new Date(startTime.getTime() + 86400000 * j) + "    " + requestHistory.get(i) + "      " + new Date(startTime.getTime() + 86400000 * j + timeDiff));

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
	 *
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

	void setupWithInitialValue(int initialPodRequestsForPrediction)
	{
		Date currentDate = parentAgent.getCurrentDate();
		Date tempDate = new Date();
//		System.out.println(currentDate);

		for (int i = 0; i < numDaysToKeepHistory; i++)
		{
			for (int j = 0; j < 24; j++)
			{
				for (int k = 0; k < initialPodRequestsForPrediction; k++)
				{
					tempDate = new Date();
					tempDate.setTime(currentDate.getTime() - (numDaysToKeepHistory * 86400000 + j * 3600000));
//					System.out.println(tempDate);
					podRequested(tempDate);
				}
			}
		}

		System.out.println("Testing initial prediction: ");
		System.out.println("Predikcija za postajo " + parentAgent.getLocalName() + " je: " + predictNumRequestsForTimeFrame(new Date(parentAgent.getCurrentTime()), new Date(parentAgent.getCurrentTime() + 3600000)));
		int[] temp = getNumRequestsInTimeFrame(new Date(currentDate.getTime()-3600000),new Date(currentDate.getTime()));
		for(int i=0;i<temp.length;i++)
		{
			System.out.print(temp[i]+", ");
		}
		System.out.println("");
	}
}
