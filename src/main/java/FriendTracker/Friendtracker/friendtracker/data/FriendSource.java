package FriendTracker.Friendtracker.friendtracker.data;

import java.util.List;

import net.semwebprogramming.friendtracker.model.Friend;


/**
 * An interface that can supply Friend objects.
 */
public interface FriendSource {
	List<Friend> getFriends();
}
