package FriendTracker.Upcoming.vocabulary;

public enum Geo {
	Base("http://www.w3.org/2003/01/geo/wgs84_pos#"),
	SpatialThing("SpatialThing"),
	Point("Point"),
	longitude("long"),
	latitude("lat"),
	altitude("alt"),
	;
	
	private final String _str;
	
	private Geo(String str)
	{
		_str = str;
	}
	
	public static String getString(Geo wgs)
	{
		return wgs._str;
	}
	
	public static String getUriFor(Geo geo)
	{
		return Geo.Base._str + geo._str;
	}
	
}
