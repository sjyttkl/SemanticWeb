package FriendTracker.Friendtracker.friendtracker.data.implementation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.semwebprogramming.facebook.FacebookRESTClient;
import net.semwebprogramming.facebook.FacebookSession;
import net.semwebprogramming.friendtracker.FriendTrackerConfiguration.FacebookConfiguration;
import net.semwebprogramming.friendtracker.data.FriendSource;
import net.semwebprogramming.friendtracker.model.Friend;
import net.semwebprogramming.friendtracker.utilities.ProperException;
import net.semwebprogramming.friendtracker.utilities.Utilities;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * An implementation of the FriendSource interface that provides 
 * information about friends from Facebook.
 */
public class FacebookFriendSource extends JenaSource implements FriendSource {

	private FacebookRESTClient _client;
	private FacebookConfiguration _config;
	private String _authToken = null;
	
	
	public String getAuthToken()
	{
		if(null == _authToken)
		{
			_authToken = _client.getAuthToken();
		}

		return _authToken;
	}
	
	@Override
	protected void createModel() {
		_model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
	}

	public URL getLoginURL()
	{
		String authToken = getAuthToken();
		return _client.getLoginUrl(authToken);
	}
	
	@Override
	public void initialize(Object configurationObject) {
		if(!(configurationObject instanceof FacebookConfiguration))
		{
			throw new ProperException(new IllegalArgumentException("Configuration must be a FacebookConfiguration object!"));
		}
		_config = (FacebookConfiguration) configurationObject;
		try {
			_client = new FacebookRESTClient(
					_config.getApiKey(), 
					_config.getSecret(),
					new URL(_config.getWebServiceUrl()),
					new URL(_config.getLoginUrl()),
					new URL(_config.getXSLLocation()));
		} catch (MalformedURLException e) {
			throw new ProperException(e);
		}
		
		String mappingLocation = _config.getMappingLocation();
		String friendtrackerOntologyLocation = _config.getFriendTrackerOntologyLocation();
		
		FileInputStream mappingStream = null;
		FileInputStream friendtrackerOntologyStream = null;
		
		try {
			mappingStream = new FileInputStream(mappingLocation);
			friendtrackerOntologyStream = new FileInputStream(friendtrackerOntologyLocation);
			// bring in the mapping stuff - OWL + rules...
			_model.read(friendtrackerOntologyStream, "http://semwebprogramming.org:8099/ont/friendtracker-ont#", "TURTLE");
			
			_model.prepare();
			
			_model.read(mappingStream, "http://semwebprogramming.org:8099/ont/friendtracker-facebook-mapping#", "TURTLE");
			
			_model.prepare();
		} catch (FileNotFoundException e) {
			throw new ProperException(e);
		}
	}

	public List<Friend> getFriends() {
		
		List<Friend> toReturn;
		String friendUids;
		String authToken = getAuthToken();
		FacebookSession session;
		Collection<String> friends;
		
		session  = _client.createSession(authToken);
		friends  = _client.getFriendsList(session);
		
		friendUids = Utilities.concatenateStringSet(new HashSet<String>(friends));
		
//		BufferedReader reader = new BufferedReader(new InputStreamReader(_client.getPopulatedFriendsXml(session, friendUids, _config.getFields())));
		BufferedReader reader = new BufferedReader(new InputStreamReader(_client.getPopulatedFriendsRdf(session, friendUids, _config.getFields(), "TURTLE")));
		StringBuilder sb = new StringBuilder();
		String oneLine;
		
		try {
			while(null != (oneLine = reader.readLine()))
			{
				sb.append(oneLine).append("\n");
			}
		} catch (IOException e) {
			throw new ProperException(e);
		}
		
		System.out.println("FacebookFriendSource:");
		System.out.println(sb.toString());
		
		
		_model.read(
				_client.getPopulatedFriendsRdf(session, friendUids, _config.getFields(), "TURTLE"),
				"",
				"TURTLE");
				
		toReturn = extractFriends();
		
		return toReturn;
	}
	
}
