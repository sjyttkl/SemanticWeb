package Chapter13.JenaSpatialIndex.temporal;

import java.util.Calendar;
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
 * This class implements Jena's Graph interface and uses a 
 * TemporalTripleIndex to timestamp all of the statements 
 * added to or removed from the TemporalGraph.
 *
 */
public class TemporalGraph implements Graph {
	/**
	 * The graph that this TemporalGraph wraps. 
	 */
	private final Graph _innerGraph;
	/**
	 * The TemporalTripleIndex that allows this graph to 
	 * create temporal subgraphs.
	 */
	private final TemporalTripleIndex _index;
	
	/**
	 * Creates a new TemporalGraph.
	 * @return a new TemporalGraph
	 */
	public static TemporalGraph newInstance()
	{
		return new TemporalGraph(new GraphMem());
	}
	
	/**
	 * Wraps an existing graph and adds temporal information 
	 * about each subsequent statement added.  Note that this 
	 * call should be made before the inner graph has any 
	 * statements.
	 * 
	 * @param inner graph to be wrapped
	 */
	public TemporalGraph(Graph inner)
	{
		_innerGraph = inner;
		_index = new TemporalTripleIndex();
	}

	/**
	 * Returns a graph containing only those statements that were added 
	 * after the start date and before the end date.
	 * 
	 * @param start the lower temporal bound for the statements in the 
	 * resulting graph 
	 * graph
	 * @param end the upper temporal bound for the statements in the 
	 * resulting graph
	 * @return a graph containing only those statements of the main graph  
	 * added after the start date and before the end date
	 */
	public Graph getTemporalSubgraph(Calendar start, Calendar end)
	{
		Graph toReturn = new GraphMem();
		
		List<Triple> triples = _index.getTriples(start, end);
		
		for(Triple t : triples)
		{
			toReturn.add(t);
		}
		
		return toReturn;
	}
	
	/*
	 * This implementation adds the statement to the underlying graph but also
	 * indexes it temporally.
	 * 
	 * (non-Javadoc)
	 * @see com.hp.hpl.jena.graph.GraphAdd#add(com.hp.hpl.jena.graph.Triple)
	 */
	public void add(Triple t) throws AddDeniedException {
		_innerGraph.add(t);
		_index.add(t);
	}

	/*
	 * This implementation removes the statement from the underlying graph 
	 * and also from the temporal index.
	 * 
	 * (non-Javadoc)
	 * @see com.hp.hpl.jena.graph.Graph#delete(com.hp.hpl.jena.graph.Triple)
	 */
	public void delete(Triple t) throws DeleteDeniedException {
		_innerGraph.delete(t);
		_index.delete(t);
	}
	
	/*
	 *  The rest of this file contains the an implementation of the Graph 
	 *  interface that simply delegates the calls to the inner graph. 
	 */
	
	
	public void close() {
		_innerGraph.close();
	}

	public boolean contains(Node s, Node p, Node o) {
		return _innerGraph.contains(s, p, o);
	}

	public boolean contains(Triple t) {
		return _innerGraph.contains(t);
	}

	public boolean dependsOn(Graph other) {
		return _innerGraph.dependsOn(other);
	}

	public ExtendedIterator find(Node s, Node p, Node o) {
		return _innerGraph.find(s, p, o);
	}

	public ExtendedIterator find(TripleMatch m) {
		return _innerGraph.find(m);
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

	public boolean isIsomorphicWith(Graph g) {
		return _innerGraph.isIsomorphicWith(g);
	}

	public QueryHandler queryHandler() {
		return _innerGraph.queryHandler();
	}

	public int size() {
		return _innerGraph.size();
	}
}
