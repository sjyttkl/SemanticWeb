package FriendTracker.Friendtracker.friendtracker;

/**
 *  A class that makes available all of the information needed to 
 *  configure the various data sources used by FriendTracker. 
 *
 */
public class FriendTrackerConfiguration {

	/**
	 *  The information needed to configure the Upcoming.org
	 *  module.
	 */
	public class UpcomingConfiguration
	{
		/**
		 * The name of the attribue in the upcoming XML file that contains 
		 * information about the Yahoo Developer Key.
		 */
		public static final String YAHOO_DEVELOPER_KEY_ATTRIBUTE = "yahooDeveloperKey";
		/**
		 * The name of the attribue in the upcoming XML file that contains 
		 * information about the location of the mapping ontology.
		 */
		public static final String MAPPING_ONTOLOGY_LOCATION_ATTRIBUTE = "mappingLocation";
		
		/**
		 * The name of the attribue in the upcoming XML file that contains 
		 * information about the base ontology.
		 */
		public static final String BASE_ONT_URI = "ontUri";
		
		private final String _yahooDeveloperKey;
		private final String _mappingLocation;
		private final String _baseOntologyUri;
		private final String _friendtrackerOntologyLocation;
		
		public UpcomingConfiguration(String yahooDeveloperKey, String mappingLocation, String baseOntologyUri, String friendtrackerOntologyLocation)
		{
			_yahooDeveloperKey = yahooDeveloperKey;
			_mappingLocation = mappingLocation;
			_baseOntologyUri = baseOntologyUri;
			_friendtrackerOntologyLocation = friendtrackerOntologyLocation;
		}

		/**
		 * 
		 * @return the yahoo developer key for upcoming.
		 */
		public String getYahooDeveloperKey() {
			return _yahooDeveloperKey;
		}
		/**
		 * 
		 * @return the location of the mapping ontology that FriendTracker 
		 * will use to convert upcoming RDF into FriendTracker RDF
		 */
		public String getMappingLocation()
		{
			return _mappingLocation;
		}
		/**
		 * 
		 * @return the location on disk of the FriendTracker ontology
		 */
		public String getFriendtrackerOntologyLocation()
		{
			return _friendtrackerOntologyLocation;
		}
		/**
		 * 
		 * @return the base URI of the upcoming ontology.
		 */
		public String getBaseOntologyUri()
		{
			return _baseOntologyUri;
		}
	}

	public class JabberConfiguration
	{
		public static final String JABBER_SERVER_ATTRIBUTE = "server";
		public static final String JABBER_LOGIN_ATTRIBUTE = "login";
		public static final String JABBER_PASSWORD_ATTRIBUTE = "password";
		
		
		public static final String MAPPING_ONTOLOGY_LOCATION_ATTRIBUTE = "mappingLocation";
		public static final String BASE_ONT_URI = "ontUri";
		
		private final String _server;
		private final String _login;
		private final String _password;

		private final String _baseOntologyUri;
		private final String _ontologyLocation;
		private final String _jabberMappingLocation;
		
		public JabberConfiguration(String server, String login, String password, String baseOntologyUri, String friendTrackerOntologyLocation, String jabberMappingLocation)
		{
			_server = server;
			_login = login;
			_password = password;
			
			_baseOntologyUri = baseOntologyUri;
			_ontologyLocation = friendTrackerOntologyLocation;
			_jabberMappingLocation = jabberMappingLocation;
		}

		/**
		 * @return the URI of the Jabber ontology.
		 */
		public String getBaseOntologyUri()
		{
			return _baseOntologyUri;
		}

		/**
		 * 
		 * @return the location on disk of the FriendTracker ontology
		 */
		public String getFriendTrackerOntologyLocation()
		{
			return _ontologyLocation;
		}
		
		/**
		 * 
		 * @return the location on disk of the Jabber mapping ontology
		 */
		public String getJabberMappingLocation()
		{
			return _jabberMappingLocation;
		}

		/**
		 * 
		 * @return the login needed for the Jabber account
		 */
		public String getLogin() {
			return _login;
		}

		/**
		 * 
		 * @return the password required for the Jabber account
		 */
		public String getPassword() {
			return _password;
		}

		/**
		 * 
		 * @return the URL of the Jabber server
		 */
		public String getServer() {
			return _server;
		}
	}
	
	public class FacebookConfiguration
	{
		public static final String FACEBOOK_FIELDS_ATTRIBUTE = "fields";
		public static final String FACEBOOK_BROWSER_LOCATION_ATTRIBUTE = "browserLocation";
		public static final String FACEBOOK_SECRET_ATTRIBUTE = "secret";
		public static final String FACEBOOK_API_KEY_ATTRIBUTE = "apiKey";
		public static final String FACEBOOK_WEB_SERVICE_URL_ATTRIBUTE = "wsUrl";
		public static final String FACEBOOK_LOGIN_URL_ATTRIBUTE = "loginUrl";
		public static final String FACEBOOK_XSL_LOCATION = "xslLocation";
		
		public static final String FACEBOOK_MAPPING_LOCATION = "mappingLocation";
		public static final String FACEBOOK_ONTOLOGY_URI= "ontUri";
		
		
		private final String _fields; 
		private final String _browserLocation;
		private final String _secret;
		private final String _apiKey;
		private final String _webServiceUrl;
		private final String _loginUrl;
		private final String _xslLocation;
		private final String _mappingLocation;
		private final String _facebookOntUri;
		private final String _friendTrackerOntLocation;
		
		public FacebookConfiguration(String fields, String browserLocation, String secret, String apiKey, String webServiceUrl, String loginUrl, String xslLocation, String mappingLocation, String facebookOntUri, String friendTrackerOntLocation)
		{
			_fields = fields;
			_browserLocation = browserLocation;
			_secret = secret;
			_apiKey = apiKey;
			_webServiceUrl = webServiceUrl;
			_loginUrl = loginUrl;
			_xslLocation = xslLocation;
			_mappingLocation = mappingLocation;
			_facebookOntUri = facebookOntUri;
			_friendTrackerOntLocation = friendTrackerOntLocation;
		}

		/**
		 * @return  the Facebook API key
		 */
		public String getApiKey() {
			return _apiKey;
		}
		/**
		 * 
		 * @return the path to the executable of a web browser for 
		 * Facebook authentication  
		 */
		public String getBrowserLocation() {
			return _browserLocation;
		}
		/**
		 * 
		 * @return a string representing the Facebook fields to request.
		 */
		public String getFields() {
			return _fields;
		}
		/**
		 * 
		 * @return the URL to use to log into the Facebook application
		 */
		public String getLoginUrl() {
			return _loginUrl;
		}
		
		/**
		 * @return Used by Facebook authentication
		 */
		public String getSecret() {
			return _secret;
		}
		/**
		 * 
		 * @return the URL of the Facebook web service
		 */
		public String getWebServiceUrl() {
			return _webServiceUrl;
		}
		/**
		 * 
		 * @return the location, on disk, of the mapping file
		 * between Facebook and FriendTracker
		 */
		public String getMappingLocation() {
			return _mappingLocation;
		}
		/**
		 * @return the location, on disk, of the XSL to transform 
		 * Facebook XML into Facebook RDF/XML 
		 */
		public String getXSLLocation()
		{
			return _xslLocation;
		}

		/**
		 * 
		 * @return the location on disk of the FriendTracker ontology
		 */
		public String getFriendTrackerOntologyLocation() {
			return _friendTrackerOntLocation;
		}
	}
	
	/**
	 *  The object that describes the WordPress configuration. 
	 *
	 */
	public class WordPressConfiguration
	{
		public static final String WORDPRESS_MAPPING_LOCATION = "wordPressMapping";
		public static final String WORDPRESS_MAPPING_XSL = "mappingXsl";
		public static final String WORDPRESS_QUERY_LOCATION = "queryLocation";
		public static final String WORDPRESS_QUERY_NAME = "queryName";
		
		private String _mappingLocation;
		private String _xslLocation;
		private String _queryLocation;
		private String _queryName;
		
		public WordPressConfiguration(String wordPressMappingLocation, String wordPressXsl, String wordPressQueryLocation, String wordPressQueryName)
		{
			_mappingLocation = wordPressMappingLocation;
			_xslLocation = wordPressXsl;
			_queryLocation = wordPressQueryLocation;
			_queryName = wordPressQueryName;
		}

		/**
		 * @return the location, on disk, of the mapping file between 
		 * WordPress and FriendTracker
		 */
		public String getMappingLocation() {
			return _mappingLocation;
		}

		/**
		 * 
		 * @param location the location of the mapping file between WordPress  
		 * and FriendTracker
		 */
		public void setMappingLocation(String location) {
			_mappingLocation = location;
		}

		/**
		 * 
		 * @return the location on disk of the file that containst the 
		 * query to issue against the WordPress service
		 */
		public String getQueryLocation() {
			return _queryLocation;
		}

		/**
		 * 
		 * @param location the location on disk of the file that containst the 
		 * query to issue against the WordPress service
		 */
		public void setQueryLocation(String location) {
			_queryLocation = location;
		}

		/**
		 * 
		 * @return the name of the query
		 */
		public String getQueryName() {
			return _queryName;
		}

		/**
		 * 
		 * @param name the name of the query
		 */
		public void setQueryName(String name) {
			_queryName = name;
		}

		/** 
		 * 
		 * @return the location of the XSL to translate WordPress 
		 * RDF/XML into FriendTracker RDF/XML
		 */
		public String getXslLocation() {
			return _xslLocation;
		}

		public void setXslLocation(String location) {
			_xslLocation = location;
		}
	}
	
	private UpcomingConfiguration _upcomingConfiguration;
	private JabberConfiguration _jabberConfiguration;
	private FacebookConfiguration _facebookConfiguration;
	private WordPressConfiguration _wordPressConfiguration;
	
	public FriendTrackerConfiguration()
	{ }
	
	public FriendTrackerConfiguration(
			UpcomingConfiguration upcoming, 
			JabberConfiguration jabber,
			FacebookConfiguration facebook,
			WordPressConfiguration wordpress)
	{
		_upcomingConfiguration = upcoming;
		_jabberConfiguration = jabber;
		_facebookConfiguration = facebook;
		_wordPressConfiguration = wordpress;
	}

	/**
	 * 
	 * @return the object which contains the Facebook configuration
	 */
	public FacebookConfiguration getFacebookConfiguration() {
		return _facebookConfiguration;
	}

	/**
	 * 
	 * @param configuration sets the object which contains the Facebook configuration 
	 */
	public void setFacebookConfiguration(FacebookConfiguration configuration) {
		_facebookConfiguration = configuration;
	}

	/**
	 * 
	 * @return the object that contains the WordPress configuration
	 */
	public WordPressConfiguration getWordPressConfiguration() { 
		return _wordPressConfiguration;
	}
	
	/**
	 * 
	 * @param configuration the object that contains the WordPress configuration
	 */
	public void setWordPressConfiguration(WordPressConfiguration configuration) {
		_wordPressConfiguration = configuration;
	}

	
	/**
	 * 
	 * @return the object that contains the Jabber configuration
	 */
	public JabberConfiguration getJabberConfiguration() {
		return _jabberConfiguration;
	}

	/**
	 * 
	 * @param configuration the Jabber configuration
	 */
	public void setJabberConfiguration(JabberConfiguration configuration) {
		_jabberConfiguration = configuration;
	}

	/**
	 * 
	 * @return the object that describes the Upcoming.org configuration
	 */
	public UpcomingConfiguration getUpcomingConfiguration() {
		return _upcomingConfiguration;
	}
	
	/**
	 * 
	 * @param configuration the Upcoming.org configuration
	 */
	public void setUpcomingConfiguration(UpcomingConfiguration configuration) {
		_upcomingConfiguration = configuration;
	}

}
