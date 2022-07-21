package FriendTracker.Friendtracker.friendtracker.utilities;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 *  A class which extends RuntimeException (so that it isn't checked), 
 *  but which wraps other exceptions so that exceptions can be thrown 
 *  without imposing the burden of forced exception checking throughout 
 *  the application. 
 *
 */
public class ProperException extends RuntimeException
{
	/**
	 * The inner exception wrapped by this instance.
	 */
	private final Exception _inner;
	
	public ProperException(Exception inner) { 
		_inner = inner;
		inner.printStackTrace();
	}

	public boolean equals(Object arg0) {
		return _inner.equals(arg0);
	}

	public Throwable fillInStackTrace() {
		Throwable toReturn;
		if(null == _inner)
		{
			toReturn = super.fillInStackTrace();
		}
		else
		{
			toReturn = _inner.fillInStackTrace();
		}
		return toReturn;
	}

	public Throwable getCause() {
		return _inner.getCause();
	}

	public String getLocalizedMessage() {
		return _inner.getLocalizedMessage();
	}

	public String getMessage() {
		return _inner.getMessage();
	}

	public StackTraceElement[] getStackTrace() {
		return _inner.getStackTrace();
	}

	public int hashCode() {
		return _inner.hashCode();
	}

	public Throwable initCause(Throwable arg0) {
		return _inner.initCause(arg0);
	}

	public void printStackTrace() {
		_inner.printStackTrace();
	}

	public void printStackTrace(PrintStream arg0) {
		_inner.printStackTrace(arg0);
	}

	public void printStackTrace(PrintWriter arg0) {
		_inner.printStackTrace(arg0);
	}

	public void setStackTrace(StackTraceElement[] arg0) {
		_inner.setStackTrace(arg0);
	}

	public String toString() {
		return _inner.toString();
	}
	
	
}
