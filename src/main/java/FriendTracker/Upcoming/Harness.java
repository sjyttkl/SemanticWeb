package FriendTracker.Upcoming;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import net.semwebprogramming.chapter9.JavaObjectRdfSerializer;

public class Harness
{
   public static final String YAHOO_DEVELOPER_KEY = "425cffbeba";

   public static void main(String[] args)
   {
      if(args.length != 3)
      {
         System.err.println("Usage: java Harness "
            + "[\"location string\"] [output file] [output file format]");
         return;
      }
      
      UpcomingModule upcoming = new UpcomingModule();
      Collection<Object> events = new ArrayList<Object>(
         upcoming.queryByLocation(Harness.YAHOO_DEVELOPER_KEY, args[0]));

      JavaObjectRdfSerializer serializer = new JavaObjectRdfSerializer(
         "http://www.upcoming.org/events#",
         "http://www.upcoming.org/ontology#",
         args[2], "net.semwebprogramming.upcoming");
      
      FileOutputStream fos = null;
      try
      {
         fos = new FileOutputStream(args[1]);
         serializer.serialize(events, fos);
         System.out.println("Success.");
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      finally
      {
         try
         {
            fos.close();
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
      }

   }
}
