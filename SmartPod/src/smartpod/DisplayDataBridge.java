/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartpod;

import com.janezfeldin.Math.Vector2D;
import com.sun.corba.se.impl.copyobject.JavaStreamObjectCopierImpl;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Janez Feldin
 */
public class DisplayDataBridge
{

	private static final String serverName = "127.0.0.1";
	private static final int port = 12345;

	public static void sendMessage(String message) throws IOException
	{
		Socket client = new Socket(serverName, port);
		OutputStream outToServer = client.getOutputStream();
		DataOutputStream out = new DataOutputStream(outToServer);

		out.writeUTF(message);

		client.close();

	}

	public static String receiveMessage() throws IOException
	{
		Socket client = new Socket(serverName, port);
		String msg = "";

		InputStream inFromServer = client.getInputStream();
		DataInputStream in = new DataInputStream(inFromServer);
		if (in.available() != 0)
		{
			msg = in.readUTF();
		}
		client.close();
		return msg;
	}

	public static String sendReceiveMessage(String message) throws IOException
	{
		Socket client = new Socket(serverName, port);
		String msg = "";

		OutputStream outToServer = client.getOutputStream();
		DataOutputStream out = new DataOutputStream(outToServer);

		out.writeUTF(message);
		
		InputStream inFromServer = client.getInputStream();
		DataInputStream in = new DataInputStream(inFromServer);
		if (in.available() != 0)
		{
			msg = in.readUTF();
		}
		client.close();

		return msg;
	}
	
	public static void sendInitialMessage(int mapWidth,int mapHeight,ArrayList<PodAgent> podList,ArrayList<StationNodeAgent> stationList,ArrayList<JunctionNodeAgent> junctionList,ArrayList<RoadAgent> roadList)
	{
		JSONObject message = new JSONObject();
		message.put("Type", "InitialMessage");
		message.put("EnvironmentSize", new Vector2D(mapWidth,mapHeight).stringRepresentation());
		
		
		JSONArray tempArray = new JSONArray();
		for(int i=0;i<podList.size();i++)
		{
			JSONObject tempObject = new JSONObject();
			tempObject.put("name", podList.get(i).getLocalName());
			tempObject.put("position", podList.get(i).getPosition().stringRepresentation());
			tempArray.put(tempObject);
		}
		message.put("Pods", tempArray);
		
		tempArray = new JSONArray();
		for(int i=0;i<stationList.size();i++)
		{
			JSONObject tempObject = new JSONObject();
			tempObject.put("name", stationList.get(i).getLocalName());
			tempObject.put("position", stationList.get(i).getPosition().stringRepresentation());
			tempObject.put("podCapacity", stationList.get(i).getPodsCapacity());
			tempObject.put("peopleCapacity", stationList.get(i).getPeopleCapacity());
			
			tempArray.put(tempObject);
		}
		message.put("Stations", tempArray);
		
		tempArray = new JSONArray();
		for(int i=0;i<junctionList.size();i++)
		{
			JSONObject tempObject = new JSONObject();
			tempObject.put("name", junctionList.get(i).getLocalName());
			tempObject.put("position", junctionList.get(i).getPosition().stringRepresentation());
//			tempObject.put("podCapacity", junctionList.get(i).getPodsCapacity());
			
			tempArray.put(tempObject);
		}
		message.put("Junctions", tempArray);
		
		tempArray = new JSONArray();
		for(int i=0;i<roadList.size();i++)
		{
			JSONObject tempObject = new JSONObject();
			tempObject.put("name", roadList.get(i).getLocalName());
			tempObject.put("startPosition", roadList.get(i).startPosition.stringRepresentation());
			tempObject.put("endPosition", roadList.get(i).endPosition.stringRepresentation());
			
			tempArray.put(tempObject);
		}
		message.put("Roads", tempArray);
		
		/*String message = "(initial)";
		//map settings
		message += mapWidth+","+mapHeight+";";
		
		//sending pods
		message += "pods:";
		for(int i=0;i<podList.size();i++)
		{
			message += podList.get(i).getLocalName()+","+podList.get(i).getPosition().x+","+podList.get(i).getPosition().y;
			if ( i < podList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";
		
		//sending stations
		message += "stations:";
		for(int i=0;i<stationList.size();i++)
		{
			message += stationList.get(i).getLocalName()+","+stationList.get(i).getPosition().x+","+stationList.get(i).getPosition().y+","+stationList.get(i).getPodsCapacity()+","+stationList.get(i).getPeopleCapacity();
			if ( i < stationList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";
//		sending roads
		message += "roads:";
		for(int i=0;i<roadList.size();i++)
		{
			message += roadList.get(i).getLocalName()+","+roadList.get(i).getStartPosition().x+","+roadList.get(i).getStartPosition().y+","+roadList.get(i).getEndPosition().x+","+roadList.get(i).getEndPosition().y;
			if ( i < roadList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";
//		sending junctions
		message += "junctions:";
		for(int i=0;i<junctionList.size();i++)
		{
			message += junctionList.get(i).getLocalName()+","+junctionList.get(i).getPosition().x+","+junctionList.get(i).getPosition().y;
			if ( i < junctionList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";*/
		try
		{
			DisplayDataBridge.sendMessage(message.toString());
		}
		catch (IOException ex)
		{
			System.out.println("Error while sending message.\nError code:\n"+ex.toString());
		}
		System.out.println("Initial msg sent.");
	}
	private static int abc = 0;
	public static void sendUpdateMessage(ArrayList<PodAgent> podList,ArrayList<StationNodeAgent> stationList,ArrayList<JunctionNodeAgent> junctionList)
	{
		JSONObject message = new JSONObject();
		message.put("Type", "UpdateMessage");
		
		
		JSONArray tempArray = new JSONArray();
		//pods
		for(int i=0;i<podList.size();i++)
		{
			JSONObject tempObject = new JSONObject();
			tempObject.put("name", podList.get(i).getLocalName());
			tempObject.put("position", podList.get(i).getPosition().stringRepresentation());
			tempArray.put(tempObject);
		}
		message.put("Pods", tempArray);
		
		//stations
		tempArray = new JSONArray();
		for(int i=0;i<stationList.size();i++)
		{
			JSONObject tempObject = new JSONObject();
			tempObject.put("name",stationList.get(i).getLocalName());
			tempObject.put("podsOnStation", stationList.get(i).registeredPods.size());
			tempObject.put("peopleOnStation", stationList.get(i).getPeopleOnStation());
			
			tempArray.put(tempObject);
		}
		message.put("Stations", tempArray);
		
		//junctions
		tempArray = new JSONArray();
		for(int i=0;i<junctionList.size();i++)
		{
			JSONObject tempObject = new JSONObject();
			tempObject.put("name", junctionList.get(i).getLocalName());
			tempObject.put("podsOnJunction",junctionList.get(i).registeredPods.size());
			
			tempArray.put(tempObject);
		}
		message.put("Junctions", tempArray);
		
		System.out.println((abc++)+"   "+message.toString());
		
		/*String message = "(update)";
		
		//sending pods
		message += "pods:";
		for(int i=0;i<podList.size();i++)
		{
			message += podList.get(i).getLocalName()+","+podList.get(i).getPosition().x+","+podList.get(i).getPosition().y;
			if ( i < podList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";
		
		//sending stations
		message += "stations:";
		for(int i=0;i<stationList.size();i++)
		{
			message += stationList.get(i).getLocalName()+","+stationList.get(i).getPeopleOnStation();
			if ( i < stationList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";
//		sending junctions
		*//*message += "junctions:";
		for(int i=0;i<junctionsList.size();i++)
		{
			message += junctionsList.get(i).getLocalName()+","+junctionsList.get(i).get;
			if ( i < junctionList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";*/
		try
		{
			DisplayDataBridge.sendMessage(message.toString());
		}
		catch (IOException ex)
		{
			System.out.println("Error while sending message.\nError code:\n"+ex.toString());
		}
		System.out.println("Update msg sent.");
	}
}