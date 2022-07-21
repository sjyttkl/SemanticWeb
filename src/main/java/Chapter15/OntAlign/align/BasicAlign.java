package Chapter15.OntAlign.align;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentProcess;
import org.semanticweb.owl.align.AlignmentVisitor;
import org.semanticweb.owl.align.Parameters;

import fr.inrialpes.exmo.align.impl.BasicParameters;
import fr.inrialpes.exmo.align.impl.eval.PRecEvaluator;
import fr.inrialpes.exmo.align.impl.method.EditDistNameAlignment;
import fr.inrialpes.exmo.align.impl.method.SMOANameAlignment;
import fr.inrialpes.exmo.align.impl.method.SubsDistNameAlignment;
import fr.inrialpes.exmo.align.impl.renderer.HTMLRendererVisitor;
import fr.inrialpes.exmo.align.impl.renderer.OWLAxiomsRendererVisitor;
import fr.inrialpes.exmo.align.impl.renderer.SWRLRendererVisitor;
import fr.inrialpes.exmo.align.ling.JWNLAlignment;

public class BasicAlign {
	
	static String ont1 = "http://semwebprogramming.org:8099/2008/friendtracker.rdf";
	static String ont2 = "http://semwebprogramming.org:8099/2008/sioc.rdf";


    public static void main(String[] args) {
    	System.out.println("Starting Alignment");
	try {
	    URI uri1 = new URI(ont1);
	    URI uri2 = new URI(ont2);

	    Parameters parm = new BasicParameters();
	    
	     AlignmentProcess ap = new SubsDistNameAlignment();
	    //AlignmentProcess ap = new JWNLAlignment();
	    ap.init( uri1, uri2 );
	    parm.setParameter("wndict", "/Tools/WordNet-3.0/dict");
	    ap.align((Alignment)null,parm);
	    //ap.cut("prop", .6); 
	   
	    // Set up PrintWriter(s)
	    File fileHTML = new File("/Users/jhebeler/Sites/SubsDistNameAlignFT.html");
	    PrintWriter pwHTML = new PrintWriter(fileHTML);
	    File fileSWRL = new File("/Users/jhebeler/Sites/SubsDistNameAlignSWRLFT.xml");
	    PrintWriter pwSWRL = new PrintWriter(fileSWRL);    
	    File fileOWL = new File("/Users/jhebeler/Sites/SubsDistNameAlignFT.owl");
	    PrintWriter pwOWL = new PrintWriter(fileOWL);
	    
	    AlignmentVisitor avHTML = new HTMLRendererVisitor(pwHTML);
	    AlignmentVisitor avSWRL = new SWRLRendererVisitor( pwSWRL ); 
	    AlignmentVisitor avOWL = new OWLAxiomsRendererVisitor(pwOWL);

	    ap.render(avHTML);
	    ap.render(avSWRL);
	    ap.render(avOWL);
	    
	    // Flush and close the files
	    pwHTML.flush(); pwHTML.close();
	    pwSWRL.flush(); pwSWRL.close();
	    pwOWL.flush();  pwOWL.close();
	    
	   
		} catch (Exception e) { e.printStackTrace(); }
	 System.out.println("Alignment Complete");
    }
  
}
