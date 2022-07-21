package Chapter3.RDFSerializer;

import java.io.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RDFSerializer {

	/**
	 * This program takes 4 parameters: 
	 *   an input file
	 *   an output file
	 *   an input format
	 *   an output format
	 * 
	 * It will then use Jena to read in the input file in 
	 * the specified input format, and write it out in the
	 * specified output format. 
	 * 
	 */
	public static void main(String[] args) {
		String inputFileName = null;
		String outputFileName = null;
		String inputFileFormat = null;
		String outputFileFormat = null;
		FileOutputStream outputStream = null;
		FileInputStream inputStream = null;
		
		if(args.length != 4) {
			System.err.println("Usage: java RDFSerializer <input file> <output file> <input format> <output format>");
			System.err.println("Valid format strings are: RDF/XML, N3, TURTLE, and N-TRIPLE");
			return;
		}

		inputFileName = args[0];
		outputFileName = args[1];
		inputFileFormat = args[2];
		outputFileFormat = args[3];

		try {
			inputStream = new FileInputStream(inputFileName);
			outputStream = new FileOutputStream(outputFileName);
		} catch (FileNotFoundException e) {
			System.err.println("'" + outputFileName + "' is an invalid file name.");
			return;
		}
		
		Model rdfModel = ModelFactory.createDefaultModel();
		rdfModel.read(inputStream, null, inputFileFormat);
		rdfModel.write(outputStream, outputFileFormat);
		
		try {
			outputStream.close();
		} catch (IOException e) {
			System.err.println("Error writing to file.");
			return;
		}
	}
}
