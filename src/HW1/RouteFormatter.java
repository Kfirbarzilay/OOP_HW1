package HW1;


import java.util.Iterator;

/**
 * A RouteFormatter class knows how to create a textual description of
 * directions from one location to another. The class is abstract to
 * support different textual descriptions.
 */
public abstract class RouteFormatter {

  	/**
     * Give directions for following this Route, starting at its start point
     * and facing in the specified heading.
     * @requires route != null && 
     * 			0 <= heading < 360
     * @param route the route for which to print directions.
   	 * @param heading the initial heading.
     * @return A newline-terminated directions <tt>String</tt> giving
     * 	       human-readable directions from start to end along this route.
     **/
  	public String computeDirections(Route route, double heading)
	{
  		// Implementation hint:
		// This method should call computeLine() for each geographic
		// feature in this route and concatenate the results into a single
		// String.
		assert route != null : "Can't compute directions since route is null";
		assert 0 <= heading && heading < 360 : "Heading is not valid";

		// Initialize the direction message and place an iterator over the route's geoFeatures list.
		String directions = "";
		Iterator<GeoFeature> geoFeatureIterator = route.getGeoFeatures();
		GeoFeature currGeoFeature = null;

		// Save the current heading for each iteration
		double currHeading = heading;

		// Iterate through all the geoFeatures list and compute directions by each GeoFeature line by line.
		while (geoFeatureIterator.hasNext())
		{
			currGeoFeature = geoFeatureIterator.next();

			// Using polymorphism to call the correct computeLine method.
			directions += computeLine(currGeoFeature, currHeading);

			// Getting the new heading to needed to compute next line.
			currHeading = currGeoFeature.getEndHeading();
		}

		return directions;
  	}


  	/**
     * Computes a single line of a multi-line directions String that
     * represents the instructions for traversing a single geographic
     * feature.
     * @requires geoFeature != null
     * @param geoFeature the geographical feature to traverse.
   	 * @param origHeading the initial heading.
     * @return A newline-terminated <tt>String</tt> that gives directions
     * 		   on how to traverse this geographic feature.
     */
  	public abstract String computeLine(GeoFeature geoFeature, double origHeading);


  	/**
     * Computes directions to turn based on the heading change.
     * @requires 0 <= oldHeading < 360 &&
     *           0 <= newHeading < 360
     * @param origHeading the start heading.
   	 * @param newHeading the desired new heading.
     * @return English directions to go from the old heading to the new
     * 		   one. Let the angle from the original heading to the new
     * 		   heading be a. The turn should be annotated as:
     * <p>
     * <pre>
     * Continue             if a < 10
     * Turn slight right    if 10 <= a < 60
     * Turn right           if 60 <= a < 120
     * Turn sharp right     if 120 <= a < 179
     * U-turn               if 179 <= a
     * </pre>
     * and likewise for left turns.
     */
  	protected String getTurnString(double origHeading, double newHeading)
	{
		assert 0 <= origHeading && origHeading < 360 : "originalHeading is not valid";
		assert 0 <= newHeading && newHeading < 360 : "newHeading is not valid";

		// calculating the relative direction the user needs to turn given the origHeading and newHeading

		double relativeAngle = newHeading - origHeading;
		//relativeAngle = (relativeAngle + 360) % 360;
		String turnDirection = "";
		// Right turns
		if (0 <= relativeAngle && relativeAngle < 180)
		{
			if (relativeAngle < 10)
			{
				turnDirection = "Continue";
			}
			else if (10 <= relativeAngle && relativeAngle < 60)
			{
				turnDirection = "Turn slight right";
			}
			else if (60 <= relativeAngle && relativeAngle < 120)
			{
				turnDirection = "Turn right";
			}
			else if (120 <= relativeAngle && relativeAngle < 179)
			{
				turnDirection = "Turn sharp right";
			}
			else if (179 <= relativeAngle)
			{
				turnDirection = "U-turn";

            }
		}
		else if ((-180 <= relativeAngle && relativeAngle < 0) || relativeAngle > 180) // Negative heading means left turn.
		{
            if(relativeAngle > 180)
            {
                relativeAngle -= 360;
            }

		    relativeAngle = -relativeAngle; // Working with absolute values for convenience.


			if (relativeAngle < 10)
			{
				turnDirection = "Continue";
			}
			else if (10 <= relativeAngle && relativeAngle < 60)
			{
				turnDirection = "Turn slight left";
			}
			else if (60 <= relativeAngle && relativeAngle < 120)
			{
				turnDirection = "Turn left";
			}
			else if (120 <= relativeAngle && relativeAngle < 179)
			{
				turnDirection = "Turn sharp left";
			}
			else if (179 <= relativeAngle)
			{
				turnDirection = "U-turn";
			}

		}

		return turnDirection;
	}


}
