package Chapter8.JenaExploration;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Reifier;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.TripleMatch;
import com.hp.hpl.jena.shared.ReificationStyle;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class CustomReifier implements Reifier {

    @Override
    public ExtendedIterator allNodes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExtendedIterator allNodes(Triple arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public ExtendedIterator find(TripleMatch arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExtendedIterator findEither(TripleMatch arg0, boolean arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExtendedIterator findExposed(TripleMatch arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Graph getParentGraph() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ReificationStyle getStyle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean handledAdd(Triple arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean handledRemove(Triple arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasTriple(Node arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasTriple(Triple arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Node reifyAs(Node arg0, Triple arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remove(Triple arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(Node arg0, Triple arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Triple getTriple(Node arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
