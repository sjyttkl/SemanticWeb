package Chapter13.JenaSpatialIndex.temporal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.hp.hpl.jena.graph.Triple;

/**
 * This class provides temporal a indexing service for statements.  
 * Each statement is given a timestamp, which can then be used for 
 * temporal queries. 
 *
 */
public class TemporalTripleIndex {

	/**
	 * This class wraps a Jena Triple and serves to associate a 
	 * time stamp with it.
	 */
	private class TemporalTriple implements Comparable<TemporalTriple> {
		/**
		 * The triple this TemporalTriple wraps 
		 */
		private final Triple _trip;

		/**
		 * The time stamp associated with this TemporalTriple.
		 */
		private final Calendar _time;

		public TemporalTriple(Triple trip, Calendar time) {
			_trip = trip;
			_time = time;
		}

		public Calendar getTime() {
			return _time;
		}

		public Triple getTriple() {
			return _trip;
		}

		/**
		 * Allows the TemporalTriples to be ordered according to their 
		 * timestamps.
		 */
		public int compareTo(TemporalTriple triple) {
			int toReturn = _time.compareTo(triple._time);
			return toReturn;
		}
	}

	/**
	 * A time-ordered collection of triples.
	 */
	private final List<TemporalTriple> _triples;

	/**
	 * Statements are added with a timestamp, and may be added out of order.
	 * This flag is true if the list of TemporalTriples is presently in 
	 * chronological order, and false otherwise.
	 */
	private boolean _sorted = true;

	/**
	 * Creates a new TemporalTripleIndex with an initial capacity of 500 
	 * statements.
	 */
	public TemporalTripleIndex() {
		this(500);
	}

	/**
	 * Creates a new TemporalTripleIndex with the given initial capacity.
	 * 
	 * @param capacity initial capacity of the index
	 */
	public TemporalTripleIndex(int capacity) {
		_triples = new ArrayList<TemporalTriple>(capacity);
	}

	/**
	 * Adds a new triple to the index using the current system time as the 
	 * time stamp.
	 * 
	 * @param triple the triple to add to the index
	 */
	public void add(Triple triple) {
		_triples.add(new TemporalTriple(triple, Calendar.getInstance()));
	}

	/**
	 * Adds a new triple to the index with the given time as the 
	 * timestamp.
	 * 
	 * @param triple the triple 
	 * @param dateTime the timestamp
	 */
	public void add(Triple triple, Calendar dateTime) {
		TemporalTriple toAdd = new TemporalTriple(triple, dateTime);

		if (_triples.size() > 0) {
			// All new triples are added at the end of the collection
			// Therefore if the item at the end of the collection is 
			// already *after* the new triple, then when the new one's 
			// added, the collection will be out of order, and 
			// will no longer be sorted.
			if (_triples.get(_triples.size() - 1).getTime().after(
					toAdd.getTime())) {
				_sorted = false;
			}
		}
		_triples.add(toAdd);
	}

	/**
	 * This method removes a triple from the index.
	 * 
	 * @param triple the triple to remove from the index
	 */
	public void delete(Triple triple) {
		for (int i = _triples.size() - 1; i <= 0; i--) {
			if (_triples.get(i)._trip.equals(triple)) {
				_triples.remove(i);
			}
		}
	}

	/**
	 * This method returns all of those statements in the index that are 
	 * between the lower bound and upper bound, inclusive.  If the lower 
	 * bound is null, then everything before the upper bound is returned.
	 * If the upper bound is null, then everything after the lower bound 
	 * is returned.
	 * 
	 * @param lowerBound the lower temporal bound, or null
	 * @param upperBound the temporal upper bound, or null
	 * @return the statements within the definied temporal bounds
	 */
	public List<Triple> getTriples(Calendar lowerBound, Calendar upperBound) {
		int lower = 0;
		int upper = _triples.size() - 1;
		int returnSize = 0;
		boolean valid = true;
		List<Triple> toReturn;

		if (!_sorted) {
			Collections.sort(_triples);
		}

		if (null != lowerBound) {
			if (!_triples.get(_triples.size() - 1)._time.before(lowerBound)) {
				lower = findLower(search(0, upper, lowerBound));
			} else {
				valid = false;
			}
		}
		if (null != upperBound) {
			if (!_triples.get(0)._time.after(upperBound)) {
				upper = findUpper(search(0, upper, upperBound));
			} else {
				valid = false;
			}
		}

		if (valid) {
			if (upper > lower) {
				returnSize = upper - lower;
			}

			toReturn = new ArrayList<Triple>(returnSize);
			for (int i = lower; i < upper; i++) {
				toReturn.add(_triples.get(i)._trip);
			}
		} else {
			toReturn = new ArrayList<Triple>();
		}
		return toReturn;
	}

	/**
	 * This method returns the lowest index which has the same timestamp 
	 * as the item at the given index.
	 * 
	 * I.e. if items 3, 4, and 5 all have the same timestamp, then calling
	 * findLower(3), findLower(4), or findLower(5), will all return 3.
	 * 
	 * @param item the index of the given item
	 * @return the lowest index of an item with the same timestamp
	 */
	private int findLower(int item) {
		Calendar comp = _triples.get(item)._time;
		int bottom = item;

		for (int i = item - 1; i >= 0; i--) {
			if (_triples.get(i)._time.equals(comp)) {
				bottom = i;
			} else {
				break;
			}
		}

		return bottom;
	}

	/**
	 * This method returns the highest index which has the same timestamp 
	 * as the item at the given index.
	 * 
	 * I.e. if items 3, 4, and 5 all have the same timestamp, then calling
	 * findLower(3), findLower(4), or findLower(5), will all return 5.
	 * 
	 * @param item the index of the given item
	 * @return the highest index of an item with the same timestamp
	 */
	private int findUpper(int item) {
		Calendar comp = _triples.get(item)._time;
		int top = item;

		for (int i = item + 1; i < _triples.size(); i++) {
			if (_triples.get(i)._time.equals(comp)) {
				top = i;
			} else {
				break;
			}
		}

		return top;
	}

	/**
	 * A helper method to perform linear search to find a statement 
	 * with the closest timestamp to the given one.  The calling code should 
	 * ensure that the value requested is within the bounds of the collection 
	 * before calling.
	 * 
	 * @param lower the index of the lower search bound 
	 * @param upper the index of the upper search bound
	 * @param toFind the timestamp to match against
	 * 
	 * @return the index of a statement with the given timestamp 
	 */
	private int search(int lower, int upper, Calendar toFind)
	{
		int toReturn = 0;
		for(int i = lower; i <= upper; i++)
		{
			if(_triples.get(i)._time.before(toFind))
			{
				continue;
			}
			else
			{
				toReturn = i;
				break;
			}
		}
		return toReturn;
	}
	
}
