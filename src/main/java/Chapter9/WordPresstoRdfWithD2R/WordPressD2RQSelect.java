package Chapter9.WordPresstoRdfWithD2R;

import java.io.IOException;
import java.io.PrintWriter;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;

public class WordPressD2RQSelect
{
   /**
    * This program issues a select query against the WordPress database
    * in order to generate a table of results.
    * @param args 
    *    query file - the file to load the queries from
    *    query name - the specific query to execute from the file
    *    mapping file - the D2RQ mapping file
    *    output file - the File into which to write the query results
    *    string args - optional parameters to substitute into a templated
    *       query
    */
   public static void main(String[] args)
   {
      if(args.length < 4)
      {
         System.err.println("Usage: java WordPressD2RQSelect"
            + "[query file] [query name] [mapping file] [output file] [ \"string args\" ]*");
         return;
      }
      
      String queryFile = args[0];
      String queryName = args[1];
      String mappingFile = args[2];
      String outputFile = args[3];
      
      int i = 0;
      Object[] strArgs = new Object[args.length - 4];
      for(int j=4; j < args.length; j++)
      {
         strArgs[i++] = args[j]; 
      }
      
      try
      {
         //load our queries
         QueryReader queryReader = QueryReader.createQueryReader(queryFile);
         String queryStr = String.format(queryReader.getQuery(queryName), strArgs);
         
         System.out.println(queryStr);
         
         //create the d2rq model
         Model d2rqModel = new ModelD2RQ(mappingFile);
         
         //create our query
         Query query = QueryFactory.create(queryStr);
         QueryExecution qExec = QueryExecutionFactory.create(query, d2rqModel);
         
         //execute the query
         ResultSet resultSet = qExec.execSelect();
         
         //output the results
         PrintWriter writer = new PrintWriter(outputFile);
         i = 0;
         while(resultSet.hasNext())
         {
            writer.println(String.format("Entry %1$s ===========", ++i));
            
            QuerySolution soln = resultSet.nextSolution();
            for(Object var : resultSet.getResultVars())
            {
               String val = null;
               RDFNode node = soln.get(var.toString());
               if(node instanceof Literal)
               {
                  val = ((Literal)node).getLexicalForm();
               }
               else if(node instanceof Resource)
               {
                  val = ((Resource)node).getURI();
               }
                  
               writer.println(String.format("%1$s: %2$s", var.toString(), val));
            }
         }
          writer.flush();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
