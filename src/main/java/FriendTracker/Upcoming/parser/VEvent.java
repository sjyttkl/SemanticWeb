package FriendTracker.Upcoming.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class VEvent
{
   private final HashMap<Attributes, Set<String>> _values =
      new HashMap<Attributes, Set<String>>();
   
   public String getId()
   {   return getValue(Attributes.Id);   }
   
   public String getName()
   {   return getValue(Attributes.Name);   }
   public String getDescription()
   {   return getValue(Attributes.Description);   }
   
   public Object getStartDate()
   {
      return getCalendarValue(Attributes.StartDate);
   }
   public Object getEndDate()
   {   
      return getCalendarValue(Attributes.EndDate);   
   }
   public String getStartTime()
   {   return getValue(Attributes.StartTime);   }
   public String getEndTime()
   {   return getValue(Attributes.EndTime);   }
   
   public String getMetroId()
   {   return getValue(Attributes.MetroId);   }
   public String getVenueId()
   {   return getValue(Attributes.VenueId);   }
   public String getUserId()
   {   return getValue(Attributes.UserId);   }
   public String getCategoryId()
   {   return getValue(Attributes.CategoryId);   }
   
   public String getDatePosted()
   {   return getValue(Attributes.DatePosted);   }
   
   public Object getLatitude()
   {   return getDoubleValue(Attributes.Latitude);   }
   public Object getLongitude()
   {   return getDoubleValue(Attributes.Longitude);   }
   
   public String getGeocodingPrecision()
   {   return getValue(Attributes.GeocodingPrecision);   }
   public Object getGeocodingAmbiguous()
   {   return getBooleanValue(Attributes.GeocodingAmbiguous);   }
   
   public String getVenueName()
   {   return getValue(Attributes.VenueName);   }
   public String getVenueAddress()
   {   return getValue(Attributes.VenueAddress);   }
   public String getVenueCity()
   {   return getValue(Attributes.VenueCity);   }
   
   public String getVenueStateName()
   {   return getValue(Attributes.VenueStateName);   }
   public String getVenueStateCode()
   {   return getValue(Attributes.VenueStateCode);   }
   public String getVenueStateId()
   {   return getValue(Attributes.VenueStateId);   }
   
   public String getVenueCountryName()
   {   return getValue(Attributes.VenueCountryName);   }
   public String getVenueCountryCode()
   {   return getValue(Attributes.VenueCountryCode);   }
   public String getVenueCountryId()
   {   return getValue(Attributes.VenueCountryId);   }
   
   public String getVenueZip()
   {   return getValue(Attributes.VenueZip);   }
   
   public String getTicketUrl()
   {   return getValue(Attributes.TicketUrl);   }
   
   public String getTicketFree()
   {   return getValue(Attributes.TicketFree);   }
   public Object getTicketPrice()
   {   return getDoubleValue(Attributes.TicketPrice);   }
   
   public String getPhotoUrl()
   {   return getValue(Attributes.PhotoUrl);   }
   public String getStat()
   {   return getValue(Attributes.Stat);   }
   
   private Object getBooleanValue(Attributes att)
   {
      Object toReturn = null;

      String val = getValue(att).trim();
      if(null != val)
      {
         toReturn = new Boolean(
            !("false".equalsIgnoreCase(val) || "0".equals(val)) );
      }
      
      return toReturn;
   }
   
   private Object getDoubleValue(Attributes att)
   {
      Object toReturn = null;

      String val = getValue(att);
      if(null != val)
      {
         try
         {
            Double d = Double.parseDouble(val);
            toReturn = d;
         }
         catch (NumberFormatException e)
         {
            toReturn = val;
         }
      }
      
      return toReturn;
   }
   
   private Object getCalendarValue(Attributes att)
   {
      Object toReturn = null;

      String val = getValue(att);
      if(null != val)
      {
         DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         try
         {
            Date d = formatter.parse(val);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            toReturn = c;
         }
         catch (ParseException e)
         {
            toReturn = val;
         }
      }
      
      return toReturn;
   }
   
   private String getValue(Attributes att)
   {
      String toReturn = null;
      
      Set<String> vals = null;
      if(_values.containsKey(att))
      {
         vals = _values.get(att);
      }
      
      if(null != vals && vals.size() > 0)
      {
         StringBuilder sb = new StringBuilder();
         for(String str : vals)
         {
            sb.append(str).append("\t");
         }
         toReturn = sb.toString();
         toReturn = toReturn.trim();
      }
      
      return toReturn;
   }
   
   /**
    * Add the property to the Event
    * @param att
    * @param value
    */
   public void setProperty(Attributes att, String value)
   {
      Set<String> set;
      if(null != value)
      {
         if(_values.containsKey(att))
         {
            set = _values.get(att);
         }
         else
         {
            set = new HashSet<String>(1);
            _values.put(att, set);
         }
   
         set.add(value);
      }
   }

}
