package FriendTracker.Friendtracker.friendtracker.data.implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import net.semwebprogramming.chapter9.JavaObjectRdfSerializer;
import net.semwebprogramming.friendtracker.FriendTrackerConfiguration.UpcomingConfiguration;
import net.semwebprogramming.friendtracker.data.EventSource;
import net.semwebprogramming.friendtracker.data.implementation.vocabulary.FriendTracker;
import net.semwebprogramming.friendtracker.data.implementation.vocabulary.Geo;
import net.semwebprogramming.friendtracker.data.implementation.vocabulary.Time;
import net.semwebprogramming.friendtracker.model.Event;
import net.semwebprogramming.friendtracker.utilities.ProperException;
import net.semwebprogramming.upcoming.UpcomingModule;
import net.semwebprogramming.upcoming.parser.VEvent;
import net.semwebprogramming.upcoming.vocabulary.Upcoming;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * An implementation of the EventSource interface that provides 
 * information about friends from Upcoming.org.
 */
public class UpcomingEventSource extends JenaSource implements EventSource {
	private UpcomingConfiguration _config;

	private UpcomingModule _upcomingModule;

	public UpcomingEventSource() {
		_upcomingModule = new UpcomingModule();
	}

	@Override
	protected void createModel() {
		_model = ModelFactory
				.createOntologyModel(PelletReasonerFactory.THE_SPEC);
	}

	@Override
	public void initialize(Object configurationObject) {
		if (configurationObject instanceof UpcomingConfiguration) {
			_config = (UpcomingConfiguration) configurationObject;
		} else {
			throw new ProperException(
					new IllegalArgumentException(
							"UpcomingEventSource must be configured with a instance of UpcomingConfiguration."));
		}

//		String mappingLocation = _config.getMappingLocation();
//		String friendtrackerOntologyLocation = _config.getFriendtrackerOntologyLocation();
//		
//		FileInputStream mappingStream = null;
//		FileInputStream friendtrackerOntologyStream = null;
//		
//		try {
//			mappingStream = new FileInputStream(mappingLocation);
//			friendtrackerOntologyStream = new FileInputStream(friendtrackerOntologyLocation);
//			// bring in the mapping stuff - OWL + rules...
//			_model.read(friendtrackerOntologyStream, "http://semwebprogramming.org:8099/ont/friendtracker-ont#", "TURTLE");
//			_model.read(mappingStream, "", "TURTLE");
//			
//			_model.prepare();
//		} catch (FileNotFoundException e) {
//			throw new ProperException(e);
//		}
		
	}

	public List<Event> getEvents(String location) {

		List<Event> toReturn;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteArrayInputStream bais;
		JavaObjectRdfSerializer serializer;
		List<VEvent> upcomingEvents;
		ArrayList<Object> toSerialize;

		upcomingEvents = _upcomingModule.queryByLocation(_config
				.getYahooDeveloperKey(), location);
		serializer = new JavaObjectRdfSerializer("", _config
				.getBaseOntologyUri(), "TURTLE",
				"net.semwebprogramming.upcoming");

		toSerialize = new ArrayList<Object>(upcomingEvents.size());
		toSerialize.addAll(upcomingEvents);
		serializer.serialize(toSerialize, baos);

		bais = new ByteArrayInputStream(baos.toByteArray());

		_model.read(bais, "", "TURTLE");

		
		performConversionFromUpcomingOntToFriendTrackerOnt();
		
		toReturn = extractEvents();

		return toReturn;
	}

	private void performConversionFromUpcomingOntToFriendTrackerOnt() {
		ResIterator upcomingEvents;
		
		upcomingEvents = _model.listSubjectsWithProperty(
				RDF.type, 
				_model.createResource(Upcoming.getString(Upcoming.Base) + "VEvent"));
		try
		{
		while(upcomingEvents.hasNext())
		{
			convertOneEvent(upcomingEvents.nextResource());
		}
		}
		finally
		{
			upcomingEvents.close();
		}
		
	}

	private void convertOneEvent(Resource upcomingEvent) 
	{
		String venueId;
		String name;
		String description;
		String startTime;
		
		String tempTimeString;
		
		Resource startInstant;
		
		Individual venue;
		
		
		venueId = extractValueFromNode(
				getFirstNode(
						_model.listObjectsOfProperty(
								upcomingEvent, 
								_model.createProperty(
										Upcoming.getString(Upcoming.Base) + "VenueId"))));
		venue = _model.createIndividual(_model.createResource("#venue" + venueId));
		convertOneVenue(upcomingEvent, venue);

		name = extractValueFromNode(
				getFirstNode(
						_model.listObjectsOfProperty(
								upcomingEvent, 
								_model.createProperty(
										Upcoming.getString(Upcoming.Base) + "Name"))));
 
		description = extractValueFromNode(
				getFirstNode(
						_model.listObjectsOfProperty(
								upcomingEvent, 
								_model.createProperty(
										Upcoming.getString(Upcoming.Base) + "Description"))));
		tempTimeString = extractValueFromNode(
				getFirstNode(
						_model.listObjectsOfProperty(
								upcomingEvent, 
								_model.createProperty(
										Upcoming.getString(Upcoming.Base) + "StartDate"))));

		startTime = tempTimeString.substring(0, 10);
		
		tempTimeString = extractValueFromNode(
				getFirstNode(
						_model.listObjectsOfProperty(
								upcomingEvent, 
								_model.createProperty(
										Upcoming.getString(Upcoming.Base) + "StartTime"))));
		if(null != tempTimeString)
		{
			startTime += " " + tempTimeString.substring(0, 8);
		}
 
		startInstant = _model.createResource(new AnonId());
		
		_model.add(startInstant, getProperty(Time.inXSDDateTime), startTime);
		_model.add(upcomingEvent, getProperty(FriendTracker.occursAt), startInstant);
		_model.add(upcomingEvent, RDF.type, getResource(FriendTracker.Event));
		_model.add(upcomingEvent, getProperty(FriendTracker.isNamed), name);
		if(null != description)
		{
			_model.add(upcomingEvent, getProperty(FriendTracker.hasDescription), description);
		}
	}

	private void convertOneVenue(Resource upcomingEvent, Individual venue) {
		String venueName;
		String venueLat;
		String venueLong;
		
		venueName = extractValueFromNode(
				getFirstNode(
						_model.listObjectsOfProperty(
								upcomingEvent, 
								_model.createProperty(
										Upcoming.getString(Upcoming.Base) + "VenueName"))));

		venueLat = extractValueFromNode(
				getFirstNode(
						_model.listObjectsOfProperty(
								upcomingEvent, 
								_model.createProperty(
										Upcoming.getString(Upcoming.Base) + "Latitude"))));
		venueLong = extractValueFromNode(
				getFirstNode(
						_model.listObjectsOfProperty(
								upcomingEvent, 
								_model.createProperty(
										Upcoming.getString(Upcoming.Base) + "Longitude"))));

		if(null != venueName)
		{
			_model.add(venue, getProperty(FriendTracker.isNamed), venueName);
		}
		if(null != venueLat)
		{
			_model.add(venue, getProperty(Geo.latitude), venueLat);
		}
		if(null != venueLong)
		{
			_model.add(venue, getProperty(Geo.longitude), venueLong);
		}

		_model.add(upcomingEvent, getProperty(FriendTracker.hasVenue), venue);
	}

	private StringBuilder listResources(ExtendedIterator iter, String label)
	{
		int count = 0;
		StringBuilder toReturn = new StringBuilder();
		try
		{
			while(iter.hasNext())
			{
				toReturn.append("\t").append(((Resource) iter.next()).getURI()).append("\n");
				count++;
			}
		}
		finally
		{
			iter.close();
		}
		toReturn.insert(0, label + ": (" + count + ")\n");
		return toReturn;
	}
	
	private void debug() {

		StringBuilder eventsString = new StringBuilder();
		int eventsCount = 0;
		
//		String cls = "http://semwebprogramming.org:8099/ont/friendtracker/upcoming-ont#VEvent";
		String cls = FriendTracker.Venue.getString();
		String venueProp = "http://semwebprogramming.org:8099/ont/friendtracker-upcoming-mapping#tripped";
		
		ExtendedIterator iter = _model.listIndividuals(_model.createClass(cls));
		
		System.out.println(listResources(iter, cls));
		
		iter = _model.listSubjectsWithProperty(_model.createProperty(venueProp));
		System.out.println(listResources(iter, venueProp));
		
		try {
			_model.write(new FileOutputStream("debug.txt"), "TURTLE");
		} catch (FileNotFoundException e) {
			throw new ProperException(e);
		} 
	}
}
