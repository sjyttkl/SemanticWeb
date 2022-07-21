package Chapter13.JenaSpatialIndex.spatial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.graph.Node;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.quadtree.Quadtree;

/**
 * This class is used to spatially index Jena nodes.  It uses a Java Topological 
 * Suite (JTS) spatial index to provide the indexing services.
 */
public class JenaSpatialIndex {

	/**
	 * A constant value describing the average meridonal radius 
	 * of Earth.  This value is needed to calculate the distance 
	 * in meters of a degree of arc at different points along 
	 * the Earth's meridians. 
	 */
	private static final double AVG_MERIDONAL_RADIUS = 6367449;
	/**
	 * A constant value describing the polar radius of the Earth.
	 * This value is needed to calculate the distance in meters 
	 * of a degree of arc at different points along the Earth's 
	 * meridians. 
	 */
	private static final double POLAR_RADIUS = 6356752.3142;
	
	/**
	 *  Simple class to represent a latitude-/longitude-based 
	 *  location.  This class also has accessors to help 
	 *  the program keep track of whether it has been fully 
	 *  populated i.e. hasX() and hasY().
	 */
	class Location {
		private Double _x = null;

		private Double _y = null;

		public double getX() {
			return _x.doubleValue();
		}

		public void setX(double d) {
			_x = new Double(d);
		}

		public boolean hasX() {
			return null != _x;
		}

		public double getY() {
			return _y.doubleValue();
		}

		public void setY(double d) {
			_y = new Double(d);
		}

		public boolean hasY() {
			return null != _y;
		}
	}

	/**
	 * A collection of URIs to locations. This is used a 
	 * temporary staging ground so that complete locations 
	 * can be built up statement by statement.  I.e. because 
	 * the order of the statements is not known, it could be 
	 * that the statements saying: 
	 * _:x a Location ; hasX "10" ; hasY "25" .
	 * 
	 * actually come in the opposite order. In that case, we 
	 * need to retain the information about the X value before 
	 * we can properly add it to the spatial index, which wants 
	 * only fully specified locations.
	 */
	private HashMap<Node, Location> _locations;

	/**
	 * A collection of the spatially indexed URIs.
	 * 
	 * The array index is the rectangle id used by the tree. 
	 * URIs are only added to the spatial index if they have a valid X 
	 * and Y value.
	 */
	private final Set<Node> _indexedNodes;

	/**
	 * The JTS spatial index that this class uses for spatial queries.
	 */
	private final SpatialIndex _index;

	public JenaSpatialIndex() {
		_index = new Quadtree();
		_locations = new HashMap<Node, Location>();
		_indexedNodes = new HashSet<Node>(50);
	}

	/**
	 * Tries to add a node to the spatial index.  If only one 
	 * value is given (i.e. not lat & lon), then the location
	 * described is not fully formed.  If that is the case, it is 
	 * stored in a buffer until enough information is available to 
	 * properly index the point.
	 *  
	 * @param node the node to be indexed
	 * @param lat the node's latitude
	 * @param lon the node's longitude
	 */
	public void add(Node node, Double lat, Double lon) {
		Location location;
		
		// Check to see if this is already spatially indexed. If 
		// it is, we can ignore the add call, since it's already 
		// been added.
		if (!_indexedNodes.contains(node)) {
			// if this is the first time we've seen this URI, 
			// create a new entry for it in the map.
			
			if (!_locations.containsKey(node)) {
				location = new Location();
				_locations.put(node, location);
			} else {
				location = _locations.get(node);
			}
		
			// if we now know an latitude value, update the location
			
			if (null != lat) {
				location.setY(lat.doubleValue());
			}
			
			// if we now know a longitude value, update the location
			
			if (null != lon) {
				location.setX(lon.doubleValue());
			}
			
			// if the location is now fully specified, add it to the spatial
			// index
			
			if (location.hasX() /* longitude */ && location.hasY() /* latitude */) {
				_indexedNodes.add(node);
				_index.insert(new Envelope(location.getX(), location.getX(),
						location.getY(), location.getY()), node);
			}
		}
	}

	/**
	 * This method builds a search envelope given a latitude, longitude, and search 
	 * distance in meters.  Because all of the points in the index are stored relative 
	 * to their latitude/longitude values, and because the search envelope is used by 
	 * the spatial index, this method must convert from meters to latitude/longitude 
	 * coordinates.  The search envelope is a square, centered on the specified point, 
	 * with the length of each side being 2 x distance in meters.
	 * 
	 * @param lat the latitude of the center of the square that defines the 
	 *            search envelope
	 * @param lon the longitude of the center of the square that defines the 
	 *            search envelope
	 * @param distanceInMeters the distance from the center of the square to t
	 * @return
	 */
	private Envelope getSearchEnvelope(double lat, double lon, double distanceInMeters)
	{
		Envelope toReturn = null;

		double metersPerDegreeLat, metersPerDegreeLon;
		double latOffsetInDegrees, lonOffsetInDegrees;
		
		metersPerDegreeLon = (Math.PI / 180) * Math.cos(Math.toRadians(lat)) * AVG_MERIDONAL_RADIUS;
		metersPerDegreeLat = (Math.PI * POLAR_RADIUS) / 180;

		lonOffsetInDegrees = distanceInMeters / (metersPerDegreeLon);
		latOffsetInDegrees = distanceInMeters / (metersPerDegreeLat);
		
		toReturn = new Envelope(lon - lonOffsetInDegrees, lon + lonOffsetInDegrees, lat + latOffsetInDegrees, lat - latOffsetInDegrees);

		return toReturn;
	}
	
	/**
	 * Convenience method for findWithin(double, double, double).
	 * 
	 * @see #findWithin(double, double, double)
	 * @param uri an indexed node
	 * @param maximumAllowedDistance the search distance
	 * @return a list of nodes within the search distance of the given node
	 */
	public List<Node> findWithin(Node uri, 
			double maximumAllowedDistance) {
		List<Node> toReturn = new ArrayList<Node>(0);
		Location temp;
		if (_indexedNodes.contains(uri)) {
			temp = _locations.get(uri);
			toReturn = findWithin(temp.getY(), temp.getX(),
					maximumAllowedDistance);
		}
		return toReturn;
	}

	/**
	 * Search method using the spatial index.  Given a latitude, longitude, and 
	 * appropriate search distance, this method returns a list of indexed nodes
	 * that are within the distance.
	 * 
	 * @param latitude the latitude of the search center point
	 * @param longitude the longitude of the search center 
	 * @param maximumAllowedDistance the search distance used to construct the
	 *        search envelope for this query
	 * @return a list of nodes within the search distance of the center point
	 */
	public List<Node> findWithin(double latitude, double longitude,
			double maximumAllowedDistance) {
		ArrayList<Node> toReturn = new ArrayList<Node>(0);
		Envelope searchEnvelope;
		List candidates = null;
		Location tempLocation;
		
		// build a search envelope given the center point and the search distance
		searchEnvelope = getSearchEnvelope(latitude, longitude, maximumAllowedDistance);
		
		// call the spatial index to get a candidate list of nodes
		candidates = _index.query(searchEnvelope);
		
		// loop over all of the candidates and confirm that they are within the 
		// search envelope (the spatial index can give false positives)
		for (Object o : candidates) {
			tempLocation = _locations.get(o);
			if (searchEnvelope.contains(tempLocation.getX(), tempLocation
					.getY())) {
				toReturn.add((Node) o);
			}
		}

		return toReturn;
	}

}
