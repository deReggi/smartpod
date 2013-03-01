package smartpod;

import com.janezfeldin.Math.Vector2D;
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
	// agent communicator

	private PathFindingCommunicator communicator = new PathFindingCommunicator(this);
	// maps
	private Map<String, Vector2D> nodePositionMap = new HashMap<String, Vector2D>();
	private Map<String, PFNode> roadNameToNodeMap = new HashMap<String, PFNode>();
	private Map<String, ArrayList<PFNode>> endNodeNameToNodeMap = new HashMap<String, ArrayList<PFNode>>();
	private Map<PFNode, ArrayList<PFNode>> childNodeMap = new HashMap<PFNode, ArrayList<PFNode>>();
	private List<PFNode> allNodes = new ArrayList<PFNode>();
	private List<PFNode> openNodes = new ArrayList<PFNode>();
	private List<PFNode> closedNodes = new ArrayList<PFNode>();

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
			String nodeName = node.getLocalName();
			nodePositionMap.put(nodeName, node.getPosition());
//			nodeNameToNodeMap.put(nodeName,node);
		}
		for (RoadAgent road : roadList)
		{
			PFNode node = new PFNode(road);
			allNodes.add(node);

			roadNameToNodeMap.put(road.getLocalName(), node);

			ArrayList<PFNode> parents = endNodeNameToNodeMap.get(road.endNode);
			if (parents == null)
			{
				parents = new ArrayList<PFNode>();
				endNodeNameToNodeMap.put(road.endNode, parents);
			}
			parents.add(node);

			childNodeMap.put(node, new ArrayList<PFNode>());
		}
		for (RoadAgent road : roadList)
		{
			PFNode child = roadNameToNodeMap.get(road.getLocalName());
			ArrayList<PFNode> parents = endNodeNameToNodeMap.get(road.startNode);
			for (PFNode parent : parents)
			{
				ArrayList<PFNode> children = childNodeMap.get(parent);
				children.add(child);
			}
		}
		
		// find path
		String finalNodeName = "Postaja6";
		String sourceNodeName = "Postaja5";
		
		PFNode currentNode = endNodeNameToNodeMap.get(sourceNodeName).get(0);
		close(currentNode);

		while (!(currentNode.nodeName).equals(finalNodeName))
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
					child.setFinalPosition(nodePositionMap.get(finalNodeName));
				}
				else
				{
					// check to see if this path to that square is better, using G cost as the measure.
					double childG = currentNode.G + child.C;
					double otherChildG = 0.0;
					ArrayList<PFNode> otherChildren = childNodeMap.get(child.parentNode);
					for (PFNode otherChild : otherChildren)
					{
						if (otherChild.nodeName.equals(child.nodeName))
						{
							otherChildG = otherChild.G + otherChild.C;
							break;
						}
					}
					if (childG < otherChildG)
					{
						ArrayList<PFNode> nodes = endNodeNameToNodeMap.get(child.nodeName);
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

		while (currentNode.parentNode != null)
		{
			System.err.println(currentNode.roadName);
			currentNode = currentNode.parentNode;
		}
		String roadName = currentNode.roadName;

		System.err.println("WAHOO :: "+roadName);

		// cleanup
		cleanup();
	}

	private void open(PFNode child, PFNode parent)
	{
//		System.out.println("Open "+child.nodeName);
		ArrayList<PFNode> nodes = endNodeNameToNodeMap.get(child.nodeName);
		for (PFNode node : nodes)
		{
			node.opened = true;
			openNodes.add(node);
			node.setParentNode(parent);
		}
	}

	private void close(PFNode child)
	{
//		System.out.println("Close "+child.nodeName);
		ArrayList<PFNode> nodes = endNodeNameToNodeMap.get(child.nodeName);
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

	/**
	 * This method gets called when agent is started. It adds the desired
	 * behaviour to the agent.
	 */
	@Override
	protected void setup()
	{
		//adds the behviour to the agent
		addBehaviour(new PathFindingAgent.PathFindingAgentBehaviour(this));
	}

	/**
	 * Behaviour class for PathFindingAgent. It extends CyclicBehaviour.
	 */
	public class PathFindingAgentBehaviour extends CyclicBehaviour
	{

		/**
		 * Constructor for PathDindingAgent's behaviour class.
		 *
		 * @param a the agent to which behaviour is being applied.
		 */
		public PathFindingAgentBehaviour(Agent a)
		{
			super(a);
		}

		/**
		 * Method that performs actions in PathFindingAgentBehaviour class. It
		 * gets called each time Jade platform has spare resources.
		 */
		@Override
		public void action()
		{
			// check road weight update messages
			ArrayList<ACLMessage> weightMessages = communicator.checkRoadWeightUpdates();
			for (ACLMessage msg : weightMessages)
			{
//				System.out.println("com-env : "+msg.getContent());

				// update node road weight
				double weight = Double.parseDouble(msg.getUserDefinedParameter("weight"));
				String roadName = msg.getSender().getLocalName();
				PFNode node = roadNameToNodeMap.get(roadName);
				if (node != null)
				{
					node.setRoadWeight(weight);
				}
			}

			// check path finding request messages
			ArrayList<ACLMessage> pathFindingMessages = communicator.checkPathFindingRequests();
			for (ACLMessage msg : pathFindingMessages)
			{
//				System.out.println("com-env : "+msg.getContent());

				// find path
				String finalNodeName = msg.getUserDefinedParameter("destination");
				String sourceNodeName = msg.getSender().getLocalName();
				
				PFNode currentNode = endNodeNameToNodeMap.get(sourceNodeName).get(0);
				close(currentNode);
				
				while (!(currentNode.nodeName).equals(finalNodeName))
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
							child.setFinalPosition(nodePositionMap.get(finalNodeName));
						}
						else
						{
							// check to see if this path to that square is better, using G cost as the measure.
							double childG = currentNode.G + child.C;
							double otherChildG = 0.0;
							ArrayList<PFNode> otherChildren = childNodeMap.get(child.parentNode);
							for (PFNode otherChild : otherChildren)
							{
								if (otherChild.nodeName.equals(child.nodeName))
								{
									otherChildG = otherChild.G + otherChild.C;
									break;
								}
							}
							if (childG < otherChildG)
							{
								ArrayList<PFNode> nodes = endNodeNameToNodeMap.get(child.nodeName);
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
				
				// loopback
				while (currentNode.parentNode != null)
				{
					System.out.println(currentNode.nodeName);
					currentNode = currentNode.parentNode;
				}
				String roadName = currentNode.roadName;

				communicator.informPathFindingResult(msg, roadName);

				// cleanup
				cleanup();

//				String node = msg.getSender().getLocalName();
//				String road = "Pot1";
//				
//				if (node.equals("Postaja3"))
//				{
//					road = "Pot12";
//				}
//				else if (node.equals("Postaja4"))
//				{
//					road = "Pot19";
//				}
//				else if (node.equals("Križišče2"))
//				{
//					road = "Pot13";
//				}
//				else if (node.equals("Križišče4"))
//				{
//					road = "Pot18";
//				}
//				communicator.informPathFindingResult(msg, road);
			}
		}
	}
}
