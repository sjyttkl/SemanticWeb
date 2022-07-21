package FriendTracker.Friendtracker.friendtracker.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.semwebprogramming.friendtracker.model.Event;
import net.semwebprogramming.friendtracker.model.Friend;
import net.semwebprogramming.friendtracker.model.Post;

/**
 *  The SourceCollection class implements the FriendSource, PostSource, 
 *  and EventSource interfaces, and acts as a pluggable abstraction 
 *  for all of the sources.  With the SourceCollection, all method calls 
 *  are routed to corresponding collections of -Source objects, and the 
 *  results are aggregated into a single response.  The SourceCollection 
 *  provides an interface that hides the number and nature of concrete 
 *  sources. 
 *
 */
public class SourceCollection implements FriendSource, PostSource, EventSource {

	/**
	 * A collection of PostSource objects
	 */
	private final Set<PostSource> _postSources = new HashSet<PostSource>();
	/**
	 * A collection of EventSource objects
	 */
	private final Set<EventSource> _eventSources = new HashSet<EventSource>();
	/**
	 * A collection of FriendSource objects
	 */
	private final Set<FriendSource> _friendSources = new HashSet<FriendSource>();
	
	
	public void addSource(FriendSource friendSource)
	{
		_friendSources.add(friendSource);
	}
	public void addSource(PostSource postSource)
	{
		_postSources.add(postSource);
	}
	public void addSource(EventSource eventSource)
	{
		_eventSources.add(eventSource);
	}
	public void removeSource(FriendSource friendSource)
	{
		_friendSources.remove(friendSource);
	}
	public void removeSource(PostSource postSource)
	{
		_postSources.remove(postSource);
	}
	public void removeSource(EventSource eventSource)
	{
		_eventSources.remove(eventSource);
	}

	/**
	 * Delegates the call to each PostSource and returns the union 
	 * of the results.
	 */
	public List<Post> getPosts(Friend author) {
		ArrayList<Post> toReturn = new ArrayList<Post>();
		
		for(PostSource s : _postSources)
		{
			toReturn.addAll(s.getPosts(author));
		}
		
		return toReturn;
	}
	/**
	 * Delegates the call to each FriendSource and returns the union 
	 * of the results.
	 */
	public List<Friend> getFriends() {
		ArrayList<Friend> toReturn = new ArrayList<Friend>();
		
		for(FriendSource s : _friendSources)
		{
			toReturn.addAll(s.getFriends());
		}
		
		return toReturn;
	}
	/**
	 * Delegates the call to each EventSource and returns the union 
	 * of the results.
	 */
	public List<Event> getEvents(String location) {
		ArrayList<Event> toReturn = new ArrayList<Event>();
		
		for(EventSource s : _eventSources)
		{
			toReturn.addAll(s.getEvents(location));
		}
		
		return toReturn;
	}
}
