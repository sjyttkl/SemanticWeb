package FriendTracker.Friendtracker.friendtracker.data.implementation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.semwebprogramming.friendtracker.data.implementation.vocabulary.FriendTracker;
import net.semwebprogramming.friendtracker.data.implementation.vocabulary.Geo;
import net.semwebprogramming.friendtracker.data.implementation.vocabulary.Time;
import net.semwebprogramming.friendtracker.model.Event;
import net.semwebprogramming.friendtracker.model.Friend;
import net.semwebprogramming.friendtracker.model.OnlineStatus;
import net.semwebprogramming.friendtracker.model.Post;
import net.semwebprogramming.friendtracker.model.Venue;
import net.semwebprogramming.friendtracker.model.implementation.EventImpl;
import net.semwebprogramming.friendtracker.model.implementation.FriendImpl;
import net.semwebprogramming.friendtracker.model.implementation.PostImpl;
import net.semwebprogramming.friendtracker.model.implementation.VenueImpl;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 *  An abstract class that can create objects of Friend, Event or Post 
 *  from RDF data encoded according to the FriendTracker ontology.
 *  
 *  It is backed by a Jena model.
 */
public abstract class JenaSource {
	protected OntModel _model;
	protected HashMap<String, Resource> _resourceMap;
	
	public JenaSource()
	{
		setup();
	}
	
	private void setup()
	{
		createModel();
		setupResourceMap();
	}
	
	/**
	 * This method is called during the initialization of the object.
	 * 
	 * At the completion of this call, the _model member MUST be initialized.
	 */
	protected abstract void createModel();
	
	public abstract void initialize(Object configurationObject);
	
	private void setupResourceMap()
	{
		_resourceMap = new HashMap<String, Resource>();
		
		_resourceMap.put(FriendTracker.Event.getString(), 
				_model.createResource(FriendTracker.Event.getString()));

		_resourceMap.put(FriendTracker.Friend.getString(), 
				_model.createResource(FriendTracker.Friend.getString()));

		_resourceMap.put(FriendTracker.hasContent.getString(), 
				_model.createProperty(FriendTracker.hasContent.getString()));

		_resourceMap.put(FriendTracker.hasDescription.getString(), 
				_model.createProperty(FriendTracker.hasDescription.getString()));

		_resourceMap.put(FriendTracker.hasEmailAddress.getString(), 
				_model.createProperty(FriendTracker.hasEmailAddress.getString()));

		_resourceMap.put(FriendTracker.hasHandle.getString(), 
				_model.createProperty(FriendTracker.hasHandle.getString()));
		
		_resourceMap.put(FriendTracker.hasPic.getString(), 
				_model.createProperty(FriendTracker.hasPic.getString()));

		_resourceMap.put(FriendTracker.hasPost.getString(), 
				_model.createProperty(FriendTracker.hasPost.getString()));

		_resourceMap.put(FriendTracker.hasStatus.getString(), 
				_model.createProperty(FriendTracker.hasStatus.getString()));

		_resourceMap.put(FriendTracker.hasTitle.getString(), 
				_model.createProperty(FriendTracker.hasTitle.getString()));

		_resourceMap.put(FriendTracker.hasVenue.getString(), 
				_model.createProperty(FriendTracker.hasVenue.getString()));

		_resourceMap.put(FriendTracker.isFrom.getString(), 
				_model.createProperty(FriendTracker.isFrom.getString()));

		_resourceMap.put(FriendTracker.isNamed.getString(), 
				_model.createProperty(FriendTracker.isNamed.getString()));

		_resourceMap.put(FriendTracker.occursAt.getString(), 
				_model.createProperty(FriendTracker.occursAt.getString()));
		
		_resourceMap.put(FriendTracker.OnlineStatus.getString(), 
				_model.createResource(FriendTracker.OnlineStatus.getString()));

		_resourceMap.put(FriendTracker.Post.getString(), 
				_model.createResource(FriendTracker.Post.getString()));
		
		_resourceMap.put(FriendTracker.Venue.getString(), 
				_model.createResource(FriendTracker.Venue.getString()));

		_resourceMap.put(Geo.latitude.getString(), 
				_model.createProperty(Geo.latitude.getString()));
		
		_resourceMap.put(Geo.longitude.getString(), 
				_model.createProperty(Geo.longitude.getString()));

		_resourceMap.put(Time.Instant.getString(),
				_model.createResource(Time.Instant.getString()));

		_resourceMap.put(Time.Interval.getString(),
				_model.createResource(Time.Interval.getString()));

		_resourceMap.put(Time.inXSDDateTime.getString(),
				_model.createProperty(Time.inXSDDateTime.getString()));
		
		_resourceMap.put(Time.TemporalEntity.getString(),
				_model.createResource(Time.TemporalEntity.getString()));
	}
	
	/**
	 * Returns a Jena resource for the given ontology element.
	 * @param en the enumerated type describing the desired ontology 
	 * element
	 * @return a Jena resource that represents the desired ontology 
	 * element
	 */
	protected Resource getResource(FriendTracker en)
	{
		return _resourceMap.get(en.getString());
	}

	/**
	 * Returns a Jena resource for the given ontology element.
	 * @param en the enumerated type describing the desired ontology 
	 * element
	 * @return a Jena resource that represents the desired ontology 
	 * element
	 */
	protected Resource getResource(Time en)
	{
		return _resourceMap.get(en.getString());
	}
	
	/**
	 * Returns a Jena property for the given ontology element.
	 * @param en the enumerated type describing the desired ontology 
	 * element
	 * @return a Jena property that represents the desired ontology 
	 * element
	 */
	protected Property getProperty(FriendTracker en)
	{
		return (Property) _resourceMap.get(en.getString());
	}
	
	/**
	 * Returns a Jena property for the given ontology element.
	 * @param en the enumerated type describing the desired ontology 
	 * element
	 * @return a Jena property that represents the desired ontology 
	 * element
	 */
	protected Property getProperty(Time en)
	{
		return (Property) _resourceMap.get(en.getString());
	}
	
	/**
	 * Returns a Jena property for the given ontology element.
	 * @param en the enumerated type describing the desired ontology 
	 * element
	 * @return a Jena property that represents the desired ontology 
	 * element
	 */
	protected Property getProperty(Geo en)
	{
		return (Property) _resourceMap.get(en.getString());
	}
	
	/**
	 * Combs through the RDF data in the model that backs 
	 * this JenaSource, and creates a list of Event objects 
	 * to represent the events described in the model. 
	 * @return the events described in the JenaSource's underlying model
	 */
	protected List<Event> extractEvents()
	{
		List<Event> toReturn = new ArrayList<Event>();
		
		ResIterator iterator;
		
		iterator = _model.listSubjectsWithProperty(
				RDF.type, 
				getResource(FriendTracker.Event));
		try
		{
			while(iterator.hasNext())
			{
				Resource eventResource = iterator.nextResource();
				toReturn.add(extractEvent(eventResource));
			}
		}
		finally
		{
			iterator.close();
		}
		
		return toReturn;
	}
	
	private Event extractEvent(Resource event)
	{
		EventImpl toReturn;
		
		String id;
		String description;
		Date startTime;
		String title;
		Venue venue;
		RDFNode node;

		id = event.getURI();
		
		title = stripType(extractValueFromNode(
				getFirstNode(
						_model.listObjectsOfProperty(
								event,
								getProperty(FriendTracker.isNamed))))); 
		
		description = stripType(extractValueFromNode(
				getFirstNode(
						_model.listObjectsOfProperty(
								event,
								getProperty(FriendTracker.hasDescription)))));
		
		node = getFirstNode(
				_model.listObjectsOfProperty(
						event,
						getProperty(FriendTracker.occursAt)));
		
		startTime = parseDate(extractValueFromNode(
				getFirstNode(
						_model.listObjectsOfProperty(
								(Resource) node.as(Resource.class),
								getProperty(Time.inXSDDateTime)))));
		
		
		venue = extractVenue((Resource) getFirstNode(
				_model.listObjectsOfProperty(
						event, 
						getProperty(FriendTracker.hasVenue))));
		
		toReturn = new EventImpl(id, title, venue, startTime, description);
		
		return toReturn;
	}
	
	protected String stripType(String typedLiteralString)
	{
		int index;
		String toReturn = typedLiteralString;
		
		if(null != typedLiteralString)
		{
			index = typedLiteralString.lastIndexOf("^^");
			if(-1 != index)
			{
				toReturn = typedLiteralString.substring(0, index);
			}
		}
		
		return toReturn;
	}
	
	private Venue extractVenue(Resource venue)
	{
		VenueImpl toReturn;
		
		String id;
		String name;
		String latString;
		String lonString;
		double latitude;
		double longitude;


		id = venue.getURI();
		
		name = stripType(extractValueFromNode(
					getFirstNode(
						_model.listObjectsOfProperty(
								venue, 
								getProperty(
										FriendTracker.isNamed)))));
		
		latString = extractValueFromNode(
					getFirstNode(
						_model.listObjectsOfProperty(
								venue, 
								getProperty(
										Geo.latitude))));
		
		lonString = extractValueFromNode(
				getFirstNode(
						_model.listObjectsOfProperty(
								venue, 
								getProperty(
										Geo.longitude)))); 
			
		latitude = Double.parseDouble(stripType(latString));

		longitude = Double.parseDouble(stripType(lonString));
		
		toReturn = new VenueImpl(id, name, latitude, longitude);
		
		return toReturn;
	}
	
	/**
	 * Combs through the RDF data in the model that backs 
	 * this JenaSource, and creates a list of Post objects 
	 * to represent the posts described in the model. 
	 * @return the posts described in the JenaSource's underlying model
	 */
	protected List<Post> extractPosts()
	{
		List<Post> toReturn = new ArrayList<Post>();
		final String queryString = 
			"PREFIX ft: <" + FriendTracker.Base.getString() + "> \n" +
			"PREFIX time: <" + Time.Base.getString() + "> \n" +
			"SELECT ?post ?title ?content ?date \n" +
			"WHERE { ?post a ft:Post ; ft:hasTitle ?title ; ft:hasContent ?content ; ft:occursAt [ time:inXSDDateTime ?date ]}";
		
		Query query; 
		QueryExecution queryExecution;

		ResultSet rs;
		
		query = QueryFactory.create(queryString);
		queryExecution = QueryExecutionFactory.create(query, _model);
		
		rs = queryExecution.execSelect();
		
		while(rs.hasNext()){
			QuerySolution solution = rs.nextSolution();
			RDFNode temp;
			PostImpl post = new PostImpl();
			
			temp = solution.get("post");
			post.setID(temp.asNode().getURI());

			temp = solution.get("title");
			post.setTitle(extractValueFromNode(temp));
			
			temp = solution.get("content");
			post.setContent(extractValueFromNode(temp));
			
			temp = solution.get("date");
			post.setPostDate(parseDate(extractValueFromNode(temp)));
			
			toReturn.add(post);
		}

		return toReturn;	
	}
	
	protected RDFNode getFirstNode(NodeIterator iterator)
	{
		RDFNode toReturn = null;
		try
		{
		if(iterator.hasNext())
		{
			toReturn = iterator.nextNode();
		}
		}
		finally
		{
			iterator.close();
		}
		return toReturn;
	}
	
	protected String extractValueFromNode(RDFNode node)
	{
		String toReturn = null;
		if(null != node)
		{
			if(node.isLiteral())
			{
				toReturn = node.asNode().getLiteral().toString();
			}
		}
		
		return toReturn;
	}
	
	private Date parseDate(String xsdDate)
	{
		int year = 0;
		int month = 0;
		int day = 0;
		int hour = 0;
		int minute = 0;
		int second = 0;

		Calendar calendar = Calendar.getInstance();
		Date toReturn;
		if(null != xsdDate)
		{
			year = Integer.parseInt(xsdDate.substring(0, 4));
			month = Integer.parseInt(xsdDate.substring(5, 7));
			day = Integer.parseInt(xsdDate.substring(8, 10));
			if(xsdDate.length() > 10)
			{
				hour = Integer.parseInt(xsdDate.substring(11, 13));
				minute = Integer.parseInt(xsdDate.substring(14, 16));
				second = Integer.parseInt(xsdDate.substring(17, 19));
			}
//			System.out.println(
//					String.format(
//							"Parse date: %1$s %2$s %3$s %4$s %5$s %6$s",
//							  year, month, day, hour, minute, second));
			// months are 0-indexed...
			calendar.set(year, month - 1, day, hour, minute, second);
		}
		toReturn = calendar.getTime();

		return toReturn;
	}

	/**
	 * Parses a Java Date object out of the given RDFNode, if possible. 
	 * @return the date represented by the given node, or null if no date is 
	 * present
	 */
	protected Date extractDate(RDFNode rdfNode)
	{
		Date toReturn = null;
		RDFNode dateNode;
		Object temp;
		
		if(null != rdfNode)
		{
			Resource r = _model.getResource(rdfNode.asNode().getBlankNodeLabel());
			NodeIterator ni = _model.listObjectsOfProperty(r, getProperty(Time.inXSDDateTime));
			while(ni.hasNext())
			{
				dateNode = ni.nextNode();
				temp = dateNode.asNode().getLiteralValue();
			}
		}
		
		return toReturn;
	}
	

	/**
	 * Combs through the RDF data in the model that backs 
	 * this JenaSource, and creates a list of Friend objects 
	 * to represent the friends described in the model. 
	 * @return the friends described in the JenaSource's underlying model
	 */
	protected List<Friend> extractFriends()
	{
		List<Friend> toReturn = new ArrayList<Friend>();
		
		ResIterator iterator;
		
		iterator = _model.listSubjectsWithProperty(
				RDF.type, 
				getResource(FriendTracker.Friend));
		try
		{
			while(iterator.hasNext())
			{
				Resource friendResource = iterator.nextResource();
				toReturn.add(extractFriend(friendResource));
			}
		}
		finally
		{
			iterator.close();
		}
		
		return toReturn;	
	}
	private List<String> extractListOfLiterals(NodeIterator iterator)
	{
		List<String> toReturn = new ArrayList<String>();
		String value;
		Node node;
		if(null != iterator)
		{
			while(iterator.hasNext())
			{
				node = iterator.nextNode().asNode();
				value = node.getLiteralValue().toString();
				toReturn.add(value);
			}
		}
		
		return toReturn;
	}
	private Friend extractFriend(Resource friend)
	{
		FriendImpl toReturn = new FriendImpl();
		
		String origin;
		String pictureUrl;
		String statusString;
		
		RDFNode statusNode;
		
		OnlineStatus status; 
		
		origin = extractValueFromNode(getFirstNode(_model.listObjectsOfProperty(friend, getProperty(FriendTracker.isFrom))));
		pictureUrl = extractValueFromNode(getFirstNode(_model.listObjectsOfProperty(friend, getProperty(FriendTracker.hasPic))));
		statusNode = (getFirstNode(_model.listObjectsOfProperty(friend, getProperty(FriendTracker.hasStatus))));
		
		status = parseStatusString(statusNode);
		
		toReturn.setOrigin(origin);
		toReturn.setPictureURL(pictureUrl);
		toReturn.setStatus(status);
		
		for(String email : extractListOfLiterals(
				_model.listObjectsOfProperty(friend, 
						getProperty(FriendTracker.hasEmailAddress))))
		{
			toReturn.addEmail(email);
		}
		
		for(String handle : extractListOfLiterals(
				_model.listObjectsOfProperty(friend, 
						getProperty(FriendTracker.hasHandle))))
		{
			toReturn.addName(handle);
		}
		
		for(String name : extractListOfLiterals(
				_model.listObjectsOfProperty(friend, 
						getProperty(FriendTracker.isNamed))))
		{
			toReturn.addName(name);
		}

		
		return toReturn;
	}
	private OnlineStatus parseStatusString(RDFNode statusNode) {
		OnlineStatus toReturn = OnlineStatus.Unknown;
		
		String statusString = null;
		
		if(null != statusNode)
		{
			statusString = statusNode.toString();
		}
		
		if(FriendTracker.AwayStatus.getString().equals(statusString))
		{
			toReturn = OnlineStatus.Away;
		}
		else if(FriendTracker.BusyStatus.getString().equals(statusString))
		{
			toReturn = OnlineStatus.Busy;
		}
		else if(FriendTracker.AvailableStatus.getString().equals(statusString))
		{
			toReturn = OnlineStatus.Available;
		}
		else if(FriendTracker.OfflineStatus.getString().equals(statusString))
		{
			toReturn = OnlineStatus.Offline;
		}

		return toReturn;
	}	
}
