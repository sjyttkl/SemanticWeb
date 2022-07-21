package FriendTracker.Friendtracker.friendtracker.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import net.semwebprogramming.friendtracker.FriendTrackerConfiguration.FacebookConfiguration;
import net.semwebprogramming.friendtracker.utilities.ProperException;

/**
 *  This class presents users a dialog with a button to click once they 
 *  have logged into Facebook. 
 *
 */
public class FacebookLoginDialog extends JDialog implements ActionListener {

	JLabel _msgLabel;
	FacebookConfiguration _config;
	URL _loginUrl;
	JButton _closeButton;
	
	public FacebookLoginDialog(FacebookConfiguration config, URL loginUrl)
	{
		_config = config;
		_loginUrl = loginUrl;
		initializeUI();
	}

	
	public void actionPerformed(ActionEvent ae) {
		if(_closeButton.equals(ae.getSource()))
		{
			this.setVisible(false);
		}
	}
	
	
	private void initializeUI() {
		
		_msgLabel = new JLabel("Click close once you have logged into Facebook.");
		_closeButton = new JButton("Close");
		
		_closeButton.addActionListener(this);
		
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(_msgLabel, BorderLayout.CENTER);
		getContentPane().add(_closeButton, BorderLayout.SOUTH);
		
		setTitle("Log into Facebook");
		pack();
	}

	@Override
	public void setVisible(boolean arg0) {
		String execString;
		
		// add quotes around the browser location if necessary (i.e. "Program Files")
		execString = -1 == _config.getBrowserLocation().indexOf(" ") ? 
				String.format("%1$s %2$s", 
                       _config.getBrowserLocation(), _loginUrl.toString()) :
   				String.format("\"%1$s\" %2$s", 
                        _config.getBrowserLocation(), _loginUrl.toString());                    	   
		File test = new File(_config.getBrowserLocation());
		if(!test.exists())
		{
			System.err.println("The system could not find the path for your " +
					"browser at '" + _config.getBrowserLocation() + "'.  Please " +
					"check the value in the 'conf/friendtracker.xml' file."); 
		}
		else {
			try {
				Runtime.getRuntime().exec(execString);
			} catch (MalformedURLException e) {
				throw new ProperException(e);
			} catch (IOException e) {
				throw new ProperException(e);
			}
			super.setVisible(arg0);
		}
	}
	
}
