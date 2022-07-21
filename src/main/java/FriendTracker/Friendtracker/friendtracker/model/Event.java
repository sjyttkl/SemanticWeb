package FriendTracker.Friendtracker.friendtracker.model;

import java.util.Date;

/**
 *  Interface describing an Event. 
 *
 */
public interface Event {

	/**
	 * Event title.
	 */
	String getTitle();
	/**
	 * A description of the event.
	 */
	String getDescription();
	/**
	 *  An object describing the event's Venue. 
	 */
	Venue getVenue();
	/**
	 *  The time at which the event starts. 
	 */
	Date getStartTime();
	/**
	 *  A unique ID of the event. 
	 */
	String getID();
}
