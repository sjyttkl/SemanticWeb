package FriendTracker.Friendtracker.friendtracker.data.implementation.vocabulary;

/**
 * A utility enumeration to make it easy to access the vocabuluary 
 * of FriendTracker.
 */
public enum FriendTracker {

	Base("http://semwebprogramming.org/2009/ftracker#"),
	Friend(Base, "Friend"),
	OnlineStatus(Base, "OnlineStatus"),
	isNamed(Base, "isNamed"),
	isFrom(Base, "isFrom"),
	hasStatus(Base, "hasStatus"),
	hasPic(Base, "hasPic"),
	hasEmailAddress(Base, "hasEmailAddress"),
	hasHandle(Base, "hasHandle"),
	hasPost(Base, "hasPost"),
	Event(Base, "Event"),
	Venue(Base, "Venue"),
	hasVenue(Base, "hasVenue"), 
	hasDescription(Base, "hasDescription"),
	occursAt(Base, "occursAt"),
	Post(Base, "Post"),
	hasTitle(Base, "hasTitle"),
	hasContent(Base, "hasContent"),
	
	UnknownStatus(Base, "Unknown"),
	AvailableStatus(Base, "Available"),
	BusyStatus(Base, "Busy"),
	AwayStatus(Base, "Away"),
	OfflineStatus(Base, "Offline"),
	;
	
	private final String _string;

	/**
	 *  Returns the full URI of the item. 
	 */
	public String getString()
	{
		return _string;
	}

	private FriendTracker(FriendTracker base, String fragment)
	{
		this(base._string + fragment);
	}
	
	private FriendTracker(String stringValue)
	{
		_string = stringValue;
	}
}
