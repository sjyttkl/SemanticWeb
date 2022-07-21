package FriendTracker.Friendtracker.friendtracker.ui;

import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.semwebprogramming.friendtracker.model.Friend;
import net.semwebprogramming.friendtracker.utilities.Utilities;

/**
 *  Graphical component to present information about a single friend 
 *  to the user. 
 *
 */
public class FriendViewerPanel extends JPanel {

	private static final String NO_IMAGE_TEXT = "No picture available";
	private static final String NO_ORIGIN_TEXT = "an unknown location";
	private static final String ORIGIN_FORMAT_TEXT = "This person is from %1$s.";
	
	private Friend _friend;

	JLabel _friendNameLabel = new JLabel("Name");

	JTextField _friendName = new JTextField();

	JLabel _friendHandleLabel = new JLabel("Handle");

	JTextField _friendHandle = new JTextField();

	JLabel _friendEmailLabel = new JLabel("Email");

	JTextField _friendEmail = new JTextField();

	JLabel _friendStatusLabel = new JLabel("Status");

	JTextField _friendStatus = new JTextField();

	JLabel _imageLabel = new JLabel();

	JTextArea _otherInfo = new JTextArea();

	public FriendViewerPanel() {
		JPanel grid = new JPanel();
		grid.setLayout(new BoxLayout(grid, BoxLayout.Y_AXIS));
		JPanel temp;
		
		temp = new JPanel();
		temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));
		temp.add(_friendNameLabel);
		temp.add(_friendName);
		grid.add(temp);
		
		temp = new JPanel();
		temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));
		temp.add(_friendHandleLabel);
		temp.add(_friendHandle);
		grid.add(temp);

		temp = new JPanel();
		temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));
		temp.add(_friendEmailLabel);
		temp.add(_friendEmail);
		grid.add(temp);

		temp = new JPanel();
		temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));
		temp.add(_friendStatusLabel);
		temp.add(_friendStatus);
		grid.add(temp);

		setLayout(new BorderLayout());
		add(_imageLabel, BorderLayout.WEST);
		add(grid, BorderLayout.CENTER);
		add(_otherInfo, BorderLayout.SOUTH);
		
		_otherInfo.setText(String.format(ORIGIN_FORMAT_TEXT, NO_ORIGIN_TEXT));
		
		_imageLabel.setText(FriendViewerPanel.NO_IMAGE_TEXT);
	}

	/**
	 * Repopulates the display with information about the given friend, 
	 * or clears the display if the parameter is null
	 * 
	 * @param friend the friend whose information to display, or null
	 */
	public void setFriend(Friend friend) {
		_friend = friend;
		
		if (null != _friend) {
			_friendName.setText(Utilities.concatenateStringSet(_friend.getNames()));
			_friendHandle.setText(Utilities.concatenateStringSet(friend.getHandles()));
			_friendEmail.setText(Utilities.concatenateStringSet(_friend.getEmailAddresses()));
			_friendStatus.setText(_friend.getOnlineStatus().name());
			_otherInfo.setText(
					String.format(ORIGIN_FORMAT_TEXT,
							null != _friend.getOrigin() ? _friend.getOrigin() : NO_ORIGIN_TEXT 
							));
			if(null != friend.getPictureURL())
			{
				try {
					_imageLabel.setIcon(new ImageIcon(new URL(friend.getPictureURL())));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				_imageLabel.setText("");
			}
			else
			{
				_imageLabel.setIcon(null);
				_imageLabel.setText(FriendViewerPanel.NO_IMAGE_TEXT);
			}
		}

		repaint();
	}

}
