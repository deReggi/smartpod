/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartpod;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
		String message = "(initial)";
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
			if ( i < podList.size()-1)
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
			if ( i < podList.size()-1)
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
			if ( i < podList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";
		try
		{
			DisplayDataBridge.sendMessage(message);
		}
		catch (IOException ex)
		{
			System.out.println("Error while sending message.\nError code:\n"+ex.toString());
		}
	}
	
	public static void sendUpdateMessage(ArrayList<PodAgent> podList,ArrayList<StationNodeAgent> stationList,ArrayList<JunctionNodeAgent> junctionList)
	{
		String message = "(update)";
		
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
			if ( i < podList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";
//		sending junctions
		/*message += "junctions:";
		for(int i=0;i<junctionsList.size();i++)
		{
			message += junctionsList.get(i).getLocalName()+","+junctionsList.get(i).get;
			if ( i < podsList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";*/
		try
		{
			DisplayDataBridge.sendMessage(message);
		}
		catch (IOException ex)
		{
			System.out.println("Error while sending message.\nError code:\n"+ex.toString());
		}
	}
}
