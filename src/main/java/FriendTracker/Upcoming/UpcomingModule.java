package FriendTracker.Upcoming;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import net.semwebprogramming.upcoming.parser.EventBuilder;
import net.semwebprogramming.upcoming.parser.VEvent;

/**
 * Module for accessing the Upcoming REST API
 */
public class UpcomingModule
{
   public static final String LOCATION_QUERY_FORMAT_STRING = 
      "http://upcoming.yahooapis.com/services/rest/?api_key=%1$s&method=event.search&location=%2$s";

   /**
    * Issue a query to Upcoming and parse the results into
    * Java objects
    * @param yahooDeveloperKey The Developer key from Yahoo
    * @param location The location string to query for
    * @return
    */
   public List<VEvent> queryByLocation(String yahooDeveloperKey, String location)
      throws UpcomingException
   {
      List<VEvent> events = null;
      
      String queryUrlString = String.format(LOCATION_QUERY_FORMAT_STRING,
         yahooDeveloperKey, location.replace(" ", "+"));
      
      EventBuilder builder = new EventBuilder();
      try
      {
         URL queryURL = new URL(queryUrlString);
         events = builder.buildEvents(queryURL.openStream());
      }
      catch (MalformedURLException e)
      {
         throw new UpcomingException(e);
      }
      catch (IOException e)
      {
         throw new UpcomingException(e);
      }
      catch (SAXException e)
      {
         throw new UpcomingException(e);
      }
      catch (ParserConfigurationException e)
      {
         throw new UpcomingException(e);
      }

      return events;
   }

}
