package Chapter13.JenaSpatialIndex.temporal;

import java.io.PrintStream;
import java.util.Calendar;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

/**
 *  Class to demonstrate the use of the TemporalGraph.  Given two RDF files,
 *  the program reads in one graph, pauses and notes the time, and reads in 
 *  the second.  Then it generates two sub graphs, one composed of all those 
 *  statements added before the midpoint, and the second with all the 
 *  statements added after the midpoint.  Finally the program writes the 
 *  two subgraphs and the entire graph to the console. 
 *
 */
public class Example {

	/**
	 * Prints a model to a given PrintStream, with a header and footer that 
	 * reference a name for the graph.
	 * 
	 * @param model the model to output
	 * @param name the name to give for the model
	 * @param out the PrintStream to which the model should be written
	 */
	private void printModel(Model model, String name, PrintStream out) {
		out.println(String.format("Starting %1$s...", name));
		model.write(out, "TURTLE");
		out.println(String.format("End of %1$s.", name));
	}
	
	/**
	 * Take the two input files, of the two formats, and read them in.  Then create 
	 * temporal subgraphs and write out the two subgraphs and overall model to the 
	 * console.
	 * 
	 * @param fileNameOne the first RDF data file
	 * @param fileNameTwo the second RDF data file
	 * @param formatOne the format of the first RDF data file
	 * @param formatTwo the format of the second RDF data file
	 */
	private void doMain(String fileNameOne, String fileNameTwo, String formatOne, String formatTwo) {
		Model model;
		Model subModel;

		Calendar midpoint;
		
		model = ModelFactory.createModelForGraph(TemporalGraph.newInstance());
		model.read(FileManager.get().open(fileNameOne), null, formatOne);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		midpoint = Calendar.getInstance();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		model.read(FileManager.get().open(fileNameTwo), null, formatTwo);

		subModel = ModelFactory.createModelForGraph(((TemporalGraph) model.getGraph()).getTemporalSubgraph(null, midpoint));
		printModel(subModel, "First part", System.out) ;
		subModel = ModelFactory.createModelForGraph(((TemporalGraph) model.getGraph()).getTemporalSubgraph(midpoint, null));
		printModel(subModel, "Second part", System.out);
		printModel(model, "Whole Model", System.out);
	}
	
	/**
	 * Main method - takes two files and two formats in the following order:
	 * 
	 * |FILE 1| |FILE 2| |FILE 1 FORMAT| |FILE 2 FORMAT| 
	 * 
	 */
	public static void main(String[] args)
	{
		Example me = new Example();
		me.doMain(args[0], args[1], args[2], args[3]);
	}
}
