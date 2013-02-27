package com.janezfeldin.Math;

/**
 * Class Vector2D provides a two-dimensional mathematical vector of type
 * <TT>double</TT>. The vector's components are stored in the fields <TT>x</TT>
 * and <TT>y</TT>.
 */
public class Vector2D
{

	/**
	 * X component.
	 */
	public double x;
	/**
	 * Y component.
	 */
	public double y;

	/**
	 * Construct a new vector. The X and Y components are initialized to 0.
	 */
	public Vector2D()
	{
	}

	/**
	 * Construct a new vector with the given X and Y components.
	 *
	 * @param x Initial X component.
	 * @param y Initial Y component.
	 */
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Construct a new vector that is a copy of the given vector.
	 *
	 * @param theVector Vector to copy.
	 */
	public Vector2D(Vector2D theVector)
	{
		this.x = theVector.x;
		this.y = theVector.y;
	}
	
	/**
	 * Construct a new vector with the given string representation.
	 * The string should be of the form
	 * <TT>"(</TT><I>x</I><TT>,</TT><I>y</I><TT>)"</TT>.
	 *
	 * @param stringRepresentation The string representation.
	 */
	public Vector2D(String stringRepresentation)
	{
		int s = stringRepresentation.length();
		String []components = stringRepresentation.substring(1,s-1).split(",");
		this.x = Double.parseDouble(components[0]);
		this.y = Double.parseDouble(components[1]);
	}

	/**
	 * Clear this vector. The X and Y components are set to 0.
	 *
	 * @return This vector, set to (0,0).
	 */
	public Vector2D clear()
	{
		this.x = 0.0;
		this.y = 0.0;
		return this;
	}

	/**
	 * Assign the given X and Y components to this vector.
	 *
	 * @param x X component.
	 * @param y Y component.
	 *
	 * @return This vector, set to (<TT>x</TT>,<TT>y</TT>).
	 */
	public Vector2D assign(double x, double y)
	{
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Assign (copy) the given vector to this vector.
	 *
	 * @param theVector Vector to copy.
	 *
	 * @return This vector, set to <TT>theVector</TT>.
	 */
	public Vector2D assign(Vector2D theVector)
	{
		this.x = theVector.x;
		this.y = theVector.y;
		return this;
	}

	/**
	 * Add the given vector to this vector.
	 *
	 * @param theVector Vector to add.
	 *
	 * @return This vector, set to <TT>this</TT> + <TT>theVector</TT>.
	 */
	public Vector2D add(Vector2D theVector)
	{
		this.x += theVector.x;
		this.y += theVector.y;
		return this;
	}

	/**
	 * Subtract the given vector from this vector.
	 *
	 * @param theVector Vector to subtract.
	 *
	 * @return This vector, set to <TT>this</TT> - <TT>theVector</TT>.
	 */
	public Vector2D sub(Vector2D theVector)
	{
		this.x -= theVector.x;
		this.y -= theVector.y;
		return this;
	}

	/**
	 * Multiply this vector by the given scalar.
	 *
	 * @param a Scalar.
	 *
	 * @return This vector, set to <TT>this</TT> * <TT>a</TT>.
	 */
	public Vector2D mul(double a)
	{
		this.x *= a;
		this.y *= a;
		return this;
	}

	/**
	 * Divide this vector by the given scalar.
	 *
	 * @param a Scalar.
	 *
	 * @return This vector, set to <TT>this</TT> / <TT>a</TT>.
	 */
	public Vector2D div(double a)
	{
		this.x /= a;
		this.y /= a;
		return this;
	}

	/**
	 * Negate this vector.
	 *
	 * @return This vector, set to -<TT>this</TT>.
	 */
	public Vector2D neg()
	{
		this.x = -this.x;
		this.y = -this.y;
		return this;
	}

	/**
	 * Rotate this vector 90 degrees counterclockwise.
	 *
	 * @return This vector, rotated.
	 */
	public Vector2D rotate90()
	{
		double tmp = this.x;
		this.x = -this.y;
		this.y = tmp;
		return this;
	}

	/**
	 * Rotate this vector 180 degrees.
	 *
	 * @return This vector, rotated.
	 */
	public Vector2D rotate180()
	{
		this.x = -this.x;
		this.y = -this.y;
		return this;
	}

	/**
	 * Rotate this vector 270 degrees counterclockwise (90 degrees clockwise).
	 *
	 * @return This vector, rotated.
	 */
	public Vector2D rotate270()
	{
		double tmp = this.x;
		this.x = this.y;
		this.y = -tmp;
		return this;
	}

	/**
	 * Normalize this vector. This vector is set to a unit vector pointing in
	 * the same direction.
	 *
	 * @return This vector, set to <TT>this</TT> / <TT>this.mag()</TT>.
	 */
	public Vector2D norm()
	{
		return this.div(this.mag());
	}

	/**
	 * Determine the dot product of this vector and the given vector.
	 *
	 * @param theVector Vector.
	 *
	 * @return Dot product.
	 */
	public double dot(Vector2D theVector)
	{
		return this.x * theVector.x + this.y * theVector.y;
	}

	/**
	 * Determine the magnitude of this vector. The magnitude of the vector
	 * (<I>x</I>,<I>y</I>) is
	 * (<I>x</I><SUP>2</SUP>&nbsp;+&nbsp;<I>y</I><SUP>2</SUP>)<SUP>1/2</SUP>.
	 *
	 * @return Magnitude.
	 */
	public double mag()
	{
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Determine the squared magnitude of this vector. The squared magnitude of
	 * the vector (<I>x</I>,<I>y</I>) is
	 * (<I>x</I><SUP>2</SUP>&nbsp;+&nbsp;<I>y</I><SUP>2</SUP>).
	 *
	 * @return Squared magnitude.
	 */
	public double sqrMag()
	{
		return x * x + y * y;
	}

	/**
	 * Determine the argument of this vector. The argument of the vector
	 * (<I>x</I>,<I>y</I>) is tan<SUP>-1</SUP> (<I>y</I>/<I>x</I>).
	 *
	 * @return Argument.
	 */
	public double arg()
	{
		return Math.atan2(y, x);
	}

	/**
	 * Determine the distance (magnitude of the difference) between this vector
	 * and the given vector.
	 *
	 * @param theVector Vector.
	 *
	 * @return Distance between this vector and <TT>theVector</TT>.
	 */
	public double dist(Vector2D theVector)
	{
		double dx = this.x - theVector.x;
		double dy = this.y - theVector.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Determine the squared distance (squared magnitude of the difference)
	 * between this vector and the given vector.
	 *
	 * @param theVector Vector.
	 *
	 * @return Squared distance between this vector and <TT>theVector</TT>.
	 */
	public double sqrDist(Vector2D theVector)
	{
		double dx = this.x - theVector.x;
		double dy = this.y - theVector.y;
		return dx * dx + dy * dy;
	}

	/**
	 * Returns a string version of this vector. The string is of the form
	 * <TT>"(</TT><I>x</I><TT>,</TT><I>y</I><TT>)"</TT>.
	 *
	 * @return String version.
	 */
	public String stringRepresentation()
	{
		return "(" + x + "," + y + ")";
	}
}