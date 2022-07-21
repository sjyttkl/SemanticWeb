package FriendTracker.Upcoming.parser;

public enum Attributes {
	
	Id("id"),
	Name("name"),
	Description("description"),
	StartDate("start_date"),
	EndDate("end_date"),
	StartTime("start_time"),
	EndTime("end_time"),
	MetroId("metro_id"),
	VenueId("venue_id"),
	UserId("user_id"),
	CategoryId("category_id"),
	DatePosted("date_posted"),
	Latitude("latitude"),
	Longitude("longitude"),
	GeocodingPrecision("geocoding_precision"),
	GeocodingAmbiguous("geocoding_ambiguous"),
	VenueName("venue_name"),
	VenueAddress("venue_address"),
	VenueCity("venue_city"),
	VenueStateName("venue_state_name"),
	VenueStateCode("venue_state_code"),
	VenueStateId("venue_state_id"),
	VenueCountryName("venue_country_name"),
	VenueCountryCode("venue_country_code"),
	VenueCountryId("venue_country_id"),
	VenueZip("venue_zip"),
	TicketUrl("ticket_url"),
	TicketFree("ticket_free"),
	TicketPrice("ticket_price"),
	PhotoUrl("photo_url"),
	
	Stat("stat");
	
	private final String _string;
	
	public static String getString(Attributes fName)
	{
		return fName._string;
	}
	
	private Attributes(String str)
	{
		_string = str;
	}
}
