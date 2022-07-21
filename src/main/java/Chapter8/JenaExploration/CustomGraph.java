package Chapter8.JenaExploration;
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
import com.hp.hpl.jena.shared.AddDeniedException;
import com.hp.hpl.jena.shared.DeleteDeniedException;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class CustomGraph implements Graph {

	public void close() {	
	}

	public boolean contains(Triple arg0) {
		return false;
	}

	public boolean contains(Node arg0, Node arg1, Node arg2) {
		return false;
	}

	public void delete(Triple arg0) throws DeleteDeniedException {
	}

	public boolean dependsOn(Graph arg0) {
		return false;
	}

	public ExtendedIterator find(TripleMatch arg0) {
		return null;
	}

	public ExtendedIterator find(Node arg0, Node arg1, Node arg2) {
		return null;
	}

	public BulkUpdateHandler getBulkUpdateHandler() {
	    return new CustomBulkUpdateHandler();
	}

	public Capabilities getCapabilities() {
		return null;
	}

	public GraphEventManager getEventManager() {
	    return new CustomGraphEventManager();
	}

	public PrefixMapping getPrefixMapping() {
	    return new CustomPrefixMapping();
	}

	public Reifier getReifier() {
	    return new CustomReifier();
	}

	public GraphStatisticsHandler getStatisticsHandler() {
	    return new CustomGraphStatisticsHandler();
	}

	public TransactionHandler getTransactionHandler() {
	    return new CustomTransactionHandler();
	}

	public boolean isClosed() {
		return false;
	}

	public boolean isEmpty() {
		return false;
	}

	public boolean isIsomorphicWith(Graph arg0) {
		return false;
	}

	public QueryHandler queryHandler() {
		return null;
	}

	public int size() {
		return 0;
	}

	public void add(Triple arg0) throws AddDeniedException {
	}

}
