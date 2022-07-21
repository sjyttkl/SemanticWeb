package Chapter9.WordPresstoRdfWithD2R;

import java.io.FileOutputStream;
import java.io.IOException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;


public class WordPressToRdfWithD2RQ
{
   /**
    * This program issues a construct query against the WordPress database
    * in order to generate an RDF graph of results that it serializes to 
    * an output file in Turtle format.
    * @param args
    *    query file - the file to load the queries from
    *    query name - the specific query to execute from the file
    *    mapping file - the D2RQ mapping file
    *    output file - the File into which to write the query results
    */
   public static void main(String[] args)
   {
      if(args.length < 5)
      {
         System.err.println("Usage: java WordPressToRdfWithD2RQ"
            + "[query file] [query name] [mapping file] [output file] [output format]");
         return;
      }
      
      String queryFile = args[0];
      String queryName = args[1];
      String mappingFile = args[2];
      String outputFile = args[3];
      String outputFormat = args[4];

      try
      {
         //load our queries
         QueryReader queryReader = QueryReader.createQueryReader(queryFile);
         String queryStr = queryReader.getQuery(queryName);
         System.out.println(queryStr);
         
         //create the d2rq model using the mapping file
         Model d2rqModel = new ModelD2RQ(mappingFile);
         
         //create the query
         Query query = QueryFactory.create(queryStr);
         QueryExecution qExec = QueryExecutionFactory.create(query, d2rqModel);
         
         //execute the query
         Model results = qExec.execConstruct();
         
         //output the resulting graph
         FileOutputStream outputStream = new FileOutputStream(outputFile);
         results.write(outputStream, outputFormat);
         outputStream.close();
         
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
