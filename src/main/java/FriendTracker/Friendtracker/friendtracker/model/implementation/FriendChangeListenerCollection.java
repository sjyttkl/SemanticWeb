package FriendTracker.Friendtracker.friendtracker.model.implementation;

import java.util.ArrayList;
import java.util.List;

import net.semwebprogramming.friendtracker.model.FriendChangeListener;
import net.semwebprogramming.friendtracker.model.OnlineStatus;

/**
 *  A collection of FriendChangeListeners which makes it 
 *  convenient to dispatch the same events to many listeners. 
  */
public class FriendChangeListenerCollection implements FriendChangeListener{
	
	private final List<FriendChangeListener> _listeners;
	private final Object _lock;
	
	public FriendChangeListenerCollection()
	{
		_lock = new Object();
		_listeners = new ArrayList<FriendChangeListener>();
	}
	
	public void add(FriendChangeListener fcl)
	{
		synchronized(_lock)
		{
			if(!_listeners.contains(fcl))
			{
				_listeners.add(fcl);
			}
		}
	}
	public void remove(FriendChangeListener fcl)
	{
		synchronized(_lock)
		{
			_listeners.remove(fcl);
		}
	}

	private List<FriendChangeListener> getListeners()
	{
		List<FriendChangeListener> toReturn;
		synchronized(_lock)
		{
			toReturn = new ArrayList<FriendChangeListener>(_listeners);
		}
		return toReturn;
	}
	
	public void handleAdded(String newHandle) {
		for(FriendChangeListener fcl : getListeners())
		{
			fcl.handleAdded(newHandle);
		}
	}
	
	public void handleRemoved(String toRemoveHandle) {
		for(FriendChangeListener fcl : getListeners())
		{
			fcl.handleRemoved(toRemoveHandle);
		}
	}

	public void nameAdded(String newName) {
		for(FriendChangeListener fcl : getListeners())
		{
			fcl.nameAdded(newName);
		}

	}

	public void nameRemoved(String toRemoveName) {
		for(FriendChangeListener fcl : getListeners())
		{
			fcl.handleRemoved(toRemoveName);
		}
	}

	public void emailAdded(String newEmail) {
		for(FriendChangeListener fcl : getListeners())
		{
			fcl.nameAdded(newEmail);
		}

	}

	public void emailRemoved(String toRemoveEmail) {
		for(FriendChangeListener fcl : getListeners())
		{
			fcl.handleRemoved(toRemoveEmail);
		}
	}	
	
	public void originChanged(String origin) {
		for(FriendChangeListener fcl : getListeners())
		{
			fcl.originChanged(origin);
		}
	}
	
	
	public void pictureURLChanged(String pictureURL) {
		for(FriendChangeListener fcl : getListeners())
		{
			fcl.pictureURLChanged(pictureURL);
		}
	}
	
	public void onlineStatusChanged(OnlineStatus newOnlineStatus) {
		for(FriendChangeListener fcl : getListeners())
		{
			fcl.onlineStatusChanged(newOnlineStatus);
		}
	}
}
