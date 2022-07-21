package Chapter11.rdfa;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;

import java.io.*;

/** A simple class that reads in RDF data and converts it to XHTML output suitable for publishing.
 *  There is a tight coupling between this code and the expected input.
 */
public class PublishRDFa {

    static final String rdfInput  = "input/friendtracker-ont.rdf";
    static final String rdfaOutput = "output/friendtracker.htm";
    static final String simpleDefaultNS = "http://semwebprogramming.net/";
    static final String defaultNS = simpleDefaultNS + "2009/Chapter11/rdfa#";
    static final String defaultPrefix = "smp";
    static final String defaultOntNS = simpleDefaultNS + "2009/ftracker#";
    static final String tableCellParams = "style=\"width: 23%;\"";
    static final String tableParams = " style=\"border: 5px solid black; width: 500px;\" ";
    static final String owner = defaultPrefix + ":Ryan";

	/** This could come from anywhere: SPARQL endpoint, resolvable URI.
	 *  Here, we'll read it from a file for simplicity.
	 */
	private Model readRDFInput() {
		// create an empty model
        Model model = ModelFactory.createDefaultModel();
        InputStream inputStr = FileManager.get().open(rdfInput);
        if (inputStr == null) {
            throw new IllegalArgumentException(rdfInput + " not found");
        }

        // read the RDF/XML file
        model.read(inputStr, "");
		return model;
	}

	/** Print out the beginning lines our our XHTML-RDFa file
	 * 
	 * @return
	 * @throws IOException
	 */
	private BufferedWriter writePreAction() throws IOException {
		BufferedWriter rdfaFile = new BufferedWriter(new FileWriter(rdfaOutput));
        
        rdfaFile.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        rdfaFile.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.0//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd\">\n");
        rdfaFile.write("<html version=\"XHTML+RDFa 1.0\" xmlns=\"http://www.w3.org/1999/xhtml\"\n");
       	rdfaFile.write("      xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"\n");
       	rdfaFile.write("      xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n");
       	rdfaFile.write("      xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n");
       	rdfaFile.write("      xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n");
        rdfaFile.write("      xmlns:ftrack=\"" + defaultOntNS + "\"\n");
        rdfaFile.write("      xmlns:" + defaultPrefix + "=\"" + defaultNS + "\"\n");
        rdfaFile.write("      xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\">\n");
        rdfaFile.write("<head>\n");
        rdfaFile.write("  <base href=\"" + owner + "\"/>\n");
        rdfaFile.write("  <title>Sample RDFa page - Friend Tracker</title>\n");
        rdfaFile.write("  <!-- validated at http://www.w3.org/2007/08/pyRdfa -->\n");
        rdfaFile.write("</head>\n");
        rdfaFile.write("<body>\n\n");
        
        return rdfaFile;
	}

	/** Write any isNamed property values
	 * 
	 * @throws IOException
	 */
	private void writeIsNamedProperty(Model model, BufferedWriter w, Resource person) throws IOException {
		Property prop = model.getProperty(defaultOntNS, "isNamed");
		if (person.hasProperty(prop)) {
			w.write("<tr>\n");
		    w.write("<th colspan=\"2\" property=\"ftrack:isNamed\">");
			NodeIterator names = model.listObjectsOfProperty(person, prop);
			while (names.hasNext()) {
				RDFNode name = names.nextNode();
				w.write(name.toString() + " ");
			}
		    w.write("\n</th>\n");
		    w.write("</tr>\n");
		}
	}
	
	/** Write any hasPic property values
	 * 
	 * @throws IOException
	 */
	private void writeHasPicProperty(Model model, BufferedWriter w, Resource person) throws IOException {
		Property prop = model.getProperty(defaultOntNS, "hasPic");
		if (person.hasProperty(prop)) {
		    w.write("<tr>\n");
		    w.write("<td " + tableCellParams + ">Picture:</td>\n");
		    w.write("<td>\n");
		    // Only get the first picture for simplicity
			NodeIterator pics = model.listObjectsOfProperty(person, prop);
			// Strip off any datatyping, such as "^^xsd:anyURI"
			RDFNode pic = pics.nextNode();
			String picURL = pic.toString();
			int dataTypelocation = picURL.lastIndexOf('^');
			if (dataTypelocation != -1)
				picURL = picURL.substring(0, picURL.lastIndexOf('^') - 1);
			w.write("<div rel=\"ftrack:hasPic\">");
		    w.write("<img alt=\"none\" src=\"" + picURL + "\"/>\n");
		    w.write("</div>\n");
		    w.write("</td>\n");
			w.write("</tr>\n");
		}
	}
	
	/** Write any hasStatus property values
	 *
	 * @throws IOException
	 */
	private void writeHasStatusProperty(Model model, BufferedWriter w, Resource person) throws IOException {
		Property prop = model.getProperty(defaultOntNS, "hasStatus");
		if (person.hasProperty(prop)) {
		    w.write("<tr>\n");
		    w.write("<td " + tableCellParams + ">Online Status:</td>\n");
		    // Only get the first status for simplicity
			NodeIterator stati = model.listObjectsOfProperty(person, prop);
			RDFNode status = stati.nextNode();
		    w.write("<td rel=\"ftrack:hasStatus\" href=\"" + status.toString()  + "\">");
		    w.write(((Resource)status).getLocalName());
			w.write("\n</td>\n");
			w.write("</tr>\n");
		}
	}
	
	/** Write any hasEmail property values
	 *
	 * @throws IOException
	 */
	private void writeHasEmailProperty(Model model, BufferedWriter w, Resource person) throws IOException {
		Property prop = model.getProperty(defaultOntNS, "hasEmailAddress");
		if (person.hasProperty(prop)) {
			w.write("<tr>\n");
		    w.write("<td " + tableCellParams + ">Email:</td>\n");
		    w.write("<td>");
			NodeIterator addrs = model.listObjectsOfProperty(person, prop);
			while (addrs.hasNext()) {
				RDFNode address = addrs.nextNode();
			    w.write("\n<div rel=\"ftrack:hasEmailAddress\" href=\"" + address.toString() + "\">");
				w.write(address.toString() + " ");
				w.write("</div>\n");
			}
		    w.write("</td>\n");
		    w.write("</tr>\n");
		}
	}
	
	/** Write any isFrom property values
	 * 
	 * @throws IOException
	 */
	private void writeIsFromProperty(Model model, BufferedWriter w, Resource person) throws IOException {
		Property prop = model.getProperty(defaultOntNS, "isFrom");
		if (person.hasProperty(prop)) {
			w.write("<tr>\n");
		    w.write("<td " + tableCellParams + ">Origin Location(s):</td>\n");
		    w.write("<td>");
			NodeIterator locations = model.listObjectsOfProperty(person, prop);
			while (locations.hasNext()) {
				RDFNode location = locations.nextNode();
			    w.write("\n<div rel=\"ftrack:isFrom\" href=\"" + location.toString() + "\">");
				w.write(location.toString() + " ");
				w.write("</div>\n");
			}
		    w.write("\n</td>\n");
		    w.write("</tr>\n");
		}
	}
	
    /** Final wrap up of our output
     * 
     * @throws IOException
     */
	private static void writePostAction(BufferedWriter rdfaWriter)
			throws IOException {
		rdfaWriter.write("</body>\n");
		rdfaWriter.write("</html>\n");
		rdfaWriter.flush();
		rdfaWriter.close();
	}
	
    public static void main (String args[]) {
    	
    	PublishRDFa rdfaFactory = new PublishRDFa();
    	
    	// Read in our data
        Model model = rdfaFactory.readRDFInput();
        
        // Open the output RDFa file and output a standard XHTML header
        BufferedWriter rdfaWriter = null;
		try {
			rdfaWriter = rdfaFactory.writePreAction();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        // Cycle through any friend instances and build the XHTML+RDFa syntax
        Resource friendClass = model.getResource( defaultOntNS + "Friend" );
        ResIterator friendIter = model.listSubjectsWithProperty(RDF.type, friendClass);
        try {
        	while (friendIter.hasNext()) {
        		Resource person = friendIter.nextResource();

				rdfaWriter.write("<div about=\"" + owner + "\" rel=\"foaf:knows\">\n");
				rdfaWriter.write("<div about=\"" + defaultPrefix + ":" + person.getLocalName() + "\">\n");
	            rdfaWriter.write("<p/><table " + tableParams + ">\n");
	            
	            // Do we have any isNamed information?
	            rdfaFactory.writeIsNamedProperty(model, rdfaWriter, person);
	            
	            // Do we have any hasPic information?
	            rdfaFactory.writeHasPicProperty(model, rdfaWriter, person);
	            
	            // Do we have any hasStatus information?
	            rdfaFactory.writeHasStatusProperty(model, rdfaWriter, person);
	    		
	           // Do we have any hasEmailAddress information?
	            rdfaFactory.writeHasEmailProperty(model, rdfaWriter, person);
	            
		        // Do we have any isFrom information?
	            rdfaFactory.writeIsFromProperty(model, rdfaWriter, person);
	            
	    		rdfaWriter.write("</table>\n");
	    		rdfaWriter.write("</div>\n");
	    		rdfaWriter.write("</div>\n\n");
        	}
	            
	        writePostAction(rdfaWriter);   
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
} 