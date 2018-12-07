package HW1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A Route is a path that traverses arbitrary GeoSegments, regardless
 * of their names.
 * <p>
 * Routes are immutable. New Routes can be constructed by adding a segment 
 * to the end of a Route. An added segment must be properly oriented; that 
 * is, its p1 field must correspond to the end of the original Route, and
 * its p2 field corresponds to the end of the new Route.
 * <p>
 * Because a Route is not necessarily straight, its length - the distance
 * traveled by following the path from start to end - is not necessarily
 * the same as the distance along a straight line between its endpoints.
 * <p>
 * Lastly, a Route may be viewed as a sequence of geographical features,
 * using the <tt>getGeoFeatures()</tt> method which returns an Iterator of
 * GeoFeature objects.
 * <p>
 * <b>The following fields are used in the specification:</b>
 * <pre>
 *   start : GeoPoint            // location of the start of the route
 *   end : GeoPoint              // location of the end of the route
 *   startHeading : angle        // direction of travel at the start of the route, in degrees
 *   endHeading : angle          // direction of travel at the end of the route, in degrees
 *   geoFeatures : sequence      // a sequence of geographic features that make up this Route
 *   geoSegments : sequence      // a sequence of segments that make up this Route
 *   length : real               // total length of the route, in kilometers
 *   endingGeoSegment : GeoSegment  // last GeoSegment of the route
 * </pre>
 **/
public class Route {

	private final GeoPoint startPoint;
	private final GeoPoint endPoint;
	private final Double startHeading;
    private final Double endHeading;
    private final List<GeoSegment> geoSegList;
    private final List<GeoFeature> geoFeatureList;
    private final Double length;
    private final GeoSegment endingGeoSeg;

    // Representation invariant for each Route:
    // geoFeatures objects cannot consist of the same names
    //

    // Abstraction Function:
    // A Geographic point constructed by latitude, gp.latitude, and longitude coordinate, gp.longitude.
    // Both are in millionth of a degree.
    // A Route is a path that traverses arbitrary GeoSegments, regardless
    // of their names.
    // A Route constructed by the following fields:
    //start : The starting point of the route
    //end : The ending point of the route
    //startHeading : The direction the route starts with
    //endHeading : The direction the route ends with
    //geoFeatures : A list of geographic features that characterize a route
    //geoSegments : A list of geographic segments that characterize a route
    //length : The total legnth of the hole route
    //endingGeoSegment : The last segment of the route

  	/**
  	 * Constructs a new Route.
     * @requires gs != null
     * @effects Constructs a new Route, r, such that
     *	        r.startHeading = gs.heading &&
     *          r.endHeading = gs.heading &&
     *          r.start = gs.p1 &&
     *          r.end = gs.p2
     **/
  	public Route(GeoSegment gs)
    {
        assert gs != null: "Got a null GeoSegment";
        this.startPoint = gs.getP1();
        this.endPoint = gs.getP2();
        this.startHeading = gs.getHeading();
        this.endHeading = gs.getHeading();
        this.geoSegList = new ArrayList<GeoSegment>();
        this.geoSegList.add(gs);
        this.geoFeatureList = new ArrayList<GeoFeature>();
        this.geoFeatureList.add(new GeoFeature(gs));
        this.length = gs.getLength();
        this.endingGeoSeg = gs;
        this.checkRep();
    }

    /**
     * Constructs a new Route.
     * @requires gs != null
     * @effects Constructs a new Route, r, such that
     *	        r.startHeading = gs.heading &&
     *          r.endHeading = gs.heading &&
     *          r.start = gs.p1 &&
     *          r.end = gs.p2
     **/
    private Route(Route prevRoute, GeoSegment gs)
    {
        assert prevRoute != null: "Got a null Route";
        assert gs != null: "Got a null GeoSegment";
        assert gs.getP1() == this.getEnd(): "prev end != new start";
        this.startPoint = prevRoute.getStart();
        this.endPoint = gs.getP2();
        this.startHeading = prevRoute.getStartHeading();
        this.endHeading = gs.getHeading();
        this.geoSegList = new ArrayList<>(prevRoute.geoSegList);
        this.geoSegList.add(gs);
        this.geoFeatureList = new ArrayList<>(prevRoute.geoFeatureList);
        // Get the last feature name from the existing list
        int lastFeatureSize = prevRoute.geoFeatureList.size();
        GeoFeature lastFeature = prevRoute.geoFeatureList.get(lastFeatureSize - 1);
        String lastFeatureName = lastFeature.getName();
        if (lastFeatureName.equals(gs.getName()))
        {
            // The object gs belongs to the last feature. So we need to add it
            GeoFeature newLastFeature = lastFeature.addSegment(gs);
            // The class geoFeature is immutable. Remove the last object and add the new one
            this.geoFeatureList.remove(lastFeatureSize - 1);
            this.geoFeatureList.add(newLastFeature);
        }
        else
        {
            this.geoFeatureList.add(new GeoFeature(gs));
        }
        this.length = prevRoute.getLength() + gs.getLength();
        this.endingGeoSeg = gs;
        this.checkRep();
    }

    /**
     * Returns location of the start of the route.
     * @return location of the start of the route.
     **/
  	public GeoPoint getStart()
    {
  		this.checkRep();
        return this.startPoint;
    }


  	/**
  	 * Returns location of the end of the route.
     * @return location of the end of the route.
     **/
  	public GeoPoint getEnd()
    {
  		this.checkRep();
  		return new GeoPoint(startPoint.getLatitude(), endPoint.getLongitude());
  	}


  	/**
  	 * Returns direction of travel at the start of the route, in degrees.
   	 * @return direction (in compass heading) of travel at the start of the
   	 *         route, in degrees.
   	 **/
  	public Double getStartHeading()
    {
  	    this.checkRep();
        return new Double(this.startHeading);
  	}


  	/**
  	 * Returns direction of travel at the end of the route, in degrees.
     * @return direction (in compass heading) of travel at the end of the
     *         route, in degrees.
     **/
  	public Double getEndHeading()
    {
        this.checkRep();
        return new Double(this.endHeading);
  	}


  	/**
  	 * Returns total length of the route.
     * @return total length of the route, in kilometers.  NOTE: this is NOT
     *         as-the-crow-flies, but rather the total distance required to
     *         traverse the route. These values are not necessarily equal.
   	 **/
  	public Double getLength()
    {
  	    this.checkRep();
  	    return new Double(this.length);
  	}


  	/**
     * Creates a new route that is equal to this route with gs appended to
     * its end.
   	 * @requires gs != null && gs.p1 == this.end
     * @return a new Route r such that
     *         r.end = gs.p2 &&
     *         r.endHeading = gs.heading &&
     *         r.length = this.length + gs.length
     **/
  	public Route addSegment(GeoSegment gs)
    {
        assert gs != null: "Got a null GeoSegment";
        this.checkRep();
        return new Route(this, gs);
    }


    /**
     * Returns an Iterator of GeoFeature objects. The concatenation
     * of the GeoFeatures, in order, is equivalent to this route. No two
     * consecutive GeoFeature objects have the same name.
     * @return an Iterator of GeoFeatures such that
     * <pre>
     *      this.start        = a[0].start &&
     *      this.startHeading = a[0].startHeading &&
     *      this.end          = a[a.length - 1].end &&
     *      this.endHeading   = a[a.length - 1].endHeading &&
     *      this.length       = sum(0 <= i < a.length) . a[i].length &&
     *      for all integers i
     *          (0 <= i < a.length - 1 => (a[i].name != a[i+1].name &&
     *                                     a[i].end  == a[i+1].start))
     * </pre>
     * where <code>a[n]</code> denotes the nth element of the Iterator.
     * @see HW1.GeoFeature
     **/
  	public Iterator<GeoFeature> getGeoFeatures() {
  	    this.checkRep();
  	    return this.geoFeatureList.iterator();
  	}


  	/**
     * Returns an Iterator of GeoSegment objects. The concatenation of the
     * GeoSegments, in order, is equivalent to this route.
     * @return an Iterator of GeoSegments such that
     * <pre>
     *      this.start        = a[0].p1 &&
     *      this.startHeading = a[0].heading &&
     *      this.end          = a[a.length - 1].p2 &&
     *      this.endHeading   = a[a.length - 1].heading &&
     *      this.length       = sum (0 <= i < a.length) . a[i].length
     * </pre>
     * where <code>a[n]</code> denotes the nth element of the Iterator.
     * @see HW1.GeoSegment
     **/
  	public Iterator<GeoSegment> getGeoSegments() {
  	    this.checkRep();
  		return this.geoSegList.iterator();
  	}


  	/**
     * Compares the specified Object with this Route for equality.
     * @return true iff (o instanceof Route) &&
     *         (o.geoFeatures and this.geoFeatures contain
     *          the same elements in the same order).
     **/
  	public boolean equals(Object o)
    {
        assert o != null: "Got a null Object";
  		if (o instanceof Route)
        {
            Iterator<GeoFeature> it1 = this.getGeoFeatures();
            Iterator<GeoFeature> it2 = ((Route)o).getGeoFeatures();
            if (this.geoFeatureList.size() == ((Route)o).geoFeatureList.size())
            {   while (it1.hasNext())
                {
                    if (it1.equals(it2))
                    {
                        it1.next();
                        it2.next();
                    }
                }
                this.checkRep();
                return true;
            }
        }
        this.checkRep();
        return false;
  	}


    /**
     * Returns a hash code for this.
     * @return a hash code for this.
     **/
  	public int hashCode() {
    	// This implementation will work, but you may want to modify it
    	// for improved performance.
        this.checkRep();
    	return 1;
  	}


    /**
     * Returns a string representation of this.
     * @return a string representation of this.
     **/
  	public String toString() {
        String str1 = String.format("Route: begins at: %s, ends at: %s", this.startPoint, this.endPoint);
        String str2 = String.format("heading from: %s, heading to: %s", this.startHeading, this.endHeading);
        String str3 = String.format("Route length is: %s kilometers", this.length.toString());
        String appendStr = str1 + str2 + str3;
        Iterator<GeoFeature> it = this.getGeoFeatures();
        while (it.hasNext())
        {
            appendStr = appendStr + it.toString() + "\n";
            it.next();
        }
        return appendStr;
    }

    private void checkRep()
    {
    //    assert (startPoint != null) && (endPoint != null) && (0 <= startHeading) && (startHeading < 360)
    //                && (0 <= endHeading && endHeading < 360) && startHeading != null
    //                && endHeading) : "Violated Rep. Inv";
    }
}
