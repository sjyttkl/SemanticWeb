package FriendTracker.Friendtracker.friendtracker.model.implementation;

import java.util.ArrayList;
import java.util.List;

import net.semwebprogramming.friendtracker.model.Event;
import net.semwebprogramming.friendtracker.model.Friend;
import net.semwebprogramming.friendtracker.model.FriendTrackerChangeListener;
import net.semwebprogramming.friendtracker.model.Post;

/**
 *  A collection of FriendTrackerChangeListeners which makes it 
 *  convenient to dispatch the same events to many listeners. 
  */
public class FriendTrackerChangeListenerCollection implements FriendTrackerChangeListener 
{
	final List<FriendTrackerChangeListener> _listeners;
	final Object _lock;
	
	public FriendTrackerChangeListenerCollection()
	{
		_lock = new Object();
		_listeners = new ArrayList<FriendTrackerChangeListener>();
	}
	
	void add(FriendTrackerChangeListener toAdd)
	{
		synchronized(_lock)
		{
			if(!_listeners.contains(toAdd))
			{
				_listeners.add(toAdd);
			}
		}
	}
	
	void remove(FriendTrackerChangeListener toRemove)
	{
		synchronized(_lock)
		{
			_listeners.remove(toRemove);
		}
	}

	List<FriendTrackerChangeListener> getListeners()
	{
		List<FriendTrackerChangeListener> toReturn;
		synchronized(_lock)
		{
			toReturn = new ArrayList<FriendTrackerChangeListener>(_listeners);
		}
		return toReturn;
	}
	
	public void eventAdded(Event newEvent) {
		List<FriendTrackerChangeListener> listeners;
		
		listeners = getListeners();
		
		for(FriendTrackerChangeListener ftcl : listeners)
		{
			ftcl.eventAdded(newEvent);
		}
	}
	public void eventsCleared() {
		List<FriendTrackerChangeListener> listeners;
		
		listeners = getListeners();
		
		for(FriendTrackerChangeListener ftcl : listeners)
		{
			ftcl.eventsCleared();
		}
	}
	public void postsCleared() {
		List<FriendTrackerChangeListener> listeners;
		
		listeners = getListeners();
		
		for(FriendTrackerChangeListener ftcl : listeners)
		{
			ftcl.postsCleared();
		}
	}
	public void eventRemoved(Event toRemoveEvent) {
		List<FriendTrackerChangeListener> listeners;
		
		listeners = getListeners();
		
		for(FriendTrackerChangeListener ftcl : listeners)
		{
			ftcl.eventRemoved(toRemoveEvent);
		}
	}

	public void friendAdded(Friend newFriend) {
		List<FriendTrackerChangeListener> listeners;
		
		listeners = getListeners();
		
		for(FriendTrackerChangeListener ftcl : listeners)
		{
			ftcl.friendAdded(newFriend);
		}
	}

	public void friendRemoved(Friend toRemoveFriend) {
		List<FriendTrackerChangeListener> listeners;
		
		listeners = getListeners();
		
		for(FriendTrackerChangeListener ftcl : listeners)
		{
			ftcl.friendRemoved(toRemoveFriend);
		}
	}
	
	public void postAdded(Post newPost) {
		List<FriendTrackerChangeListener> listeners;
		
		listeners = getListeners();
		
		for(FriendTrackerChangeListener ftcl : listeners)
		{
			ftcl.postAdded(newPost);
		}
	}

	public void postRemoved(Post toRemovePost) {
		List<FriendTrackerChangeListener> listeners;
		
		listeners = getListeners();
		
		for(FriendTrackerChangeListener ftcl : listeners)
		{
			ftcl.postRemoved(toRemovePost);
		}
	}
}
