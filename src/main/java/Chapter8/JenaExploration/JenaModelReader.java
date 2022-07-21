package Chapter8.JenaExploration;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.shared.Lock;

public class JenaModelReader implements Runnable {
	private Model _model;
	public boolean waitingState = false;

	JenaModelReader(Model m){
		_model = m;
		
	}
	public void run() {
		
		// Start Timer
		JenaTimerInterrupt timerThread = new JenaTimerInterrupt(this, Thread.currentThread());
		Thread t = new Thread(timerThread);
		t.start();
		
		try {
		    waitingState = true;
		    System.out.println("Attempt Lock for Reading");
		    _model.enterCriticalSection(Lock.READ);
		    waitingState=false;
		    if(Thread.currentThread().isInterrupted() == false)
		        System.out.println("Performing Read");
		    else
		        System.out.println("Unable to Read");
		    }
		    catch (Exception e){
		        System.out.println("Unable to Read");
		        System.out.println(e.getMessage());
		    }
		    finally{
		        if(waitingState==true){
		           _model.leaveCriticalSection();
		           waitingState=false;
		        }
		    }
	}
}

class JenaTimerInterrupt implements Runnable{
    private Thread myThread;
    private JenaModelReader jenaWaiter;
    
    JenaTimerInterrupt(JenaModelReader waiter, Thread thread){
        myThread = thread;
        jenaWaiter = waiter;
    }
    
    public void run()
    {
        System.out.println("Read Timer Started");
        
        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Thread.sleep(50000);
  
        System.out.println("Read Timer Ending Sleep");

        // Cannot use method for the thread is not running
        
        if( jenaWaiter.waitingState==true){
            System.out.println("Interrupt Reader");
            myThread.interrupt();
        }
        
    }
    
}
