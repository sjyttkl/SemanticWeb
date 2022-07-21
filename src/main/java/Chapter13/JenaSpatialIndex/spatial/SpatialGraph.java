package Chapter13.JenaSpatialIndex.spatial;

import java.util.List;

import com.hp.hpl.jena.graph.BulkUpdateHandler;
import com.hp.hpl.jena.graph.Capabilities;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.GraphEventManager;
import com.hp.hpl.jena.graph.GraphStatisticsHandler;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Reifier;
import com.hp.hpl.jena.graph.TransactionHandler;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.TripleMatch;
import com.hp.hpl.jena.graph.query.QueryHandler;
import com.hp.hpl.jena.mem.GraphMem;
import com.hp.hpl.jena.shared.AddDeniedException;
import com.hp.hpl.jena.shared.DeleteDeniedException;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * This implementation of Jena's Graph interface provides a method 
 * called getSpatialSubgraph which uses a JenaSpatialIndex to retun 
 * a subgraph of the main graph containing only those points within a 
 * spatial region.
 * 
 * @see #getSpatialSubgraph(double, double, double)
 * @see JenaSpatialIndex
 */
public class SpatialGraph implements Graph {
	private final Graph _innerGraph;

	private final JenaSpatialIndex _index;

	/**
	 * Initializes a SpatialGraph.  This class will add a spatial 
	 * indexing capability to another graph. 
	 * @param graph a graph to which a spatial indexing capability 
	 * will be added 
	 */
	public SpatialGraph(Graph graph) {
		_innerGraph = graph;
		_index = new JenaSpatialIndex();
	}

	/**
	 * Adds a triple to the graph, and adding any relevant portion to
	 * to the spatial index. 
	 */
	public void add(Triple triple) throws AddDeniedException {
		_innerGraph.add(triple);
		String predicate = triple.getPredicate().getURI();

		if (WGS84.lat.equals(predicate) || WGS84.lon.equals(predicate)) {
			Node subjNode;
			Node objNode;
			Double value;

			subjNode = triple.getSubject();
			objNode = triple.getObject();

			if (WGS84.lat.equals(predicate)) {
				value = new Double(objNode.getLiteral().toString());
				_index.add(subjNode, value, null);
			} else if (WGS84.lon.equals(predicate)) {
				value = new Double(objNode.getLiteral().toString());
				_index.add(subjNode, null, value);
			}
		}
	}
	
	/**
	 * Given a latitude, longitude and distance (measured in meters),
	 * this method will return a Graph object containing only 
	 * statements where the subject is a point that falls within a 
	 * rectangular region centered on the latitude/longitude value.
	 * 
	 * @param lat the latitude of the center of the search area
	 * @param lon the longitude of the center of the search area
	 * @param distance the maximum distance from the center point 
	 * for which values should be returned 
	 * @return a graph including only statements about locations 
	 * within the search region
	 */
	public Graph getSpatialSubgraph(double lat, double lon, double distance) {
		Graph toReturn = new GraphMem();
		List<Node> validLocations;

		validLocations = _index.findWithin(lat, lon, distance);
		for (Node location : validLocations) {
			ExtendedIterator iterator = _innerGraph.find(location, null, null);
			try {
				while (iterator.hasNext()) {
					toReturn.add((Triple) iterator.next());
				}
			} finally {
				if (null != iterator) {
					iterator.close();
				}
			}
		}

		return toReturn;
	}

	/** 
	 * Returns the JenaSpatialIndex instance that this graph uses.
	 * 
	 * @see JenaSpatialIndex
	 * @return the JenaSpatialIndex used by this instance
	 */
	public JenaSpatialIndex getIndex() {
		return _index;
	}
	
	/*
	 *  The rest of this file contains methods that simply delegate the call
	 *  to the underlying Graph instance around which this graph is wrapped. 
	 */
	
	
	public void close() {
		_innerGraph.close();
	}

	public boolean contains(Node arg0, Node arg1, Node arg2) {
		return _innerGraph.contains(arg0, arg1, arg2);
	}

	public boolean contains(Triple arg0) {
		return _innerGraph.contains(arg0);
	}

	public void delete(Triple arg0) throws DeleteDeniedException {
		_innerGraph.delete(arg0);
	}

	public boolean dependsOn(Graph arg0) {
		return _innerGraph.dependsOn(arg0);
	}

	public ExtendedIterator find(Node subj, Node pred, Node obj) {
		return _innerGraph.find(subj, pred, obj);
	}

	public ExtendedIterator find(TripleMatch arg0) {
		return _innerGraph.find(arg0);
	}

	public BulkUpdateHandler getBulkUpdateHandler() {
		return _innerGraph.getBulkUpdateHandler();
	}

	public Capabilities getCapabilities() {
		return _innerGraph.getCapabilities();
	}

	public GraphEventManager getEventManager() {
		return _innerGraph.getEventManager();
	}

	public PrefixMapping getPrefixMapping() {
		return _innerGraph.getPrefixMapping();
	}

	public Reifier getReifier() {
		return _innerGraph.getReifier();
	}

	public GraphStatisticsHandler getStatisticsHandler() {
		return _innerGraph.getStatisticsHandler();
	}

	public TransactionHandler getTransactionHandler() {
		return _innerGraph.getTransactionHandler();
	}

	public boolean isClosed() {
		return _innerGraph.isClosed();
	}

	public boolean isEmpty() {
		return _innerGraph.isEmpty();
	}

	public boolean isIsomorphicWith(Graph arg0) {
		return _innerGraph.isIsomorphicWith(arg0);
	}

	public QueryHandler queryHandler() {
		return _innerGraph.queryHandler();
	}

	public int size() {
		return _innerGraph.size();
	}
}
