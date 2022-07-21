package FriendTracker.Upcoming.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EventBuilder
{
   protected VEvent buildEvent(Node eventElement)
   {
      VEvent toReturn = new VEvent();

      for(Attributes field : Attributes.values())
      {
         String fieldStr = Attributes.getString(field);
         String value = null;
         
         Node valNode = eventElement.getAttributes().getNamedItem(fieldStr);
         if(null != valNode)
         {
            value = valNode.getNodeValue();
         }

         if(!"".equals(value))
         {
            toReturn.setProperty(field, value);
         }
      }

      return toReturn;
   }

   public List<VEvent> buildEvents(InputStream is)
      throws IOException, SAXException, ParserConfigurationException
   {
      List<VEvent> toReturn = new ArrayList<VEvent>();
      
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      Document dom = dbf.newDocumentBuilder().parse(is);

      NodeList eventElements = dom.getElementsByTagName(
         Elements.getString(Elements.Event));
      
      for(int i = 0; i < eventElements.getLength(); i++)
      {
         toReturn.add(buildEvent(eventElements.item(i)));
      }

      return toReturn;
   }

}
