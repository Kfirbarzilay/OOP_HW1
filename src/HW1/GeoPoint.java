package HW1;

import java.lang.Math;
/**
 * A GeoPoint is a point on the earth. GeoPoints are immutable.
 * <p>
 * North latitudes and east longitudes are represented by positive numbers.
 * South latitudes and west longitudes are represented by negative numbers.
 * <p>
 * The code may assume that the represented points are nearby the Technion.
 * <p>
 * <b>Implementation direction</b>:<br>
 * The Ziv square is at approximately 32 deg. 46 min. 59 sec. N
 * latitude and 35 deg. 0 min. 52 sec. E longitude. There are 60 minutes
 * per degree, and 60 seconds per minute. So, in decimal, these correspond
 * to 32.783098 North latitude and 35.014528 East longitude. The 
 * constructor takes integers in millionths of degrees. To create a new
 * GeoPoint located in the the Ziv square, use:
 * <tt>GeoPoint zivCrossroad = new GeoPoint(32783098,35014528);</tt>
 * <p>
 * Near the Technion, there are approximately 110.901 kilometers per degree
 * of latitude and 93.681 kilometers per degree of longitude. An
 * implementation may use these values when determining distances and
 * headings.
 * <p>
 * <b>The following fields are used in the specification:</b>
 * <pre>
 *   latitude :  real        // latitude measured in degrees
 *   longitude : real        // longitude measured in degrees
 * </pre>
 **/
public class GeoPoint {

	/** Minimum value the latitude field can have in this class. **/
	public static final int MIN_LATITUDE  =  -90 * 1000000;
	    
	/** Maximum value the latitude field can have in this class. **/
	public static final int MAX_LATITUDE  =   90 * 1000000;
	    
	/** Minimum value the longitude field can have in this class. **/
	public static final int MIN_LONGITUDE = -180 * 1000000;
	    
	/** Maximum value the longitude field can have in this class. **/
	public static final int MAX_LONGITUDE =  180 * 1000000;

  	/**
   	 * Approximation used to determine distances and headings using a
     * "flat earth" simplification.
     */
  	public static final double KM_PER_DEGREE_LATITUDE = 110.901;

  	/**
     * Approximation used to determine distances and headings using a
     * "flat earth" simplification.
     */
  	public static final double KM_PER_DEGREE_LONGITUDE = 93.681;

    // The object's latitude.
  	private final int latitude;

    // The object's longitude.
    private final int longitude;

    // The millionth of a degree
    private final int millionth = 1000000;

    // Representation invariant for each GeoPoint gp:
    // (MIN_LATITUDE <= gp.latitude  && gp.latitude<= MAX_LATITUDE)
    //  && (MIN_LONGITUDE <= gp.longitude &&  gp.longitude <= MAX_LONGITUDE)

    // Abstraction Function:
    // A Geographic point constructed by latitude, gp.latitude, and longitude coordinate, gp.longitude.
    // Both are in millionth of a degree.

  	/**
  	 * Constructs GeoPoint from a latitude and longitude.
     * @requires the point given by (latitude, longitude) in millionths
   	 *           of a degree is valid such that:
   	 *           (MIN_LATITUDE <= latitude <= MAX_LATITUDE) and
     * 	 		 (MIN_LONGITUDE <= longitude <= MAX_LONGITUDE)
   	 * @effects constructs a GeoPoint from a latitude and longitude
     *          given in millionths of degrees.
   	 **/
  	public GeoPoint(int latitude, int longitude)
	{
  		this.latitude = latitude;
  		this.longitude = longitude;
		this.checkRep();
  	}

  	 
  	/**
     * Returns the latitude of this.
     * @return the latitude of this in millionths of degrees.
     */
  	public int getLatitude()
	{
  		return this.latitude;
  	}


  	/**
     * Returns the longitude of this.
     * @return the latitude of this in millionths of degrees.
     */
  	public int getLongitude()
    {
  		return this.longitude;
  	}


  	/**
     * Computes the distance between GeoPoints.
     * @requires gp != null
     * @return the distance from this to gp, using the flat-surface, near
     *         the Technion approximation.
     **/
  	public double distanceTo(GeoPoint gp)
    {
  		// Can't accept GeoPoint which is null
		assert gp != null: "Got a null GeoPoint";

		checkRep();

		// Calculates latitude distance in meters.
		double latitudeDelta = (double)(this.latitude - gp.getLatitude());
        latitudeDelta /= millionth; // from millionth of deg to deg.
        latitudeDelta = Math.pow(latitudeDelta * KM_PER_DEGREE_LATITUDE,2);

        // Calculates longitude distance in meters.
		double longitudeDelta = (double)(this.longitude - gp.getLongitude());
        longitudeDelta /= millionth; // from millionth of deg to deg.
        longitudeDelta = Math.pow(longitudeDelta * KM_PER_DEGREE_LONGITUDE,2);

        checkRep();

		// Ocleadean distance between two points.
		return Math.sqrt(latitudeDelta + longitudeDelta);
  	}


  	/**
     * Computes the compass heading between GeoPoints.
     * @requires gp != null && !this.equals(gp)
     * @return the compass heading h from this to gp, in degrees, using the
     *         flat-surface, near the Technion approximation, such that
     *         0 <= h < 360. In compass headings, north = 0, east = 90,
     *         south = 180, and west = 270.
     **/
  	public double headingTo(GeoPoint gp)
    {
        assert gp != null: "Got a null GeoPoint";
        assert !this.equals(gp) : "Same GeoPoint";

        this.checkRep();

        double latitudeDelta = (gp.latitude - this.latitude)  * KM_PER_DEGREE_LATITUDE;
        double longitudeDelta = (gp.longitude - this.longitude)  * KM_PER_DEGREE_LONGITUDE;

        // The calculation is based on how we want to place our coordinate system:
		// We want the angle to increase in range of [0,360) going from north and in a clockwise direction
		// (east ==> south ==> west ==> north). So we place the x axis to point north which is equivalent to latitude
		// positive direction and place the y axis to point east which is equivalent to the longitude
		// positive direction.
        double thetaInDegrees = Math.toDegrees(Math.atan2(longitudeDelta,latitudeDelta));

        if(thetaInDegrees < 0)
        { // A positive representation of the heading.
            thetaInDegrees += 360;
        }
        return thetaInDegrees;
  	}


  	/**
     * Compares the specified Object with this GeoPoint for equality.
     * @return gp != null && (gp instanceof GeoPoint) &&
     * 		   gp.latitude = this.latitude && gp.longitude = this.longitude
     **/
    @Override
    public boolean equals(Object gp)
    {
        this.checkRep();

        if (!(gp instanceof GeoPoint))
            return false;
        GeoPoint geoPoint = (GeoPoint)gp;
		this.checkRep();

        return (this.latitude == geoPoint.latitude) && (this.longitude == geoPoint.longitude);
  	}


  	/**
     * Returns a hash code value for this GeoPoint.
     * @return a hash code value for this GeoPoint.
   	 **/
  	@Override
  	public int hashCode()
    {
    	// This implementation will work, but you may want to modify it
    	// for improved performance.

    	return 1;
  	}


  	/**
     * Returns a string representation of this GeoPoint.
     * @return a string representation of this GeoPoint.
     **/
  	@Override
  	public String toString()
    {
		this.checkRep();
        return String.format("Geographic Point: latitude %d, longitude %d", this.latitude, this.longitude);
  	}

  	private void checkRep()
    {
        assert (MIN_LATITUDE <= this.latitude  && this.latitude<= MAX_LATITUDE)
                && (MIN_LONGITUDE <= this.longitude &&  this.longitude <= MAX_LONGITUDE)
                : "Violated Rep. Inv";
    }
  	// TODO: check if more methods needed.


}

