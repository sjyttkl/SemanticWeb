package FriendTracker.Friendtracker.friendtracker.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import net.semwebprogramming.friendtracker.utilities.ProperException;

/** 
 *  A graphical component which displays a map with a red 'X'
 *  at the center.  
 * 
 *
 */
public class MapPanel extends JLabel {

	/**
	 * A format string used to fetch a PNG image from Yahoo centered at 
	 * a particular lat/long.  The values of the parameters are as follows:
	 * 
	 * 1 = lat, 
	 * 2 = lon, 
	 * 3 = scale (~13600), 
	 * 4 = format (jpg, gif, png), 
	 * 5 = width,
	 * 6 = height
	 */
	public static final String URL_FORMAT_STRING = "http://gws.maps.yahoo.com/MapImage?clat=%1$s&clon=%2$s&ims=%3$s&imf=%4$s&imw=%5$s&imh=%6$s";
	private Font _mapFont;
	private final ImageIcon _imageIcon;
	
	public MapPanel()
	{
		super();
		_imageIcon = new ImageIcon();

		this.setHorizontalAlignment(JLabel.CENTER);
		this.setVerticalAlignment(JLabel.CENTER);
		setIcon(_imageIcon);

		_mapFont = getFont().deriveFont(Font.BOLD, 15);
		
	}
	
	/**
	 * Fetches a picture of a map from Yahoo Maps, adds it to the 
	 * center of the label, and draws a red 'X' at the center of 
	 * it.
	 * 
	 * @param latitude the latitude 
	 * @param longitude the longitude
	 */
	public void setLocation(double latitude, double longitude)
	{
		URL url;
		Image newImage;

		try {
			url = new URL(String.format(URL_FORMAT_STRING,
					latitude,
					longitude,
					13600,
					"png",
					this.getWidth(),
					this.getHeight()));
			newImage = ImageIO.read(url);
			_imageIcon.setImage(newImage);
		} catch (MalformedURLException e) {
			throw new ProperException(e);
		} catch (IOException e) {
			throw new ProperException(e);
		}
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		g.setColor(Color.RED);
		g.setFont(_mapFont);
		g.drawString("X", this.getWidth() / 2, this.getHeight() / 2);
	}
	
}
