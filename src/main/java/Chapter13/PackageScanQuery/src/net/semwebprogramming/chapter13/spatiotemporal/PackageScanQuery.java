package Chapter13.PackageScanQuery.src.net.semwebprogramming.chapter13.spatiotemporal;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class PackageScanQuery {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dataFile = args[0];
		String queryFile = args[1];

		Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);
		FileManager fileManager = FileManager.get();
		ResultSet results;
		String queryString;
		String lat = null, lon = null, name = null, time = null;
		
		fileManager.readModel(model, dataFile, null, "TURTLE" );
		
		queryString = fileManager.readWholeFileAsUTF8(queryFile);
		
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExec = QueryExecutionFactory.create(query, model);
		
		results = queryExec.execSelect();
		
		while(results.hasNext()){
			QuerySolution solution = results.nextSolution();

			lat = solution.getLiteral("lat").getString();
			lon = solution.getLiteral("lon").getString();
			name = solution.getLiteral("name").getString();
			time = solution.getLiteral("time").getString();

			System.out.println(
					String.format("Package was at '%1$s':(%2$s, %3$s) at %4$s",
							name, lat, lon, time));
		}
	}

}
