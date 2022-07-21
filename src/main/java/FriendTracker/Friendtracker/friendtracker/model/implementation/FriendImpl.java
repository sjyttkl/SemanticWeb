package FriendTracker.Friendtracker.friendtracker.model.implementation;

import java.util.HashSet;
import java.util.Set;

import net.semwebprogramming.friendtracker.model.Friend;
import net.semwebprogramming.friendtracker.model.FriendChangeListener;
import net.semwebprogramming.friendtracker.model.OnlineStatus;
import net.semwebprogramming.friendtracker.utilities.Utilities;

/**
 *  A concrete implementation of the Friend interface. 
 */
public class FriendImpl implements Friend {

	private final Set<String> _names = new HashSet<String>();
	private final Set<String> _handles = new HashSet<String>();
	private final Set<String> _emails = new HashSet<String>();
	private String _origin;
	private OnlineStatus _status;
	private String _pictureURL;
	
	private final FriendChangeListenerCollection _listeners;

	public FriendImpl()
	{
		this(null, null, null, null, null, OnlineStatus.Unknown);
	}
	
	public FriendImpl(String name, String handle, String email, String pictureURL, String origin, OnlineStatus status)
	{
		if(null != name)
		{
			_names.add(name);
		}
		if(null != handle)
		{
			_handles.add(handle);
		}
		if(null != email)
		{
			_emails.add(email);
		}
		if(null != origin)
		{
			_origin = origin;
		}
		_pictureURL = pictureURL;
		_status = status;

		_listeners = new FriendChangeListenerCollection();
	}
	
	public void addHandle(String handle) {
		_handles.add(handle);
		_listeners.handleAdded(handle);
	}

	public void removeHandle(String handle)
	{
		if(_handles.remove(handle))
		{
			_listeners.handleRemoved(handle);
		}
	}
	
	public String getPictureURL()
	{
		return _pictureURL;
	}
	
	public void setPictureURL(String pictureURL)
	{
		_pictureURL = pictureURL;
		_listeners.pictureURLChanged(pictureURL);
	}

	public String getOrigin()
	{
		return _origin;
	}
	
	public void setOrigin(String origin)
	{
		_origin = origin;
		_listeners.originChanged(origin);
	}
	
	
	public void addName(String name) {
		_names.add(name);
		_listeners.nameAdded(name);
	}

	public void removeName(String name)
	{
		if(_names.remove(name))
		{
			_listeners.nameRemoved(name);
		}
	}

	public void addEmail(String email) {
		_emails.add(email);
		_listeners.emailAdded(email);
	}

	public void removeEmail(String email)
	{
		if(_emails.remove(email))
		{
			_listeners.emailRemoved(email);
		}
	}
	
	public void setStatus(OnlineStatus status) {
		this._status = status;
		_listeners.onlineStatusChanged(status);
	}

	public Set<String> getHandles() {
		return new HashSet<String>(_handles);
	}

	public Set<String> getNames() {
		return new HashSet<String>(_names);
	}

	public Set<String> getEmailAddresses() {
		return new HashSet<String>(_emails);
	}

	public OnlineStatus getOnlineStatus() {
		return _status;
	}

	@Override 
	public String toString() {
		String toReturn = "Unknown person";
		
		if(!_names.isEmpty())
		{
			toReturn = Utilities.concatenateStringSet(_names);
		}
		else if(!_handles.isEmpty())
		{
			toReturn = Utilities.concatenateStringSet(_handles);
		}
		else if(!_emails.isEmpty())
		{
			toReturn = Utilities.concatenateStringSet(_emails);
		}
		
		return toReturn + " - " + _status.name(); 
	};

	public void removeFriendChangeListener(FriendChangeListener listener) {
		_listeners.remove(listener);
	}

	public void addFriendChangeListener(FriendChangeListener listener) {
		_listeners.add(listener);
	}

}
