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
	
	public static void sendInitialMessage(int mapWidth,int mapHeight,ArrayList<PodAgent> podsList,ArrayList<StationNodeAgent> stationsList,ArrayList<JunctionNodeAgent> junctionsList,ArrayList<RoadAgent> roadsList)
	{
		String message = "(initial)";
		//map settings
		message += mapWidth+","+mapHeight+";";
		
		//sending pods
		message += "pods:";
		for(int i=0;i<podsList.size();i++)
		{
			message += podsList.get(i).getLocalName()+","+podsList.get(i).getPosition().x+","+podsList.get(i).getPosition().y;
			if ( i < podsList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";
		
		//sending stations
		message += "stations:";
		for(int i=0;i<stationsList.size();i++)
		{
			message += stationsList.get(i).getLocalName()+","+stationsList.get(i).getPosition().x+","+stationsList.get(i).getPosition().y+","+stationsList.get(i).getPodsCapacity()+","+stationsList.get(i).getPeopleCapacity();
			if ( i < podsList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";
//		sending roads
		message += "roads:";
		for(int i=0;i<roadsList.size();i++)
		{
			message += roadsList.get(i).getLocalName()+","+roadsList.get(i).getStartPosition().x+","+roadsList.get(i).getStartPosition().y+","+roadsList.get(i).getEndPosition().x+","+roadsList.get(i).getEndPosition().y;
			if ( i < podsList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";
//		sending junctions
		message += "junctions:";
		for(int i=0;i<junctionsList.size();i++)
		{
			message += junctionsList.get(i).getLocalName()+","+junctionsList.get(i).getPosition().x+","+junctionsList.get(i).getPosition().y;
			if ( i < podsList.size()-1)
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
	
	public static void sendUpdateMessage(ArrayList<PodAgent> podsList,ArrayList<StationNodeAgent> stationsList,ArrayList<JunctionNodeAgent> junctionsList)
	{
		String message = "(update)";
		
		//sending pods
		message += "pods:";
		for(int i=0;i<podsList.size();i++)
		{
			message += podsList.get(i).getLocalName()+","+podsList.get(i).getPosition().x+","+podsList.get(i).getPosition().y;
			if ( i < podsList.size()-1)
			{
				message+=",";
			}
		}
		message += ";";
		
		//sending stations
		message += "stations:";
		for(int i=0;i<stationsList.size();i++)
		{
			message += stationsList.get(i).getLocalName()+","+stationsList.get(i).getPeopleOnStation();
			if ( i < podsList.size()-1)
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
