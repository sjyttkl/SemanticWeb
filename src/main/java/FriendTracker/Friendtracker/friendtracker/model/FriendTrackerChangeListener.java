package FriendTracker.Friendtracker.friendtracker.model;

/**
 * This interface defines the events that can take place 
 * on the FriendTracker model.
 *
 */
public interface FriendTrackerChangeListener {
	
	void friendAdded(Friend newFriend);
	void friendRemoved(Friend toRemoveFriend);
	
	void eventAdded(Event newEvent);
	void eventRemoved(Event toRemoveEvent);
	void eventsCleared();
	
	void postAdded(Post newPost);
	void postRemoved(Post toRemovePost);
	void postsCleared();
}
