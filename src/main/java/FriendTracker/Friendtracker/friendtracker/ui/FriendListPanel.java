package FriendTracker.Friendtracker.friendtracker.ui;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;

import net.semwebprogramming.friendtracker.model.Event;
import net.semwebprogramming.friendtracker.model.Friend;
import net.semwebprogramming.friendtracker.model.FriendTrackerChangeListener;
import net.semwebprogramming.friendtracker.model.FriendTrackerModel;
import net.semwebprogramming.friendtracker.model.Post;

/**
 *  This subclass of JPanel presents users with a list of friends.
 *
 */
public class FriendListPanel extends JPanel implements FriendTrackerChangeListener {

	private FriendTrackerModel _model;
	private final JList _listbox;
	private JScrollPane _listScroll;
	private final DefaultListModel _listModel;
	
	public FriendListPanel()
	{
		_listModel = new DefaultListModel();
		_listbox = new JList(_listModel);
		
		_listScroll = new JScrollPane(_listbox);
		_listScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		_listScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		setLayout(new BorderLayout());
		add(new JLabel("Friends"), BorderLayout.NORTH);
		add(_listScroll, BorderLayout.CENTER);
	}

	public void initialize(FriendTrackerModel model)
	{
		_model = model;
		_model.addFriendTrackerChangeListener(this);
		for(Friend f : _model.getFriends())
		{
			friendAdded(f);
		}
	}
	
	public void cleanup()
	{
		_model.removeFriendTrackerChangeListener(this);
	}
	
	public void friendAdded(Friend newFriend) {
		_listModel.addElement(newFriend);
	}

	public void friendRemoved(Friend toRemoveFriend) {
		_listModel.removeElement(toRemoveFriend);
	}

	/**
	 * Exposes event registration for the listbox in this component 
	 * to other clients.
	 * 
	 * @param lsl a list selection listener
	 */
	public void addListSelectionListener(ListSelectionListener lsl)
	{
		_listbox.addListSelectionListener(lsl);
	}

	/**
	 * Exposes event unregistration for the listbox in this component 
	 * to other clients.
	 * 
	 * @param lsl a list selection listener
	 */
	public void removeListSelectionListener(ListSelectionListener lsl)
	{
		_listbox.removeListSelectionListener(lsl);
	}

	public void postAdded(Post newPost) {
		// do nothing
	}
	public void postsCleared() {
		// do nothing
	}
	public void eventsCleared() {
		// do nothing
	}

	public void postRemoved(Post toRemovePost) {
		// do nothing
	}

	public void eventAdded(Event newEvent) {
		// do nothing
	}
	public void eventRemoved(Event toRemoveEvent) {
		// do nothing
	}
}
