package Chapter8.JenaExploration;

import com.hp.hpl.jena.graph.TransactionHandler;
import com.hp.hpl.jena.shared.Command;

public class CustomTransactionHandler implements TransactionHandler {

    @Override
    public void abort() {
        // TODO Auto-generated method stub

    }

    @Override
    public void begin() {
        // TODO Auto-generated method stub

    }

    @Override
    public void commit() {
        // TODO Auto-generated method stub

    }

    @Override
    public Object executeInTransaction(Command arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean transactionsSupported() {
        // TODO Auto-generated method stub
        return false;
    }

}
