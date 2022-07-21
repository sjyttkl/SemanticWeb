package FriendTracker.Friendtracker.friendtracker.model;

import java.util.Date;

/**
 *  Interface describing a blog Post. 
 */
public interface Post {

	/**
	 * The post's title.
	 */
	String getTitle();
	/**
	 * The content of the post.
	 */
	String getContent();
	/**
	 * The date the post was created.
	 */
	Date getPostDate();
	/**
	 * A unique ID for the blog post.
	 */
	String getID();
}
