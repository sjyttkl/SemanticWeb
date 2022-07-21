package FriendTracker.Friendtracker.friendtracker.model;

import java.util.Set;

/**
 *  The main model of the FriendTracker UI.
 *  
 *  This class maintains a list of Friends, Events, and blog Posts.
 */
public interface FriendTrackerModel {
	
	/**
	 * @return A set of unique Friend objects.
	 */
	Set<Friend> getFriends();
	/**
	 * @return A set of unique Event objects.
	 */
	Set<Event> getEvents();
	/**
	 * @return A set of unique Post objects.
	 */
	Set<Post> getPosts();
	
	/**
	 * Make the FriendTrackerModel aware of a new Friend.
	 * @param newFriend the new Friend
	 */
	void addFriend(Friend newFriend);
	void removeFriend(Friend toRemoveFriend);

	/**
	 * Make the FriendTrackerModel aware of a new Event.
	 * @param newEvent the new Event
	 */
	void addEvent(Event newEvent);
	void removeEvent(Event toRemoveEvent);
	
	/**
	 * Remove all of the events presently in the FriendTrackerModel.
	 *
	 */
	void clearEvents();
	/**
	 * Remove all of the blog posts presently in the FriendTrackerModel.
	 *
	 */
	void clearPosts();
	
	/**
	 * Make the FriendTrackerModel aware of a new Post.
	 * @param newPost the new Post
	 */
	void addPost(Post newPost);
	void removePost(Post toRemovePost);
	
	/**
	 * Registers a new FriendTrackerChangeListener.
	 */
	void addFriendTrackerChangeListener(FriendTrackerChangeListener listener);
	/**
	 * Unregisters a FriendTrackerChangeListener.
	 */
	void removeFriendTrackerChangeListener(FriendTrackerChangeListener listener);
}
