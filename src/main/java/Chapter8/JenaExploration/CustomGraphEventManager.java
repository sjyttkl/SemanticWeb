package Chapter8.JenaExploration;

import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.GraphEventManager;
import com.hp.hpl.jena.graph.GraphListener;
import com.hp.hpl.jena.graph.Triple;

public class CustomGraphEventManager implements GraphEventManager {

    @Override
    public boolean listening() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void notifyAddIterator(Graph arg0, List arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyDeleteIterator(Graph arg0, List arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public GraphEventManager register(GraphListener arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GraphEventManager unregister(GraphListener arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void notifyAddArray(Graph arg0, Triple[] arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyAddGraph(Graph arg0, Graph arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyAddIterator(Graph arg0, Iterator arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyAddList(Graph arg0, List arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyAddTriple(Graph arg0, Triple arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyDeleteArray(Graph arg0, Triple[] arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyDeleteGraph(Graph arg0, Graph arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyDeleteIterator(Graph arg0, Iterator arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyDeleteList(Graph arg0, List arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyDeleteTriple(Graph arg0, Triple arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyEvent(Graph arg0, Object arg1) {
        // TODO Auto-generated method stub

    }

}
