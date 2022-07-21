package FriendTracker.Upcoming.vocabulary;

public enum Time {

	Base("http://www.w3.org/2006/time#"),
	TemporalEntity("TemporalEntity"),
	Interval("Interval"),
	Instant("Instant"),
	inXSDDateTime("inXSDDateTime"),
	;
	
	private final String _str;
	
	private Time(String str)
	{
		_str = str;
	}
	
	public static String getString(Time time)
	{
		return time._str;
	}
	
	public static String getUriFor(Time time)
	{
		return Time.Base._str + time._str;
	}
}
	
