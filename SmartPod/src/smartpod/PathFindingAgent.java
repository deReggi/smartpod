package smartpod;

import com.janezfeldin.Math.Vector2D;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The agent responsible for path finding.
 *
 * @author Andreas
 */
public class PathFindingAgent extends SPAgent
{
	private class PFResult
	{
		public AID roadAID = null;
		public double totalCost = 0.0;
		public PFResult(AID roadAID, double totalCost)
		{
			this.roadAID = roadAID;
			this.totalCost = totalCost;
		}
	}
	
	/***************************************************************************
	 * Variables
	 **************************************************************************/
	
	// agent communicator
	private PathFindingCommunicator communicator = new PathFindingCommunicator(this);
	
	// maps
	private Map<AID, Vector2D> nodePositionMap = new HashMap<AID, Vector2D>();
	private Map<AID, PFNode> roadNameToNodeMap = new HashMap<AID, PFNode>();
	private Map<AID, ArrayList<PFNode>> endNodeNameToNodeMap = new HashMap<AID, ArrayList<PFNode>>();
	private Map<PFNode, ArrayList<PFNode>> childNodeMap = new HashMap<PFNode, ArrayList<PFNode>>();
	private List<PFNode> allNodes = new ArrayList<PFNode>();
	private List<PFNode> openNodes = new ArrayList<PFNode>();
	private List<PFNode> closedNodes = new ArrayList<PFNode>();

	/***************************************************************************
	 * Public methods
	 **************************************************************************/
	
	/**
	 * Loads maps for path finding agent.
	 *
	 * @param nodeList the ArrayList containing NodeAgents.
	 * @param roadList the ArrayList containing RoadAgents.
	 */
	void loadMap(ArrayList<NodeAgent> nodeList, ArrayList<RoadAgent> roadList)
	{
		for (NodeAgent node : nodeList)
		{
			AID nodeAID = node.getAID();
			nodePositionMap.put(nodeAID, node.getPosition());
//			nodeNameToNodeMap.put(nodeAID,node);
		}
		for (RoadAgent road : roadList)
		{
			PFNode node = new PFNode(road);
			allNodes.add(node);

			roadNameToNodeMap.put(road.getAID(), node);

			ArrayList<PFNode> parents = endNodeNameToNodeMap.get(road.getEndNode());
			if (parents == null)
			{
				parents = new ArrayList<PFNode>();
				endNodeNameToNodeMap.put(road.getEndNode(), parents);
			}
			parents.add(node);

			childNodeMap.put(node, new ArrayList<PFNode>());
		}
		for (RoadAgent road : roadList)
		{
			PFNode child = roadNameToNodeMap.get(road.getAID());
			ArrayList<PFNode> parents = endNodeNameToNodeMap.get(road.getStartNode());
			for (PFNode parent : parents)
			{
				ArrayList<PFNode> children = childNodeMap.get(parent);
				children.add(child);
			}
		}
		//<editor-fold defaultstate="collapsed" desc="debug">
//		for (PFNode parent : allNodes)
//		{
//			System.out.println("P : "+parent);
//			ArrayList<PFNode> children = childNodeMap.get(parent);
//			for (PFNode child : children)
//			{
//				System.out.println("C :  |"+child);
//			}
//		}
//		for (PFNode parent : allNodes)
//		{
//			System.out.println("endName : "+parent.nodeAID);
//			ArrayList<PFNode> children = endNodeNameToNodeMap.get(parent.nodeAID);
//			for (PFNode child : children)
//			{
//				System.out.println("  |"+child);
//			}
//		}
//
//		// find path
//		AID finalNodeAID	= new AID("Postaja2", false);
//		AID sourceNodeAID	= new AID("Postaja1", false);
//
//		AID roadAID = findNextOptimalRoad(sourceNodeAID, finalNodeAID);
//
//		System.err.println("WAHOO :: " + roadAID);

		//</editor-fold>
	}
	
	/***************************************************************************
	 * Private methods
	 **************************************************************************/
	
	//<editor-fold defaultstate="collapsed" desc="Private path finding methods">
	
	private PFResult findNextOptimalRoad(AID sourceNodeAID, AID finalNodeAID)
	{
		PFNode currentNode = endNodeNameToNodeMap.get(sourceNodeAID).get(0);
		close(currentNode);

		while (!(currentNode.nodeAID).equals(finalNodeAID))
		{
			ArrayList<PFNode> children = childNodeMap.get(currentNode);

			for (PFNode child : children)
			{
				if (child.closed)
				{
					// ignore
				}
				else if (!child.opened)
				{
					// open child with parent
					open(child, currentNode);
					// set the final position for F calculation
					child.setFinalPosition(nodePositionMap.get(finalNodeAID));
				}
				else
				{
					// check to see if this path to that square is better, using G cost as the measure.
					if (currentNode.G  + child.C < child.G)
					{
						ArrayList<PFNode> nodes = endNodeNameToNodeMap.get(child.nodeAID);
						for (PFNode node : nodes)
						{
							node.setParentNode(currentNode);
						}
					}
				}
			}

			int index = findLowestCost();

			currentNode = openNodes.get(index);
			close(currentNode);
		}
		
		double cost = currentNode.C;
		// loopback
		while (currentNode.parentNode.parentNode != null)
		{
			currentNode = currentNode.parentNode;
			cost += currentNode.C;
		}
		
		// cleanup
		cleanup();

		return new PFResult(currentNode.roadAID, cost);
	}

	private void open(PFNode child, PFNode parent)
	{
//		System.out.println("Open "+child.nodeAID);

		openNodes.add(child);

		ArrayList<PFNode> nodes = endNodeNameToNodeMap.get(child.nodeAID);
		for (PFNode node : nodes)
		{
			node.opened = true;
			node.setParentNode(parent);
		}
	}

	private void close(PFNode child)
	{
//		System.out.println("Close "+child.nodeAID);
		ArrayList<PFNode> nodes = endNodeNameToNodeMap.get(child.nodeAID);
		for (PFNode node : nodes)
		{
			node.closed = true;
			openNodes.remove(node);
			closedNodes.add(node);
		}
	}

	private int findLowestCost()
	{
		int index = 0;
		double cost = 1000;
		for (int i = 0; i < openNodes.size(); i++)
		{
			PFNode node = openNodes.get(i);
			if (node.F < cost)
			{
				cost = node.F;
				index = i;
			}
		}
		return index;
	}

	private void cleanup()
	{
		for (PFNode node : allNodes)
		{
			node.opened = false;
			node.closed = false;
			node.setParentNode(null);
		}
		openNodes.clear();
		closedNodes.clear();
	}
	//</editor-fold>
	
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
		//adds the behviours to the agent
		addBehaviour(new PathFindingAgent.RoadUpdateBehaviour(this));
		addBehaviour(new PathFindingAgent.PathFindingBehaviour(this));
	}

	/**
	 * Behaviour class for PathFindingAgent. It extends CyclicBehaviour.
	 */
	public class RoadUpdateBehaviour extends CyclicBehaviour
	{
		/**
		 * Constructor for PathFindingAgent's behaviour class.
		 *
		 * @param a
		 *		The agent which behaviour is being applied to.
		 */
		public RoadUpdateBehaviour(Agent a)
		{
			super(a);
		}

		/**
		 * Method that performs actions in RoadUpdateBehaviour class. It
		 * gets called each time Jade platform has spare resources.
		 */
		@Override
		public void action()
		{
			// check road weight update messages
			ACLMessage weightMessages = communicator.checkRoadWeightUpdates();
			while (weightMessages != null)
			{
				// update node road weight
				double weight = Double.parseDouble(weightMessages.getUserDefinedParameter("weight"));
				PFNode node = roadNameToNodeMap.get(weightMessages.getSender());
				if (node != null)
				{
					node.setRoadWeight(weight);
				}
				// check if new messages have arrived during the proces
				weightMessages = communicator.checkRoadWeightUpdates();
			}
			// block the cycle until new messages arrive
			block();
		}
	}
	
	/**
	 * Behaviour class for PathFindingAgent. It extends CyclicBehaviour.
	 */
	public class PathFindingBehaviour extends CyclicBehaviour
	{

		/**
		 * Constructor for PathDindingAgent's behaviour class.
		 *
		 * @param a
		 *		The agent which behaviour is being applied to.
		 */
		public PathFindingBehaviour(Agent a)
		{
			super(a);
		}

		/**
		 * Method that performs actions in RoadUpdateBehaviour class. It
		 * gets called each time Jade platform has spare resources.
		 */
		@Override
		public void action()
		{
			// check path finding request messages
			ACLMessage pathFindingRequest = communicator.checkPathFindingRequests();
			while (pathFindingRequest != null)
			{
				// find path
				AID finalNodeAID = new AID(pathFindingRequest.getUserDefinedParameter("destination"), false);
				AID sourceNodeAID = pathFindingRequest.getSender();
				
				PFResult result = findNextOptimalRoad(sourceNodeAID, finalNodeAID);

				communicator.informPathFindingResult(pathFindingRequest, result.roadAID, result.totalCost);
				
				// check if new messages have arrived during the proces
				pathFindingRequest = communicator.checkPathFindingRequests();
			}
			// block the cycle until new messages arrive
			block();
		}
	}
}
