package FriendTracker.Friendtracker.friendtracker.model.implementation;

import java.text.DateFormat;
import java.util.Date;

import net.semwebprogramming.friendtracker.model.Event;
import net.semwebprogramming.friendtracker.model.Venue;

/**
 *  A concrete implementation of the Event interface. 
 */
public class EventImpl implements Event {

	private String _description;
	private String _id;
	private Date _startTime;
	private Venue _venue;
	private String _title;
	private DateFormat _dateFormat;
	
	public EventImpl()
	{
		this(null, null, null, null, null);
	}
	
	public EventImpl(String id, String title, Venue venue, Date startTime, String description)
	{
		_id = id;
		_title = title;
		_venue = venue;
		_startTime = startTime;
		_description = description;
		
		_dateFormat = DateFormat.getDateInstance();
	}
	
	public void setTitle(String title)
	{
		_title = title;
	}
	public String getTitle() {
		return _title;
	}
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	public String getID() {
		return _id;
	}
	public void setID(String id) {
		_id = id;
	}
	public Date getStartTime() {
		return _startTime;
	}
	public void setStartTime(Date startTime) {
		this._startTime = startTime;
	}
	public Venue getVenue() {
		return _venue;
	}
	public void setVenue(Venue venue) {
		this._venue = venue;
	}

	@Override
	public String toString() {
		String toReturn = _title + " at " + _venue.getName();
		if(null != _startTime)
		{
			toReturn += " - " + _dateFormat.format(_startTime);
		}
		
		return toReturn;
	}
	
}
