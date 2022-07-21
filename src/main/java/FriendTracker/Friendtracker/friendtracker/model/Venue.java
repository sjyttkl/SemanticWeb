package FriendTracker.Friendtracker.friendtracker.model;


/**
 *  Interface describing an event's venue. 
 */
public interface Venue {

	/**
	 *  An ID that denotes this venue. 
	 */
	String getId();
	/**
	 *  The name of the venue. 
	 */
	String getName();
	/**
	 *  The latitude of the venue. 
	 */
	double getLatitude();
	/**
	 *  The longitude of the venue. 
	 */
	double getLongitude();
}
