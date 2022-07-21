package FriendTracker.Friendtracker.friendtracker.data;

import java.util.List;

import net.semwebprogramming.friendtracker.model.Friend;
import net.semwebprogramming.friendtracker.model.Post;

/**
 * An interface that can supply Post objects.
 */
public interface PostSource {
	List<Post> getPosts(Friend author);
}
