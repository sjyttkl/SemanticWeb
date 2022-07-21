package FriendTracker.Friendtracker.friendtracker.model.implementation;

import net.semwebprogramming.friendtracker.model.Venue;


/**
 *  A concrete implementation of the Venue interface. 
 */
public class VenueImpl implements Venue {

	private double _latitude;
	private double _longitude;
	private String _name;
	private String _id;
	
	public VenueImpl()
	{
		this(null, null, 0d, 0d);
	}
	
	public VenueImpl(String id, String name, double latitude, double longitude) {
		_id = id;
		_name = name;
		_latitude = latitude;
		_longitude = longitude;
	}
	
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		_id = id;
	}
	public double getLatitude() {
		return _latitude;
	}
	public void setLatitude(double latitude) {
		this._latitude = latitude;
	}
	public double getLongitude() {
		return _longitude;
	}
	public void setLongitude(double longitude) {
		this._longitude = longitude;
	}
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}
}
