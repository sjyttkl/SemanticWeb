package FriendTracker.Friendtracker.friendtracker;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import net.semwebprogramming.friendtracker.data.FriendSource;
import net.semwebprogramming.friendtracker.data.PostSource;
import net.semwebprogramming.friendtracker.data.SourceCollection;
import net.semwebprogramming.friendtracker.data.implementation.FacebookFriendSource;
import net.semwebprogramming.friendtracker.data.implementation.JabberFriendSource;
import net.semwebprogramming.friendtracker.data.implementation.UpcomingEventSource;
import net.semwebprogramming.friendtracker.data.implementation.WordPressSource;
import net.semwebprogramming.friendtracker.model.FriendTrackerModel;
import net.semwebprogramming.friendtracker.model.OnlineStatus;
import net.semwebprogramming.friendtracker.model.implementation.FriendImpl;
import net.semwebprogramming.friendtracker.model.implementation.FriendTrackerModelImpl;
import net.semwebprogramming.friendtracker.ui.FacebookLoginDialog;
import net.semwebprogramming.friendtracker.ui.FriendTrackerWindow;
import net.semwebprogramming.friendtracker.ui.LoadingDialog;

/**
 *  This is the main FriendTracker application.   
 *
 */
public class FriendTracker {

	private static FriendTracker _me;
	private FriendTrackerModel _model;
	private FriendTrackerConfiguration _configuration;
	private FriendTrackerWindow _window;
	private SourceCollection _sources;
	
	private void initialize()
	{
		// get the configuration
		_configuration = FriendTrackerConfigurator.getConfiguration("conf/friendtracker.xml");

		// instantiate the model for the UI and the data sources it will use
		_model = new FriendTrackerModelImpl();
		_sources = new SourceCollection();
		
		
		// set up the upcoming event source
		UpcomingEventSource upcoming;
		upcoming = new UpcomingEventSource();
		upcoming.initialize(_configuration.getUpcomingConfiguration());

		// set up the jabber friend source
		JabberFriendSource jabber;
		jabber = new JabberFriendSource();
		jabber.initialize(_configuration.getJabberConfiguration());
		
		WordPressSource wordPress;
		wordPress = new WordPressSource();
		wordPress.initialize(_configuration.getWordPressConfiguration());
		
		// set up the facebook friend source
		FacebookFriendSource facebook;
		facebook = new FacebookFriendSource();
		facebook.initialize(_configuration.getFacebookConfiguration());
		
		FacebookLoginDialog loginDialog = new FacebookLoginDialog(
				_configuration.getFacebookConfiguration(), 
				facebook.getLoginURL());
		
		loginDialog.setModal(true);
		loginDialog.setVisible(true);
		
		_sources.addSource(upcoming);
		_sources.addSource(jabber);
		_sources.addSource(facebook);
		
		_sources.addSource((FriendSource) wordPress);
		_sources.addSource((PostSource) wordPress);
		
		_window = new FriendTrackerWindow();
		
		// Pop up the loading dialog so that users can have something 
		// to look at while Facebook is loading...
		LoadingDialog ld = new LoadingDialog("Initializing", "Loading data sources.  Please wait...");
		
		ld.setVisible(true);
		_window.initialize(_model, _sources);
		ld.setVisible(false);
	}
	
	private void start()
	{
		_window.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent args) {
				super.windowClosing(args);
				System.exit(0);
			}	
		});
		
		_window.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		_me = new FriendTracker();
		_me.initialize();
		_me.start();
	}
}
