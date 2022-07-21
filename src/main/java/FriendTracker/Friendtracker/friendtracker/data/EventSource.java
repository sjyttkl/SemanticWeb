package FriendTracker.Friendtracker.friendtracker.data;

import java.util.List;

import net.semwebprogramming.friendtracker.model.Event;

/**
 * An interface that can supply Event objects.
 */
public interface EventSource {
	List<Event> getEvents(String location);
}
