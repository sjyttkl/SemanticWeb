package Chapter9.WeatherToRdfWithXsl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class WeatherToRdfWithXsl
{
   /**
    * Program that generates an RDF representation of the 
    * Weather.gov XML feed using XSLT
    * @param args
    *    xml file - The URL of the weather XML feed
    *       (http://www.weather.gov/xml/current_obs/KBWI.xml)
    *    xsl file - The file name of the XSLT
    *    output file - The file name of the output RDF document
    */
   public static void main(String[] args)
   {
      if(args.length != 3)
      {
         System.err.println("Usage: java WeatherToRdfWithXsl "
            + "[xml file url] [xsl file] [output file]");
         return;
      }

      String xmlFile = args[0];
      String xslFile = args[1];
      String outputFile = args[2];

      try
      {
         //create the output turtle file
         FileOutputStream outputStream = new FileOutputStream(outputFile);

         //get the xml file input
         URL xmlFileUrl = new URL(xmlFile);
         InputStream xmlFileInputStream = xmlFileUrl.openStream();

         //get the xsl file input
         FileInputStream xslInputStream = new FileInputStream(xslFile);

         //set up an output stream that we can redirect to the jena model
         ByteArrayOutputStream transformOutputStream = new ByteArrayOutputStream();

         //transform the xml document into rdf/xml
         TransformerFactory factory = TransformerFactory.newInstance();
         StreamSource xslSource = new StreamSource(xslInputStream);
         StreamSource xmlSource = new StreamSource(xmlFileInputStream);
         StreamResult outResult = new StreamResult(transformOutputStream);

         Transformer transformer = factory.newTransformer(xslSource);
         transformer.transform(xmlSource, outResult);
         transformOutputStream.close();

         //build a jena model so we can serialize to Turtle
         ByteArrayInputStream modelInputStream = new ByteArrayInputStream(
            transformOutputStream.toByteArray());

         Model rdfModel = ModelFactory.createDefaultModel();
         rdfModel.read(modelInputStream, null, "RDF/XML");
         rdfModel.write(outputStream, "TURTLE");
         outputStream.flush();

         System.out.println("Success.");
      }
      catch (MalformedURLException e)
      {
         e.printStackTrace();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      catch (TransformerConfigurationException e)
      {
         e.printStackTrace();
      }
      catch (TransformerException e)
      {
         e.printStackTrace();
      }
   }
}
