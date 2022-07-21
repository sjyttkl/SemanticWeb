package FriendTracker.Friendtracker.friendtracker.data.implementation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import net.semwebprogramming.chapter9.JabberToRdf;
import net.semwebprogramming.friendtracker.FriendTrackerConfiguration.JabberConfiguration;
import net.semwebprogramming.friendtracker.data.FriendSource;
import net.semwebprogramming.friendtracker.data.implementation.vocabulary.FriendTracker;
import net.semwebprogramming.friendtracker.model.Friend;
import net.semwebprogramming.friendtracker.utilities.ProperException;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * An implementation of the FriendSource interface that provides 
 * information about friends from Jabber.
 */
public class JabberFriendSource extends JenaSource implements FriendSource {

	private JabberToRdf _jabber;
	private JabberConfiguration _config;
	
	public JabberFriendSource()
	{
	}
	
	@Override
	protected void createModel() {
		_model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF); 
//		_model = ModelFactory
//				.createOntologyModel(PelletReasonerFactory.THE_SPEC);
	}

	@Override
	public void initialize(Object configurationObject) {
		if (configurationObject instanceof JabberConfiguration) {
			_config = (JabberConfiguration) configurationObject;
		} else {
			throw new ProperException(
					new IllegalArgumentException(
							"UpcomingEventSource must be configured with a instance of UpcomingConfiguration."));
		}

		String mappingLocation = _config.getJabberMappingLocation();
		String friendtrackerOntologyLocation = _config.getFriendTrackerOntologyLocation();
		
		FileInputStream mappingStream = null;
		FileInputStream friendtrackerOntologyStream = null;
		
		try {
			mappingStream = new FileInputStream(mappingLocation);
			friendtrackerOntologyStream = new FileInputStream(friendtrackerOntologyLocation);
			// bring in the mapping stuff - OWL + rules...
			_model.read(friendtrackerOntologyStream, "http://semwebprogramming.org:8099/ont/friendtracker-ont#", "TURTLE");
			
			_model.prepare();
			
			_model.read(mappingStream, "http://semwebprogramming.org:8099/ont/friendtracker-jabber-mapping#", "TURTLE");
			
			_model.prepare();
		} catch (FileNotFoundException e) {
			throw new ProperException(e);
		}
		
		_jabber = new JabberToRdf();
	}

	public List<Friend> getFriends() {
		List<Friend> toReturn;
		
		_model.read(
				_jabber.retrieveFriends(
						_config.getServer(), 
						_config.getLogin(),
						_config.getPassword()),
				"",
				"TURTLE");
		
		convertStatus();
		
		toReturn = extractFriends();
		
		return toReturn;
	}

	private Model performQuery(String ftProp, String ftVal, String jabProp, String jabVal)
	{
		Model toReturn;
		String queryFormatString =  
			"CONSTRUCT {\n " +
			"?person <%1$s> <%2$s> .\n" +
			"}\n" +
			"WHERE {\n" +
			"?person <%3$s> <%4$s> .\n" +
			"}";
		String queryString = String.format(
				queryFormatString,
				ftProp,
				ftVal,
				jabProp,
				jabVal);
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExec = QueryExecutionFactory.create(query, _model);
		
		toReturn = queryExec.execConstruct();
		
		return toReturn;
	}
	
	private void convertStatus() {
		final String jabberOnt = _config.getBaseOntologyUri();
		
		_model.add(
				performQuery(
						FriendTracker.hasStatus.getString(),
						FriendTracker.AvailableStatus.getString(),
						jabberOnt + "presenceMode",
						jabberOnt + "Available"));
		_model.add(
				performQuery(
						FriendTracker.hasStatus.getString(),
						FriendTracker.AwayStatus.getString(),
						jabberOnt + "presenceMode",
						jabberOnt + "Away"));
		_model.add(
				performQuery(
						FriendTracker.hasStatus.getString(),
						FriendTracker.BusyStatus.getString(),
						jabberOnt + "presenceMode",
						jabberOnt + "DoNotDisturb"));
		_model.add(
				performQuery(
						FriendTracker.hasStatus.getString(),
						FriendTracker.OfflineStatus.getString(),
						jabberOnt + "presenceType",
						jabberOnt + "Unavailable"));
	}
}
