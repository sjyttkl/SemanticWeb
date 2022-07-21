package FriendTracker.Friendtracker.friendtracker.data.implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.semwebprogramming.chapter9.QueryReader;
import net.semwebprogramming.friendtracker.FriendTrackerConfiguration.WordPressConfiguration;
import net.semwebprogramming.friendtracker.data.FriendSource;
import net.semwebprogramming.friendtracker.data.PostSource;
import net.semwebprogramming.friendtracker.data.implementation.vocabulary.FriendTracker;
import net.semwebprogramming.friendtracker.model.Friend;
import net.semwebprogramming.friendtracker.model.Post;
import net.semwebprogramming.friendtracker.utilities.ProperException;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;

/**
 * An implementation of the PostSource and FriendSource interfaces that 
 * provides information about friends and blog posts from a WordPress blog.
 */
public class WordPressSource extends JenaSource implements PostSource,
		FriendSource {

	private final Object _lock = new Object();
	private List<Post> _posts;
	private WordPressConfiguration _config;
	private boolean _loaded;
	
	// first is friendtracker ont URI, second is email address to match
	private static final String POSTS_QUERY_STRING = 
		"PREFIX ft: <%1$s>" + "\n\n" +
		"SELECT ?post WHERE { ?author ft:hasEmailAddress \"%2$s\" ; ft:hasPost ?post }";
	
	
	@Override
	protected void createModel() {
		_model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);
	}

	@Override
	public void initialize(Object configurationObject) {
		if(!(configurationObject instanceof WordPressConfiguration))
		{
			throw new ProperException(new IllegalArgumentException("Configuration object must be of type WordPressConfiguration"));
		}
		_config = (WordPressConfiguration) configurationObject;
		_loaded = false;
	}

	InputStream getRdf()
	{
		QueryReader queryReader;
		String queryString;
		Model d2rqModel;
		ByteArrayOutputStream baos;
		Query query;
		QueryExecution queryExecution;
		Model rdfResults;

		
		baos = new ByteArrayOutputStream();
		try {
			queryReader = QueryReader.createQueryReader(_config.getQueryLocation());
			queryString = queryReader.getQuery(_config.getQueryName());
			 
			//create the d2rq model using the mapping file
			d2rqModel = new ModelD2RQ(_config.getMappingLocation());
			 //create the query
			query = QueryFactory.create(queryString);
			queryExecution = QueryExecutionFactory.create(query, d2rqModel);
			 
			 //execute the query
			rdfResults = queryExecution.execConstruct();
			 
			 //output the resulting graph
			rdfResults.write(baos, "RDF/XML");
		} catch (IOException e) {
			throw new ProperException(e);
		}
		 
		return new ByteArrayInputStream(baos.toByteArray());
	}
	
	private void doLoad()
	{
		Transformer t;
		InputStream rdfStream;
		ByteArrayOutputStream baos;
		
		File xslFile;
		Source xsl;
		
		StreamResult xslResults;
		
		synchronized(_lock)
		{
			if(!_loaded)
			{
				try
				{
					// set up the XSL transform stuff 
					xslFile = new File(_config.getXslLocation());
					xsl = new StreamSource(xslFile);
					t = TransformerFactory.newInstance().newTransformer(xsl);

					baos = new ByteArrayOutputStream();
					xslResults = new StreamResult(baos);
			        rdfStream = getRdf();
			        t.transform(new StreamSource(rdfStream), xslResults);

			        _model.read(new ByteArrayInputStream(baos.toByteArray()), "", "RDF/XML");
			        
			        _posts = extractPosts();
			        
			        _loaded = true;
				}
				catch(TransformerException e)
				{
					throw new ProperException(e);
				}
				finally
				{
					
				}
			}
		}
	}
	
	public List<Post> getPosts(Friend author) {
		List<Post> toReturn = new ArrayList<Post>();
		String queryString;
		Set<String> msgIds = new HashSet<String>();
		Query query; 
		QueryExecution queryExecution;
		
		ResultSet rs;
		
		doLoad();
		
		for(String email : author.getEmailAddresses())
		{
			queryString = String.format(POSTS_QUERY_STRING, 
					FriendTracker.Base.getString(), 
					email);
			query = QueryFactory.create(queryString);
			queryExecution = QueryExecutionFactory.create(query, _model);
			
			rs = queryExecution.execSelect();
			while(rs.hasNext()){
				QuerySolution solution = rs.nextSolution();
				RDFNode postNode = solution.get("post");
				if(null != postNode)
				{
					msgIds.add(postNode.asNode().getURI());
				}
			}
		}
		for(Post p : _posts)
		{
			if(msgIds.contains(p.getID()))
			{
				toReturn.add(p);
			}
		}
		
		return toReturn;
	}

	public List<Friend> getFriends() {
		doLoad();
		return extractFriends();
	}

}
