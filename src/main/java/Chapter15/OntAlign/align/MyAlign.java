 
package Chapter15.OntAlign.align;

/*
 * $Id: MyAlign.java 851 2008-09-27 08:58:49Z euzenat $
 *
 * Copyright (C)  INRIA 2003-2005, 2008
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */

// Align API

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentProcess;
import org.semanticweb.owl.align.AlignmentVisitor;
import org.semanticweb.owl.align.Parameters;
import org.semanticweb.owl.align.Evaluator;

// Align API Implementation
import fr.inrialpes.exmo.align.impl.BasicParameters;
import fr.inrialpes.exmo.align.parser.AlignmentParser;
import fr.inrialpes.exmo.align.impl.method.SubsDistNameAlignment;
import fr.inrialpes.exmo.align.impl.method.SMOANameAlignment;
import fr.inrialpes.exmo.align.impl.method.NameAndPropertyAlignment;
import fr.inrialpes.exmo.align.impl.method.EditDistNameAlignment;
import fr.inrialpes.exmo.align.impl.eval.PRecEvaluator;
import fr.inrialpes.exmo.align.impl.renderer.HTMLRendererVisitor;
import fr.inrialpes.exmo.align.impl.renderer.OWLAxiomsRendererVisitor;
import fr.inrialpes.exmo.align.impl.renderer.SWRLRendererVisitor;
import fr.inrialpes.exmo.align.ling.JWNLAlignment;

// Java classes
import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URI;



public class MyAlign {
	static String ont1 = "http://localhost/~jhebeler/edu.umbc.ebiquity.publication.owl";
	static String ont2 = "http://localhost/~jhebeler/edu.mit.visus.bibtex.owl";
	
	//static String ont1 = "http://localhost/~jhebeler/foaf.rdf";
	//static String ont2 = "http://localhost/~jhebeler/additionalFriendsSchema.owl";
	//static String ont2 = "http://localhost/~jhebeler/sioc.rdf";


    public static void main(String[] args) {
    	System.out.println("Starting Alignment");
	try {
	    URI uri1 = new URI(ont1);
	    URI uri2 = new URI(ont2);

	    Parameters p = new BasicParameters();
	    
	    AlignmentProcess ap1 = new SubsDistNameAlignment();
	    ap1.init( uri1, uri2 );
	    AlignmentProcess ap2 = new SMOANameAlignment();
	    ap2.init( uri1, uri2 ); 
	    //AlignmentProcess A3 = new NameAndPropertyAlignment();
	    AlignmentProcess ap3 = new EditDistNameAlignment();
	    ap3.init( uri1, uri2 ); 
	    AlignmentProcess ap4 = new JWNLAlignment();
	    ap4.init(uri1, uri2);
	    
	    
	    ap1.align((Alignment)null,p); ap1.cut("prop", .5); 
	    ap2.align((Alignment)null,p); ap3.align(ap2,p); 
	    p.setParameter("wndict", "/Tools/WordNet-3.0/dict");
	    ap4.align((Alignment) null,p);
	    
	   
	    PRecEvaluator eval = new PRecEvaluator(ap1, ap3);     
	    
	    eval.eval(p); 
	    System.out.println("Precision = " + eval.getPrecision() + " Recall = " + eval.getRecall() + " F = " + eval.getFmeasure() );
	    
	    PrintWriter pw = new PrintWriter (
			       new BufferedWriter(
				 new OutputStreamWriter( System.out, "UTF-8" )),
			       true);
	    File fileHTML = new File("OntAlign.html");
	    PrintWriter pwhtml = new PrintWriter(fileHTML);
	    AlignmentVisitor V = new SWRLRendererVisitor( pw ); 
	    AlignmentVisitor V2 = new OWLAxiomsRendererVisitor(pw);
	    AlignmentVisitor V3 = new HTMLRendererVisitor(pwhtml);
	    
	    
	    if ( ((PRecEvaluator)eval).getPrecision() > .6 ) ap3.render(V);
	    System.out.println("\n\nStarting OWL MErged ONtology");
	    if( ((PRecEvaluator)eval).getPrecision() > .6) ap3.render(V2);
	    pw.flush();
	    System.out.println("\n\nStarting HTML Alignment");
	    if( ((PRecEvaluator)eval).getPrecision() > .2) ap3.render(V3);	    
	    pw.flush(); 
	    System.out.println("\n\n Starting WORDNET Alignment");
	    ap4.render(V3);
	    pw.close(); // necessary when the program is really embedded
	    pwhtml.flush();
	    pwhtml.close();
	} catch (Exception e) { e.printStackTrace(); };
    }
}
