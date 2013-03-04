package smartpod;

import com.janezfeldin.Math.Vector2D;
import jade.core.AID;

/**
 * The path finding node wrapper class.
 * 
 * @author Andreas
 */
public class PFNode
{
	/**
	 * The weights used for cost calculation.
	 */
	private static final double distanceWeight	= 0.01;
	private static final double heuristicWeight = 0.01;
	
	/**
	 * The corresponding endNode position.
	 */
	private Vector2D position	= null;
	
	/**
	 * The parent PFNode in the pathfinding tree.
	 */
	public PFNode parentNode	= null;
	/**
	 * The AID of the road agent that this PFNode represents.
	 */
	public AID roadAID			= null;
	/**
	 * The AID of the node agent that is on the end of the road.
	 */
	public AID nodeAID			= null;
	/**
	 * The road distance is calculated from the road start and end position 
	 * multiplied my the distanceWeight factor.
	 */
	public double D = 0.0;
	/**
	 * The movement cost needed to move from the parent node.
	 * It is calculated as the sum of road weight and the road distance.
	 * 
	 * C = D + W
	 */
	public double C = 0.0;
	/**
	 * The full movement cost of the parent node.
	 */
	public double P = 0.0;
	/**
	 * The full movement cost of the node.
	 * It is calculated as the sum of the full parent movement cost and 
	 * the cost to move from the parent node to this node.
	 * 
	 * G = P + C
	 */
	public double G = 0.0;
	/**
	 * The estimated movement cost to move to the end node.
	 */
	public double H = 0.0;
	/**
	 * The total cost sum used for cost comparison.
	 * 
	 * F = G + H
	 */
	public double F = 0.0;
	
	/**
	 * Boolean value stating if the node has already been opened.
	 */
	public boolean opened = false;
	/**
	 * Boolean value stating if the node has already been closed.
	 */
	public boolean closed = false;
	

	/**
	 * Constructor using RoadAgent.
	 * It calculates all the constants according with the RoadAgent.
	 * 
	 * @param road
	 *		The RoadAgent which PFNode represents.
	 */
	public PFNode(RoadAgent road)
	{
		this.nodeAID = road.getEndNode();
		this.roadAID = road.getAID();
		this.D = road.getStartPosition().dist(road.getEndPosition())*distanceWeight;
		this.C = D + road.getWeight();
		this.G = P + C;
		this.position = road.getEndPosition();
	}
	
	/**
	 * Constructor using node AID.
	 * It is used for constructing the root PFNode.
	 * 
	 * @param nodeAID
	 *		The AID of the root node agent.
	 */
	public PFNode(AID nodeAID)
	{
		this.nodeAID = nodeAID;
	}

	/**
	 * Updates the road weight and all depending costs.
	 * 
	 * @param weight
	 *		The weight updated.
	 */
	public void setRoadWeight(double weight)
	{
		this.C = D + weight;
		this.G = P + C;
		this.F = G + H;
	}
	
	/**
	 * Updates the final (end node) position and all depending costs.
	 * 
	 * @param finalPosition
	 *		The Vector2D position of the final node.
	 */
	public void setFinalPosition(Vector2D finalPosition)
	{
		this.H = finalPosition.dist(position)*heuristicWeight + 1;
		this.F = G + H;
	}
	
	/**
	 * Sets the parent PFNode and updates all depending costs.
	 * 
	 * @param parentNode
	 *		The parent PFNode.
	 */
	public void setParentNode(PFNode parentNode)
	{
		this.parentNode = parentNode;
		this.P = (parentNode != null ? parentNode.G + parentNode.C : 0.0);
		this.G = P + C;
		this.F = G + H;
	}
	
	/**
	 * Creates and returns the representing string value of the object.
	 * 
	 * @return
	 *		The string value.
	 */
	@Override
	public String toString()
	{
		return "===="+roadAID.getLocalName()+"===>"+nodeAID.getLocalName();//+" parentNode : "+parentNode;
	}
}
