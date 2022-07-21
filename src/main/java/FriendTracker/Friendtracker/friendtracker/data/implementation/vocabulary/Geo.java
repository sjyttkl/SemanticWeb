package FriendTracker.Friendtracker.friendtracker.data.implementation.vocabulary;

/**
 * A utility enumeration to make it easy to access the vocabuluary 
 * of WGS84 Geo Positioning.
 */
public enum Geo {
	Base("http://www.w3.org/2003/01/geo/wgs84_pos#"),
	SpatialThing(Base, "SpatialThing"),
	Point(Base, "Point"),
	longitude(Base, "long"),
	latitude(Base, "lat"),
	altitude(Base, "alt"),
	;
	
	private final String _str;
	
	private Geo(Geo base, String fragment)
	{
		this(base._str + fragment);
	}
	
	private Geo(String str)
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
