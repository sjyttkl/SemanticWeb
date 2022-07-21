package FriendTracker.Friendtracker.friendtracker.model;

import java.util.Set;

/**
 *  An interface that describes a Friend. 
 */
public interface Friend {

	/**
	 *  Names associated with the friend. 
	 */
	Set<String> getNames();
	/**
	 *  Online handles associated with the friend. 
	 */
	Set<String> getHandles();
	/**
	 *  Email addresses associated with the friend. 
	 */
	Set<String> getEmailAddresses();
	/**
	 *  A string giving the friend's hometown.
	 */
	String getOrigin();
	/**
	 *  A string giving the URL of a picture of the friend. 
	 */
	String getPictureURL();
	/**
	 *  Information about the friend's online status. 
	 */
	OnlineStatus getOnlineStatus();
	
	/**
	 *  Register an object to be notified should aspects of the 
	 *  friend change. 
	 */
	void addFriendChangeListener(FriendChangeListener listener);
	
	/**
	 *  Unregister an object to be notified should aspects of the 
	 *  friend change. 
	 */
	void removeFriendChangeListener(FriendChangeListener listener);
}
