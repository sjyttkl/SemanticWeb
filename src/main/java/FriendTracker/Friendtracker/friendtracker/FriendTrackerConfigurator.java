package FriendTracker.Friendtracker.friendtracker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.semwebprogramming.friendtracker.FriendTrackerConfiguration.FacebookConfiguration;
import net.semwebprogramming.friendtracker.FriendTrackerConfiguration.JabberConfiguration;
import net.semwebprogramming.friendtracker.FriendTrackerConfiguration.UpcomingConfiguration;
import net.semwebprogramming.friendtracker.FriendTrackerConfiguration.WordPressConfiguration;
import net.semwebprogramming.friendtracker.utilities.ProperException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *  This class parses the FriendTracker configuration files and produces
 *  various configuration objects which hold the parameters for the 
 *  different modules of the application.   
 *
 */
public class FriendTrackerConfigurator {

	private static final String FRIENDTRACKER_ELEMENT = "friendtracker";
	private static final String JABBER_ELEMENT = "jabber";
	private static final String WORDPRESS_ELEMENT = "wordpress";
	private static final String FACEBOOK_ELEMENT = "facebook";
	private static final String UPCOMING_ELEMENT = "upcoming";
	private static final String FRIENDTRACKER_ONTOLOGY_LOCATION = "friendtrackerOntologyLocation";
	
	/**
	 * Parses the FriendTracker XML configuration file and returns an 
	 * object that describes the relevant parameters for how FriendTracker 
	 * should behave.
	 * @param confStream the XML configuration stream 
	 * @return a configuration object that describes how the 
	 * FriendTracker application should behave
	 */
	public static FriendTrackerConfiguration getConfiguration(InputStream confStream)
	{
		FriendTrackerConfiguration toReturn = new FriendTrackerConfiguration();
		Document dom = null;
		
		try {
			dom = DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse(confStream);
		} catch (SAXException e) {
			throw new ProperException(e);
		} catch (IOException e) {
			throw new ProperException(e);		
		} catch (ParserConfigurationException e) {
			throw new ProperException(e);
		}
		
		toReturn = buildConfiguration(toReturn, dom);
		
		return toReturn;
	}
	
	private static FriendTrackerConfiguration buildConfiguration(FriendTrackerConfiguration conf, Document dom) {
		
		NodeList tempList;
		Node tempNode;
		NamedNodeMap attributes;
		String friendTrackerOntLocation;
		
		JabberConfiguration jabber;
		UpcomingConfiguration upcoming;
		FacebookConfiguration facebook;
		WordPressConfiguration wordpress;
		
		// do overall...
		tempList = dom.getElementsByTagName(FRIENDTRACKER_ELEMENT);
		tempNode = tempList.item(0);
		attributes = tempNode.getAttributes();
		friendTrackerOntLocation = attributes.getNamedItem(FRIENDTRACKER_ONTOLOGY_LOCATION).getNodeValue();
		
		
		// do jabber...
		tempList = dom.getElementsByTagName(JABBER_ELEMENT);
		tempNode = tempList.item(0);
		attributes = tempNode.getAttributes();
		
		jabber = getJabber(conf, attributes, friendTrackerOntLocation);

		// do wordpress...
		tempList = dom.getElementsByTagName(WORDPRESS_ELEMENT);
		tempNode = tempList.item(0);
		attributes = tempNode.getAttributes();
		
		wordpress = getWordPress(conf, attributes, friendTrackerOntLocation);

		
		// do upcoming...
		tempList = dom.getElementsByTagName(UPCOMING_ELEMENT);
		tempNode = tempList.item(0);
		attributes = tempNode.getAttributes();
		
		upcoming = getUpcoming(conf, attributes, friendTrackerOntLocation);

		// do facebook...
		tempList = dom.getElementsByTagName(FACEBOOK_ELEMENT);
		tempNode = tempList.item(0);
		attributes = tempNode.getAttributes();
		
		facebook = getFacebook(conf, attributes, friendTrackerOntLocation);

		conf.setFacebookConfiguration(facebook);
		conf.setJabberConfiguration(jabber);
		conf.setUpcomingConfiguration(upcoming);
		conf.setWordPressConfiguration(wordpress);
		
		return conf;
	}

	private static WordPressConfiguration getWordPress(FriendTrackerConfiguration conf,
			NamedNodeMap attributes,
			String friendtrackerOntLocation) {
		String xslLocation;
		String mappingLocation;
		String queryLocation;
		String queryName;
		
		xslLocation = attributes.getNamedItem(WordPressConfiguration.WORDPRESS_MAPPING_XSL).getNodeValue();
		mappingLocation = attributes.getNamedItem(WordPressConfiguration.WORDPRESS_MAPPING_LOCATION).getNodeValue();
		queryLocation = attributes.getNamedItem(WordPressConfiguration.WORDPRESS_QUERY_LOCATION).getNodeValue();
		queryName = attributes.getNamedItem(WordPressConfiguration.WORDPRESS_QUERY_NAME).getNodeValue();
		return conf.new WordPressConfiguration(mappingLocation, xslLocation, queryLocation, queryName);
	}
	
	private static FacebookConfiguration getFacebook(FriendTrackerConfiguration conf,
			NamedNodeMap attributes,
			String friendtrackerOntLocation) {
		FacebookConfiguration toReturn;
		
		String fields;
		String wsUrl;
		String loginUrl;
		String secret;
		String apiKey;
		String browserLocation;
		String xslLocation;
		String mappingLocation;
		String ontUri;
		
		fields = attributes.getNamedItem(FacebookConfiguration.FACEBOOK_FIELDS_ATTRIBUTE).getNodeValue();
		wsUrl = attributes.getNamedItem(FacebookConfiguration.FACEBOOK_WEB_SERVICE_URL_ATTRIBUTE).getNodeValue();
		loginUrl = attributes.getNamedItem(FacebookConfiguration.FACEBOOK_LOGIN_URL_ATTRIBUTE).getNodeValue();
		secret = attributes.getNamedItem(FacebookConfiguration.FACEBOOK_SECRET_ATTRIBUTE).getNodeValue();
		apiKey = attributes.getNamedItem(FacebookConfiguration.FACEBOOK_API_KEY_ATTRIBUTE).getNodeValue();
		browserLocation = attributes.getNamedItem(FacebookConfiguration.FACEBOOK_BROWSER_LOCATION_ATTRIBUTE).getNodeValue();
		xslLocation = attributes.getNamedItem(FacebookConfiguration.FACEBOOK_XSL_LOCATION).getNodeValue();
		mappingLocation = attributes.getNamedItem(FacebookConfiguration.FACEBOOK_MAPPING_LOCATION).getNodeValue();
		ontUri = attributes.getNamedItem(FacebookConfiguration.FACEBOOK_ONTOLOGY_URI).getNodeValue();
		
		toReturn = conf.new FacebookConfiguration(
				fields, 
				browserLocation, 
				secret,
				apiKey,
				wsUrl,
				loginUrl,
				xslLocation, 
				mappingLocation,
				ontUri,
				friendtrackerOntLocation);
		
		return toReturn;
	}

	private static UpcomingConfiguration getUpcoming(FriendTrackerConfiguration conf, 
			NamedNodeMap attributes,
			String friendtrackerOntologyLocation) {
		UpcomingConfiguration toReturn = null;
		String mappingOntologyLocation = null;
		String yahooDeveloperKey;
		String baseOntologyUri;
		
		yahooDeveloperKey = attributes.getNamedItem(UpcomingConfiguration.YAHOO_DEVELOPER_KEY_ATTRIBUTE).getNodeValue();
		mappingOntologyLocation = attributes.getNamedItem(UpcomingConfiguration.MAPPING_ONTOLOGY_LOCATION_ATTRIBUTE).getNodeValue();
		baseOntologyUri = attributes.getNamedItem(UpcomingConfiguration.BASE_ONT_URI).getNodeValue();
		
		toReturn = conf.new UpcomingConfiguration(yahooDeveloperKey, mappingOntologyLocation, baseOntologyUri, friendtrackerOntologyLocation);
		
		return toReturn;
	}

	private static JabberConfiguration getJabber(FriendTrackerConfiguration conf, 
			NamedNodeMap attributes,
			String friendtrackerOntologyLocation) {
		JabberConfiguration toReturn = null;
		String server;
		String login;
		String password;

		String baseOntologyUri;
		String jabberMappingLocation;

		
		server = attributes.getNamedItem(JabberConfiguration.JABBER_SERVER_ATTRIBUTE).getNodeValue();
		login = attributes.getNamedItem(JabberConfiguration.JABBER_LOGIN_ATTRIBUTE).getNodeValue();
		password = attributes.getNamedItem(JabberConfiguration.JABBER_PASSWORD_ATTRIBUTE).getNodeValue();
		
		baseOntologyUri = attributes.getNamedItem(JabberConfiguration.BASE_ONT_URI).getNodeValue();
		jabberMappingLocation = attributes.getNamedItem(JabberConfiguration.MAPPING_ONTOLOGY_LOCATION_ATTRIBUTE).getNodeValue();
		
		toReturn = conf.new JabberConfiguration(server, login, password, baseOntologyUri, friendtrackerOntologyLocation, jabberMappingLocation);
		
		return toReturn;
	}

	public static FriendTrackerConfiguration getConfiguration(String filename)
	{
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			throw new ProperException(e);
		}
		
		return getConfiguration(fis);
	}
}
