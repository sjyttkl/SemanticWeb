package FriendTracker.Friendtracker.friendtracker.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.semwebprogramming.friendtracker.model.Event;
import net.semwebprogramming.friendtracker.model.Friend;
import net.semwebprogramming.friendtracker.model.FriendTrackerChangeListener;
import net.semwebprogramming.friendtracker.model.FriendTrackerModel;
import net.semwebprogramming.friendtracker.model.Post;

public class EventPanel extends JPanel implements FriendTrackerChangeListener, ListSelectionListener {

	private FriendTrackerModel _model;
	private JList _eventList;
	private MapPanel _mapPanel;
	private DefaultListModel _eventListModel;
	
	private JSplitPane _mainSplitPane;
	private JScrollPane _mapScrollPane;
	private JScrollPane _eventScrollPane;
	
	public EventPanel() {
		setLayout(new BorderLayout());
		
		_eventListModel = new DefaultListModel();
		_eventList = new JList(_eventListModel);
		
		_eventScrollPane = new JScrollPane(_eventList);
		_eventScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		_eventScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		_mapPanel = new MapPanel();
		
		_mapScrollPane = new JScrollPane(_mapPanel);
		_mapScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		_mapScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_mapScrollPane.setPreferredSize(new Dimension(150, 150));

		_mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _eventScrollPane, _mapScrollPane);
		_mainSplitPane.setDividerLocation(200);
		
		add(new JLabel("Events"), BorderLayout.NORTH);
		add(_mainSplitPane, BorderLayout.CENTER);
	}

	public void initialize(FriendTrackerModel model) {
		_model = model;
		_model.addFriendTrackerChangeListener(this);

		_eventList.addListSelectionListener(this);
		
		_mapPanel.setLocation(40.689164, -74.045105);
	}

	public void cleanup() {
		_model.removeFriendTrackerChangeListener(this);
		_eventList.removeListSelectionListener(this);
	}

	public void eventAdded(Event newEvent) {
		_eventListModel.addElement(newEvent);
	}

	public void eventRemoved(Event toRemoveEvent) {
		_eventListModel.removeElement(toRemoveEvent);
	}

	public void valueChanged(ListSelectionEvent arg) {
		Event event;
		double lat;
		double lon;
		
		int selectedIndex;
		
		if(!arg.getValueIsAdjusting())
		{
			for(selectedIndex = arg.getFirstIndex(); selectedIndex <= arg.getLastIndex(); selectedIndex++)
			{
				if(_eventList.isSelectedIndex(selectedIndex))
				{
					break;
				}
			}
			if(_eventListModel.size() > selectedIndex)
			{
				event = (Event) _eventListModel.elementAt(selectedIndex);
				lat = event.getVenue().getLatitude();
				lon = event.getVenue().getLongitude();
				
				_mapPanel.setLocation(lat, lon);
			}
		}
	}

	public void postAdded(Post newPost) {
		// TODO Auto-generated method stub

	}

	public void postRemoved(Post toRemovePost) {
		// do nothing
	}

	public void eventsCleared() {
		_eventListModel.clear();
	}
	public void postsCleared() {
		// do nothing	
	}
	public void friendAdded(Friend newFriend) {
		// do nothing
	}

	public void friendRemoved(Friend toRemoveFriend) {
		// do nothing
	}
}
