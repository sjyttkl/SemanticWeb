package FriendTracker.Friendtracker.friendtracker.model.implementation;

import java.util.HashSet;
import java.util.Set;

import net.semwebprogramming.friendtracker.model.Event;
import net.semwebprogramming.friendtracker.model.Friend;
import net.semwebprogramming.friendtracker.model.FriendTrackerChangeListener;
import net.semwebprogramming.friendtracker.model.FriendTrackerModel;
import net.semwebprogramming.friendtracker.model.Post;

/**
 *  Implementation of the FriendTrackerModel.
 *  
 *  This class performs very little work, and exists mostly to 
 *  host the various collections of Friends, Events, and Posts, and 
 *  to fire events when changes are made to any of those collections.
 *
 */
public class FriendTrackerModelImpl implements FriendTrackerModel {
	
	private final Object _lock;

	private final Set<Event> _events;
	private final Set<Friend> _friends;
	private final Set<Post> _posts;
	
	private final FriendTrackerChangeListenerCollection _changeListeners;
	
	public FriendTrackerModelImpl()
	{
		_lock = new Object();
		_events = new HashSet<Event>();
		_friends = new HashSet<Friend>();
		_posts = new HashSet<Post>();
		_changeListeners = new FriendTrackerChangeListenerCollection();
	}
	
	public void addFriendTrackerChangeListener(FriendTrackerChangeListener listener) {
		// no need to synchronize, that's done in the collection
		_changeListeners.add(listener);
	}
	public void removeFriendTrackerChangeListener(FriendTrackerChangeListener listener) {
		// no need to synchronize, that's done in the collection
		_changeListeners.remove(listener);
	}
	
	public Set<Event> getEvents() {
		Set<Event> toReturn;
		synchronized(_lock)
		{
			toReturn = new HashSet<Event>(_events);
		}
		return toReturn;
	}

	public Set<Friend> getFriends() {
		Set<Friend> toReturn;
		synchronized(_lock)
		{
			toReturn = new HashSet<Friend>(_friends);
		}
		return toReturn;
	}

	public Set<Post> getPosts() {
		Set<Post> toReturn;
		synchronized(_lock)
		{
			toReturn = new HashSet<Post>(_posts);
		}
		return toReturn;
	}
	
	public void addEvent(Event newEvent) {
		boolean toFire = false;
		synchronized(_lock)
		{
			toFire = _events.add(newEvent);
		}
		if(toFire)
		{
			_changeListeners.eventAdded(newEvent);
		}
	}

	public void addFriend(Friend newFriend) {
		boolean toFire = false;
		synchronized(_lock)
		{
			toFire = _friends.add(newFriend);
		}
		if(toFire)
		{
			_changeListeners.friendAdded(newFriend);
		}
	}
	public void addPost(Post newPost) {
		boolean toFire = false;
		synchronized(_lock)
		{
			toFire = _posts.add(newPost);
		}
		if(toFire)
		{
			_changeListeners.postAdded(newPost);
		}
	}

	public void removePost(Post toRemovePost) {
		boolean toFire = false;
		synchronized(_lock)
		{
			toFire = _posts.remove(toRemovePost);
		}
		if(toFire)
		{
			_changeListeners.postRemoved(toRemovePost);
		}		
	}
	public void removeEvent(Event toRemoveEvent) {
		boolean toFire = false;
		synchronized(_lock)
		{
			toFire = _events.remove(toRemoveEvent);
		}
		if(toFire)
		{
			_changeListeners.eventRemoved(toRemoveEvent);
		}		
	}
	public void clearEvents() {
		synchronized(_lock)
		{
			_events.clear();
		}
		_changeListeners.eventsCleared();
	}
	public void clearPosts() {
		synchronized(_lock)
		{
			_posts.clear();
		}
		_changeListeners.eventsCleared();
	}

	public void removeFriend(Friend toRemoveFriend) {
		boolean toFire = false;
		synchronized(_lock)
		{
			toFire = _friends.remove(toRemoveFriend);
		}
		if(toFire)
		{
			_changeListeners.friendRemoved(toRemoveFriend);
		}		
	}
}
