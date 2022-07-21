package FriendTracker.Friendtracker.friendtracker.data.implementation.vocabulary;

/**
 * A utility enumeration to make it easy to access the vocabuluary 
 * of FriendTracker.
 */
public enum Foaf {

	Base("http://xmlns.com/foaf/0.1/"),
	Person(Base, "Person"),
	;

	private final String _string;

	/**
	 *  Returns the full URI of the item. 
	 */
	public String getString()
	{
		return _string;
	}

	private Foaf(Foaf base, String fragment)
	{
		this(base._string + fragment);
	}
	
	private Foaf(String stringValue)
	{
		_string = stringValue;
	}
}
