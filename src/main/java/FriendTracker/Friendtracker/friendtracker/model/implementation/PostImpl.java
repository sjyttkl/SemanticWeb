package FriendTracker.Friendtracker.friendtracker.model.implementation;

import java.util.Date;

import net.semwebprogramming.friendtracker.model.Post;

/**
 *  A concrete implementation of the Post interface. 
 */
public class PostImpl implements Post {

	private final int CONTENT_SUMMARY_MAX_LENGTH = 25;
	private String _content;
	private String _id;
	private Date _postDate;
	private String _title;
	
	public PostImpl()
	{
		this(null, null, null, null);
	}
	
	public PostImpl(String id, String title, String content, Date postDate)
	{
		_id = id;
		_title = title;
		_content = content;
		_postDate = postDate;
	}
	
	public String getContent() {
		return _content;
	}
	public void setContent(String content) {
		this._content = content;
	}
	public String getID() {
		return _id;
	}
	public void setID(String id) {
		_id = id;
	}
	public Date getPostDate() {
		return _postDate;
	}
	public void setPostDate(Date postDate) {
		this._postDate = postDate;
	}
	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}

	@Override
	public String toString() {
		String shortenedContent = _content;
		
		if(null != shortenedContent && shortenedContent.length() > CONTENT_SUMMARY_MAX_LENGTH)
		{
			shortenedContent = shortenedContent.substring(0, CONTENT_SUMMARY_MAX_LENGTH) + "...";
		}
		
		return String.format("%1$s: %2$s",
				_title,
				shortenedContent);
	}
	
	
}
