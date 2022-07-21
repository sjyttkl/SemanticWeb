package FriendTracker.Upcoming.vocabulary;

public enum Upcoming {
	
	Base("http://semwebprogramming.org/2009/upcoming#"),
	Event("Event"),
	Venue("Venue"),
	name("name"),
	description("description"),
	venue("venue"),
	startTime("startTime"),
	;
	
	private final String _str;
	
	private Upcoming(String str)
	{
		_str = str;
	}
	
	public static String getString(Upcoming upcoming)
	{
		return upcoming._str;
	}
	
	public static String getUriFor(Upcoming upcoming)
	{
		return Upcoming.Base._str + upcoming._str;
	}
}
