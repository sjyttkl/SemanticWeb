package FriendTracker.Upcoming.parser;

public enum Elements {

	Event("event"),
	Response("rsp");
	
	private final String _string;
	
	public static String getString(Elements clsName)
	{
		return clsName._string;
	}
	
	private Elements(String str)
	{
		_string = str;
	}
}
