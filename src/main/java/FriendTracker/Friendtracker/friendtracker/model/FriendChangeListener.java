package FriendTracker.Friendtracker.friendtracker.model;

/**
 *  An interface describing the notifications that should be 
 *  made should some aspect of a friend change. 
 *
 */
public interface FriendChangeListener {

	void nameAdded(String newName);
	void nameRemoved(String toRemoveName);
	
	void handleAdded(String newHandle);
	void handleRemoved(String toRemoveHandle);
	
	void emailAdded(String newEmail);
	void emailRemoved(String toRemoveEmail);
	
	void originChanged(String origin);
	
	void pictureURLChanged(String pictureURL);
	
	void onlineStatusChanged(OnlineStatus newOnlineStatus);
}
