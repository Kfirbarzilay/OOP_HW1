package HW1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A GeoFeature represents a route from one location to another along a
 * single geographic feature. GeoFeatures are immutable.
 * <p>
 * GeoFeature abstracts over a sequence of GeoSegments, all of which have
 * the same name, thus providing a representation for nonlinear or nonatomic
 * geographic features. As an example, a GeoFeature might represent the
 * course of a winding river, or travel along a road through intersections
 * but remaining on the same road.
 * <p>
 * GeoFeatures are immutable. New GeoFeatures can be constructed by adding
 * a segment to the end of a GeoFeature. An added segment must be properly
 * oriented; that is, its p1 field must correspond to the end of the original
 * GeoFeature, and its p2 field corresponds to the end of the new GeoFeature,
 * and the name of the GeoSegment being added must match the name of the
 * existing GeoFeature.
 * <p>
 * Because a GeoFeature is not necessarily straight, its length - the
 * distance traveled by following the path from start to end - is not
 * necessarily the same as the distance along a straight line between
 * its endpoints.
 * <p>
 * <b>The following fields are used in the specification:</b>
 * <pre>
 *   start : GeoPoint       // location of the start of the geographic feature
 *   end : GeoPoint         // location of the end of the geographic feature
 *   startHeading : angle   // direction of travel at the start of the geographic feature, in degrees
 *   endHeading : angle     // direction of travel at the end of the geographic feature, in degrees
 *   geoSegments : sequence	// a sequence of segments that make up this geographic feature
 *   name : String          // name of geographic feature
 *   length : real          // total length of the geographic feature, in kilometers
 * </pre>
 **/
public class GeoFeature {
	
	// Implementation hint:
	// When asked to return an Iterator, consider using the iterator() method
	// in the List interface. Two nice classes that implement the List
	// interface are ArrayList and LinkedList. If comparing two Lists for
	// equality is needed, consider using the equals() method of List. More
	// info can be found at:
	//   http://docs.oracle.com/javase/8/docs/api/java/util/List.html
	
	private final GeoPoint start;
	private final GeoPoint end;
	private final Double startHeading;
	private final Double endHeading;
	private final List<GeoSegment> geoSegments;
	private final String name;
	private final Double length;

	// Representation invariant:
	// this.start and this.end hold for the GeoPoint checkRep() method.
	// this.start and this.end hold for the GeoSegment checkRep() method.
	// this.startHeading and this.endHeading are non-negative real values less than 360.
	// this.geoSegments is not empty and all segments have the same name.
	// this.name is a non empty string of letters.
	// this.length >= 0.

	// Abstraction Function:
	// A GeoFeature with a name, this.name, that has a starting point, this.start, and an ending point, this.end,
	// consisted of a non empty list of GeoSegments, this.geoSegments all with the same name, this.name, such that the
	// first segment is connected to this.start and with a heading equal to this.startHeading and the last segment is
	// connected to this.end with a heading equal to this.endHeading.

	/**
     * Constructs a new GeoFeature.
     * @requires gs != null
     * @effects Constructs a new GeoFeature, r, such that
     *	        r.name = gs.name &&
     *          r.startHeading = gs.heading &&
     *          r.endHeading = gs.heading &&
     *          r.start = gs.p1 &&
     *          r.end = gs.p2
     **/
  	public GeoFeature(GeoSegment gs)
    {
		assert gs != null : "GeoFeature c'tor, gs is null";
		this.start = gs.getP1();
		this.end = gs.getP2();
		this.startHeading = gs.getHeading();
		this.endHeading = gs.getHeading(); // TODO: Check why we use the same heading.

		// Add the GeoSegment to the list
		this.geoSegments = new ArrayList<>();
		this.geoSegments.add(gs);

		// This is the c'tor of GeoFeature hence it's name is first defined here.
		this.name = gs.getName();
  		this.length = gs.getLength();
		this.checkRep();
	}

//
// Constructs a new GeoFeature with an existing geoSegments list.
// @requires geoSegments != null && !geoSegments.isEmpty().
// @effects Constructs a new GeoFeature, r, such that
//		r.name = all of the GeoSegments name &&
//	 	r.startHeading = geoSegments[0].heading &&
//	 	r.endHeading = geoSegments[geoSegments.size() - 1].heading &&
// 		r.start = geoSegments[0].p1 &&
//	 	r.end = geoSegments[geoSegments.size() - 1].p2
	private GeoFeature(List<GeoSegment> geoSegments)
	{
		assert (geoSegments != null); // Don't want to end with a statement so we don't cause Rep. exposure.

		// Get the name of the GeoFeature which is the name of the geoSegments.
		this.name = geoSegments.get(0).getName();

		// Copying all the geoSegments in the new GeoFeature we are producing.
		this.geoSegments = new ArrayList<GeoSegment>(geoSegments);

		// initializing the start point and heading according to the first segment and initializing the end point
		// and heading according to the last segment in geoSegments.
		GeoSegment startSegment = this.geoSegments.get(0);
		this.start = startSegment.getP1();
		this.startHeading = startSegment.getHeading();

		GeoSegment endSegment = this.geoSegments.get(geoSegments.size() - 1);
		this.end  = endSegment.getP2();
		this.endHeading = endSegment.getHeading();

		// Calculating the length of the newly created GeoFeature.
		Iterator<GeoSegment> iter = this.geoSegments.iterator(); // Returns an iterator over the elements in this list in proper sequence.
		Double tmpLength = 0.0;
		while (iter.hasNext()) // True if the iteration has more elements
		{
			tmpLength += iter.next().getLength(); // At the beginning the iterator will point to the index before the first element.
		}
		this.length = tmpLength;
		checkRep();
	}


	/**
 	  * Returns name of geographic feature.
      * @return name of geographic feature
      */
  	public String getName()
    {
        this.checkRep();
  		return this.name;
  	}


    /**
     * Returns location of the start of the geographic feature.
     *
     * @return location of the start of the geographic feature.
     */
    public GeoPoint getStart()
    {
        this.checkRep();
        return this.start;
    }


    /**
     * Returns location of the end of the geographic feature.
     *
     * @return location of the end of the geographic feature.
     */
    public GeoPoint getEnd()
    {
        this.checkRep();
        return this.end;
    }


  	/**
  	 * Returns direction of travel at the start of the geographic feature.
     * @return direction (in standard heading) of travel at the start of the
     *         geographic feature, in degrees.
     */
  	public double getStartHeading()
    {
        this.checkRep();
        return this.startHeading;
  	}


    /**
     * Returns direction of travel at the end of the geographic feature.
     *
     * @return direction (in standard heading) of travel at the end of the
     * geographic feature, in degrees.
     */
    public double getEndHeading()
    {
        this.checkRep();
        return this.endHeading;
    }


    /**
     * Returns total length of the geographic feature, in kilometers.
     *
     * @return total length of the geographic feature, in kilometers.
     * NOTE: this is NOT as-the-crow-flies, but rather the total
     * distance required to traverse the geographic feature. These
     * values are not necessarily equal.
     */
    public double getLength()
    {
        this.checkRep();
        return this.length;
    }



    /**
     * Creates a new GeoFeature that is equal to this GeoFeature with gs
     * appended to its end.
     *
     * @return a new GeoFeature r such that
     * r.end = gs.p2 &&
     * r.endHeading = gs.heading &&
     * r.length = this.length + gs.length
     * @requires gs != null && gs.p1 = this.end && gs.name = this.name.
     **/
    public GeoFeature addSegment(GeoSegment gs)
    {
		assert gs != null : "In addSegment method gs is null";

		// We need to make sure the provided GeoSegment is obeying the contract.
		assert this.name.equals(gs.getName()) : "GeoSegment doesn't have the GeoFeature's name";
		assert this.end == gs.getP1() : "GeoSegment doesn't start where original GeoFeature started";

		// Constructing a new GeoSegment list with all the previous GeoFeature this.geoSegments and with
		// the new GeoSegment appended to end of the list.
		List<GeoSegment> newGeoSegmentsList = new ArrayList<GeoSegment>(this.geoSegments); // Using an ArrayList producer method.
		newGeoSegmentsList.add(gs);

		// Creating the new immutable GeoFeature using the private constructor method.
		GeoFeature newGeoFeature = new GeoFeature(newGeoSegmentsList);
		checkRep();
		return newGeoFeature;
    }


  	/**
     * Returns an Iterator of GeoSegment objects. The concatenation of the
     * GeoSegments, in order, is equivalent to this GeoFeature. All the
     * GeoSegments have the same name.
     * @return an Iterator of GeoSegments such that
     * <pre>
     *      this.start        = a[0].p1 &&
     *      this.startHeading = a[0].heading &&
     *      this.end          = a[a.length - 1].p2 &&
     *      this.endHeading   = a[a.length - 1].heading &&
     *      this.length       = sum(0 <= i < a.length) . a[i].length &&
     *      for all integers i
     *          (0 <= i < a.length-1 => (a[i].name == a[i+1].name &&
     *                                   a[i].p2d  == a[i+1].p1))
     * </pre>
     * where <code>a[n]</code> denotes the nth element of the Iterator.
     * @see HW1.GeoSegment
     */
  	public Iterator<GeoSegment> getGeoSegments()
	{
		// GeoFeature is immutable so we can't return an iterator to our geoSegments ArrayList.
		// We use an immutable wrapper method unmodifiableList in Collections interface
		checkRep();
		List<GeoSegment> immutableGeoSegmentsList  = Collections.unmodifiableList(this.geoSegments);
		return immutableGeoSegmentsList.iterator();
  	}


	/**
	 * Compares the argument with this GeoFeature for equality.
	 *
	 * @return o != null && (o instanceof GeoFeature) &&
	 * (o.geoSegments and this.geoSegments contain
	 * the same elements in the same order).
	 **/
	@Override
	public boolean equals(Object o)
	{
		this.checkRep();

		if (!(o instanceof GeoFeature)) // No need to check for null, this condition covers it.
			return false;

		GeoFeature geoFeature = (GeoFeature) o;

		// Check for equal GeoSegments in this.geoSegments and o.geoSegments.
		if (this.geoSegments.size() != geoFeature.geoSegments.size())
			return false;

		Iterator<GeoSegment> iter = geoFeature.getGeoSegments();
		Iterator<GeoSegment> thisIter = geoFeature.getGeoSegments();
		while (thisIter.hasNext()) // True if the iteration has more elements
		{
			if (!thisIter.next().equals(iter.next()))
			{
				return false; // Not equal GeoFeatures.
			}
		}

		return true;
	}

		/**
		 * Returns a hash code for this.
		 * @return a hash code for this.
		 **/
		public int hashCode ()
		{
		// This implementation will work, but you may want to modify it
		// improved performance.
			return 1;
		}


		/**
		 * Returns a string representation of this.
		 * @return a string representation of this.
		 **/
		public String toString ()
		{
			this.checkRep();
			String geoFeatureString =  "GeoFeature: \nname: " + this.name + "\nStarting Point: " + this.start
					+ "\nEnding Point: " + this.end + "\nStart Heading: " + this.startHeading + "\nEnd Heading: "
					+ this.endHeading + "\nLength: " + this.length + "\nGeoSegments are: \n";
			// Adding the GeoFeature GeoSegments to the string.
			Iterator<GeoSegment> iter = this.geoSegments.iterator();
			while (iter.hasNext())
			{
				geoFeatureString += "       " + iter.next().toString();
			}

			geoFeatureString += "\n";

			return geoFeatureString;
		}

	// Representation invariant:
	// this.start and this.end are instances of GeoPoint.
	// this.startHeading and this.endHeading are non-negative real values less than 360.
	// this.geoSegments is not empty and all segments have the same name.
	// this.name is a non empty string of letters.
	// this.length >= 0.

	// Abstraction Function:
	// A GeoFeature with a name, this.name, that has a starting point, this.start, and an ending point, this.end,
	// consisted of a non empty list of GeoSegments, this.geoSegments all with the same name, this.name, such that the
	// first segment is connected to this.start and with a heading equal to this.startHeading and the last segment is
	// connected to this.end with a heading equal to this.endHeading.
	private void checkRep()
	{
		assert this.name != null && !this.name.isEmpty() : "this.name is not valid";
		assert this.start != null : "this.start is not a GeoPoint in GeoFeature";
		assert this.end != null : "this.end is not a GeoPoint in GeoFeature";
		assert 0 <= this.startHeading && this.startHeading < 360 : "this.startHeading is not valid";
		assert 0 <= this.endHeading && this.endHeading < 360 : "this.endHeading is not valid";
		assert 	this.geoSegments != null && !this.geoSegments.isEmpty() : "this.geoSegments is not valid";
		assert this.length >= 0 : "this.length is not valid";

		// Now checking geoSegments Rep. Inv.:
		// for all integers i
		//     (0 <= i < geoSegments.length-1 => (geoSegments[i].name == geoSegments[i+1].name
		//     && geoSegments[i].p2  == geoSegments[i+1].p1))
		Iterator<GeoSegment> iter = this.geoSegments.iterator();
		GeoSegment currSeg = iter.next();
		while (iter.hasNext())
		{
			assert this.name.equals(currSeg.getName()) : "GeoSegment don't have the same name: " + currSeg ;
			if (iter.hasNext())
			{
				GeoSegment nextGeoSegment = iter.next();
				assert currSeg.getP2().equals(nextGeoSegment.getP1()): "GeoSegment " + currSeg + " and GeoSegment " + nextGeoSegment
						+ " are not connected";

				// Moving to the next GeoSegment
				currSeg = nextGeoSegment;
			}
		}
	}
}
