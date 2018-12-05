package HW1;

import org.jetbrains.annotations.Contract;

/**
 * A GeoSegment models a straight line segment on the earth. GeoSegments 
 * are immutable.
 * <p>
 * A compass heading is a non-negative real number less than 360. In compass
 * headings, north = 0, east = 90, south = 180, and west = 270.
 * <p>
 * When used in a map, a GeoSegment might represent part of a street,
 * boundary, or other feature.
 * As an example usage, this map
 * <pre>
 *  Trumpeldor   a
 *  Avenue       |
 *               i--j--k  Hanita
 *               |
 *               z
 * </pre>
 * could be represented by the following GeoSegments:
 * ("Trumpeldor Avenue", a, i), ("Trumpeldor Avenue", z, i),
 * ("Hanita", i, j), and ("Hanita", j, k).
 * </p>
 * 
 * </p>
 * A name is given to all GeoSegment objects so that it is possible to
 * differentiate between two GeoSegment objects with identical
 * GeoPoint endpoints. Equality between GeoSegment objects requires
 * that the names be equal String objects and the end points be equal
 * GeoPoint objects.
 * </p>
 *
 * <b>The following fields are used in the specification:</b>
 * <pre>
 *   name : String       // name of the geographic feature identified
 *   p1 : GeoPoint       // first endpoint of the segment
 *   p2 : GeoPoint       // second endpoint of the segment
 *   length : real       // straight-line distance between p1 and p2, in kilometers
 *   heading : angle     // compass heading from p1 to p2, in degrees
 * </pre>
 **/
public class GeoSegment  {

	private final String name;
	private final GeoPoint p1;
	private final GeoPoint p2;
	private final Double length;
	private final Double heading;

	// Rep. Inv. for each GeoSegment:
	// this.name is a non empty string of letters.
	// this.p1 and this.p2 can be of the same value.
	// this.length >= 0.
	// this.heading is a real value and 360 > this.heading >= 0. If (p1.isEqual(p2) == true) heading is not defined.

	// Abstraction Function:
	// A GeoSegment with a name this.name is determined between two GeoPoints this.p1 and this.p2, hence it's a straight
    // line with a non-negative length this.length and with a heading this.heading from this.p1 to this.p2.

	/**
     * Constructs a new GeoSegment with the specified name and endpoints.
     * @requires name != null && p1 != null && p2 != null
     * @effects constructs a new GeoSegment with the specified name and endpoints.
     **/
    public GeoSegment(String name, GeoPoint p1, GeoPoint p2)
	{
		assert p1 != null && p2 != null && name != null;
  		this.name = name;
  		this.p1 = p1;
  		this.p2 = p2;
  		this.length = p1.distanceTo(p2);
		if (!this.p1.equals(p2))
		{
			this.heading = p1.headingTo(p2);
		}
		else
		{
			this.heading = null;
		}
  		this.checkRep();
  	}


  	/**
     * Returns a new GeoSegment like this one, but with its endpoints reversed.
     * @return a new GeoSegment gs such that gs.name = this.name
     *         && gs.p1 = this.p2 && gs.p2 = this.p1
     **/
  	public GeoSegment reverse()
	{
	    this.checkRep();
  		// Construct the reversed GeoSegment
        return new GeoSegment(this.name, this.p2, this.p1);
  	}


	/**
	 * Returns the name of this GeoSegment.
	 *
	 * @return the name of this GeoSegment.
	 */
	public String getName()
	{
		this.checkRep();
		return this.name;
	}


	/**
	 * Returns first endpoint of the segment.
	 *
	 * @return first endpoint of the segment.
	 */
	public GeoPoint getP1()
	{
		this.checkRep();
		return this.p1;
	}


	/**
	 * Returns second endpoint of the segment.
	 *
	 * @return second endpoint of the segment.
	 */
	public GeoPoint getP2()
	{
		this.checkRep();
		return this.p2;
	}


  	/**
  	 * Returns the length of the segment.
     * @return the length of the segment, using the flat-surface, near the
     *         Technion approximation.
     */
  	public double getLength()
	{
		this.checkRep();
		return this.p1.distanceTo(p2);
  	}


  	/**
  	 * Returns the compass heading from p1 to p2.
     * @requires this.length != 0
     * @return the compass heading from p1 to p2, in degrees, using the
     *         flat-surface, near the Technion approximation.
     **/
  	public double getHeading()
	{
  		assert  this.length != 0: "Can't calculate heading since length = 0";
		this.checkRep();
  		return this.heading;
  	}


  	/**
     * Compares the specified Object with this GeoSegment for equality.
     * @return gs != null && (gs instanceof GeoSegment)
     *         && gs.name = this.name && gs.p1 = this.p1 && gs.p2 = this.p2
   	 **/
  	@Override
  	public boolean equals(Object gs)
	{
		this.checkRep();

		if (!(gs instanceof GeoSegment)) // No need to check for null. this condition covers it.
			return false;
		GeoSegment geoSegment = (GeoSegment) gs;
		return this.p1.equals(geoSegment.p1) && this.p2.equals(geoSegment.p2) && this.name.equals(geoSegment.getName()); // The last method is a String method.
  	}


  	/**
  	 * Returns a hash code value for this.
     * @return a hash code value for this.
     **/
  	public int hashCode()
    {
    	// This implementation will work, but you may want to modify it 
    	// for improved performance. 

    	return 1;
  	}


  	/**
  	 * Returns a string representation of this.
     * @return a string representation of this.
     **/
  	@Override
  	public String toString()
    {
        this.checkRep();
        return "GeoSegment: " + this.getName() + "\n" + "           " +
                "p1: " + this.getP1() + "\n" + "           p2: " + this.getP2() + "\n"
                + "           Length: " + this.getLength() + "\n" +
                "           Heading: " + (this.heading != null ? (this.getHeading() + "\n") : "Not Defined\n");
  	}

    private void checkRep()
    {
        assert this.name != null && this.p1 != null && this.p2 != null && this.length != null && this.heading != null;
        assert !this.name.isEmpty();
        assert this.length >= 0;
        assert this.heading >= 0 && this.heading < 360;
    }
}

