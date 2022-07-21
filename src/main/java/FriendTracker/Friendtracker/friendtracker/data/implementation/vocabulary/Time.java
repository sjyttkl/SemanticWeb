package FriendTracker.Friendtracker.friendtracker.data.implementation.vocabulary;

/**
 * A utility enumeration to make it easy to access the vocabuluary 
 * of OWL-Time.
 */
public enum Time {

	Base("http://www.w3.org/2006/time#"),
	TemporalEntity(Base, "TemporalEntity"),
	Interval(Base, "Interval"),
	Instant(Base, "Instant"),
	inXSDDateTime(Base, "inXSDDateTime"),
	;
	
	private final String _str;
	
	private Time(Time base, String fragment)
	{
		this(base._str + fragment);
	}

	private Time(String str)
	{
		_str = str;
	}
	
	/**
	 *  Returns the full URI of the item. 
	 */
	public String getString()
	{
		return _str;
	}

}
	
