package FriendTracker.Friendtracker.friendtracker.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.semwebprogramming.friendtracker.data.SourceCollection;
import net.semwebprogramming.friendtracker.model.Event;
import net.semwebprogramming.friendtracker.model.Friend;
import net.semwebprogramming.friendtracker.model.FriendTrackerChangeListener;
import net.semwebprogramming.friendtracker.model.FriendTrackerModel;
import net.semwebprogramming.friendtracker.model.Post;

/**
 *  The main window of the FriendTracker application.  This control 
 *  serves as host for many of the other UI components in the 
 *  program. 
 *
 */
public class FriendTrackerWindow extends JFrame 
implements FriendTrackerChangeListener, ListSelectionListener {

	private FriendTrackerModel _model;
	private FriendListPanel _friendListPanel;
	private FriendViewerPanel _friendViewerPanel;
	private JSplitPane _friendPostSplitPane;
	private PostsPanel _postPanel;
	private EventPanel _eventPanel;
	private SourceCollection _sources;
	private JSplitPane _mainSplitPane;
	
	/**
	 * Initializes the FriendTrackerWindow.  Given a model and a collection
	 * of sources, the FriendTrackerWindow presents and manages the rest of 
	 * the behavior of the application. 
	 * 
	 * @param model A model object to represent the data in this UI
	 * @param sources a SourceCollection object to interrogate for 
	 * information about friends, events, and blog posts 
	 */
	public void initialize(FriendTrackerModel model, SourceCollection sources) {
		_model = model;
		_model.addFriendTrackerChangeListener(this);
	
		_sources = sources;
		
		_friendListPanel = new FriendListPanel();
		_friendListPanel.initialize(model);
		
		_friendListPanel.addListSelectionListener(this);
		
		_postPanel = new PostsPanel();
		_postPanel.initialize();
		
		_eventPanel = new EventPanel();
		_eventPanel.initialize(model);
		
		_friendViewerPanel = new FriendViewerPanel();
		
		_friendPostSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _friendViewerPanel, _postPanel);
		
		initializeUI();
		initializeData();
	}
	
	/**
	 * This method initializes the UI with data, i.e. Friend 
	 * data.
	 *
	 */
	private void initializeData() {

		for(Friend f : _sources.getFriends())
		{
			_model.addFriend(f);
		}
	}

	/**
	 * Called at application closing to release resources.
	 *
	 */
	public void cleanup()
	{
		_model.removeFriendTrackerChangeListener(this);
		_friendListPanel.cleanup();
	}

	private void initializeUI() {

		_mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, _friendPostSplitPane, _eventPanel);
		
		setTitle("Friend Tracker");
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(_friendListPanel, BorderLayout.WEST);
		getContentPane().add(_mainSplitPane, BorderLayout.CENTER);
		pack();
	}

	/**
	 * Event handler
	 */
	public void valueChanged(ListSelectionEvent arg) {
		Friend friend;
		
		int selectedIndex;
		JList src = (JList) arg.getSource();
		
		if(!arg.getValueIsAdjusting())
		{
			for(selectedIndex = arg.getFirstIndex(); selectedIndex <= arg.getLastIndex(); selectedIndex++)
			{
				if(src.isSelectedIndex(selectedIndex))
				{
					break;
				}
			}
			
			if(src.getModel().getSize() > selectedIndex)
			{
				friend = (Friend) src.getModel().getElementAt(selectedIndex);
				_friendViewerPanel.setFriend(friend);
				
				for(Event e : _model.getEvents())
				{
					_model.removeEvent(e);
				}
				if(null != friend.getOrigin())
				{
					for(Event e : _sources.getEvents(friend.getOrigin()))
					{
						_model.addEvent(e);
					}
				}
				_postPanel.setPosts(_sources.getPosts(friend));
			}
		}
	}
	
	/**
	 * Event handler
	 */
	public void eventAdded(Event newEvent) {
	}
	
	/**
	 * Event handler
	 */
	public void postsCleared() {
		// do nothing
	}
	
	/**
	 * Event handler
	 */
	public void eventsCleared() {
		// do nothing
	}
	
	/**
	 * Event handler
	 */
	public void eventRemoved(Event toRemoveEvent) {
		// do nothing		
	}
	
	/**
	 * Event handler
	 */
	public void friendAdded(Friend newFriend) {
		// do nothing
	}
	
	/**
	 * Event handler
	 */
	public void friendRemoved(Friend toRemoveFriend) {
		// do nothing
	}
	
	/**
	 * Event handler
	 */
	public void postAdded(Post newPost) {
		// do nothing
	}

	/**
	 * Event handler
	 */
	public void postRemoved(Post toRemovePost) {
		// do nothing
	}
	
}
