package Chapter13.JenaSpatialIndex.spatial;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.mem.GraphMem;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

/**
 * Sample application to issue a spatial query with the
 * JenaSpatialIndex class from Chapter 13.
 * 
 * The first parameter should be the path to a file with 
 * points, and the second should be the format of the 
 * RDF serialization of the file.  Included with this
 * code is a file, location-data.tur.  To run this 
 * example with that data file, the parameters would be:
 * 
 * location-data.tur TURTLE
 * 
 * This example is very simple.  It first reads in the 
 * given data file and creates a JenaSpatialIndex based 
 * on that data.  Then it creates a sub graph containing only 
 * those points which are within a defined spatial region.
 * That happens with a call to JenaSpatialIndex.getSpatialSubgraph
 * on line 55.  The parameters are latitude, longitude, and 
 * distance in meters, respectively.  Finally, the program writes 
 * out the contents of the sub graph to the console.  
 * 
 */
public class Example 
{
	public static void main(String[] args)
	{
		String filename;
		String fileformat;
		
		if(args.length < 2)
		{
			System.err.println("Please specify an input file and format.");
			return;
		}
		
		filename = args[0];
		fileformat = args[1];
		
		Graph g = new GraphMem();
		SpatialGraph spg = new SpatialGraph(g);
		Graph subGraph;
		
		Model model = ModelFactory.createModelForGraph(spg);
		
		FileManager.get().readModel(model, filename, fileformat);

		subGraph = spg.getSpatialSubgraph(38.893754, -77.072568, 10000);

		Model subModel = ModelFactory.createModelForGraph(subGraph);
		
		subModel.write(System.out, "TURTLE");
		subModel.close();
		model.close();
	}
}
