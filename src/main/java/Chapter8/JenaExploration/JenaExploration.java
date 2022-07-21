package Chapter8.JenaExploration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.prefs.BackingStoreException;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.listeners.ObjectListener;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.NsIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RSIterator;
import com.hp.hpl.jena.rdf.model.ReifiedStatement;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.reasoner.dig.DIGReasoner;
import com.hp.hpl.jena.reasoner.dig.DIGReasonerFactory;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class JenaExploration {

	final private String defaultNameSpace = 
		"http://semwebprogramming.org/2009/ont/chp8#";
    private OntModel _modelMem = null, _modelDB = null;
    private InfModel _jenaInferModel = null, _jenaRuleModel = null, 
             _pelletInferModel = null, _digInferModel = null;   
	private JenaListener _listenerMem = null, _listenerDB = null;
	
	public static void main(String[] args)  {
	    
	    try {
	        
		JenaExploration jena = new JenaExploration();
		
		// Initial Creation
		jena.aquireMemoryForData();
		jena.aquireDBForData();
		
		// Bulk Populate
		jena.addDataFromFile();
		jena.addDataFromURL();
		
		// Monitor Events
		jena.setEventListener();  // Part of Management		
		
		// Populate with Statements
		jena.addDataFromStatements();
		jena.addDatafromOntology();
		
		jena.addLiterals();
		jena.addReifiedStatements();
		
		// Get Status
		jena.getModelInfo();  // Part of Management
		
		// Combinations
		jena.combineData();
		//QueryResults queryrel = null;
		// Interrogation
		jena.searchAndNavigateData();
		jena.queryData();
		
		// Reason
		jena.reasonOverData();
		
		// Validate
		jena.validateData();
		
		// Critical region 
		jena.criticalRegionWrite();   // Part of Management
		
		// Export the Model
		jena.writeData();
		
	    // Create a Custom Graph for the Model
        jena.createCustomModel();  // Part of Management
	     
		// Clear and Close
		jena.clearAndCloseData();
		
	    } 
        catch (Exception e) {
            System.out.println("Failure: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            return;
        }
	}
	
	// **************
	
	public JenaExploration () {
		System.out.println("JenaExploration started");
		_listenerMem = new JenaListener(_modelMem, "modelMem");
		_listenerDB = new JenaListener(_modelDB, "modelDB");
	}
	
	private Model _customModel = null;
	private void createCustomModel(){
	    System.out.println("Creating null Custom Model");
		CustomGraph myGraph = new CustomGraph();
		_customModel = ModelFactory.createModelForGraph(myGraph);
	}
	
	private void writeData() throws IOException{
	    FileOutputStream outFoaf= null, outFoafInstance=null;
	    
	    outFoaf = new FileOutputStream("Ontologies/foaf.turtle");
	    outFoafInstance = new FileOutputStream("Ontologies/foafInstance.turtle");
	    
	    _modelMem.write(outFoaf, "TURTLE");
	    _modelDB.write(outFoafInstance, "TURTLE");
	     outFoaf.close();
	    outFoafInstance.close();
    }
	
	
	private void aquireMemoryForData(){
		//_modelMem = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);	
		
		ModelMaker modelMaker = ModelFactory.createFileModelMaker("temp/filesave.owl");
		Model modeltmp = modelMaker.createDefaultModel();
		_modelMem = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, modeltmp);
	}
	
	private void reasonOverData(){
		_jenaInferModel = bindJenaInferenceReasoner();
		_jenaRuleModel = bindJenaRuleReasoner();
		_pelletInferModel = bindPelletReasoner();
		_digInferModel = bindDigReasoner();
	}
	
	private void combineData(){
		Model _modelNew;
		// Add two models together model1.add(model2)
		_modelMem =  (OntModel) _modelMem.add((Model)_modelDB); //allows cascading
		
		// Union of two models model1.union(model2)
		_modelNew = _modelMem.union(_modelDB);  //creates a new model
		
		// Intersection of two models model1.intersection(model2)
		_modelNew = _modelMem.intersection(_modelDB);  // creates a new model
		
		// Difference of two models  model1.difference(model2)
		_modelNew = _modelMem.difference(_modelDB);  // creats a new model
		
		// Are two models equal model1.equals(model2)
		
		if( _modelNew.equals(_modelMem)){
			System.out.println("Underlying Graph Objects are identical");
		}
		
		if (_modelNew.isIsomorphicWith(_modelMem)){
			System.out.println("Model Statements are identical");
		}
	}
	
	private void addLiterals(){
		Literal name = _modelMem.createLiteral("John");
		Literal age = _modelMem.createTypedLiteral(7);
		
		System.out.println("Name: " + name.getString() + "  Type: " + ((name.getDatatypeURI()!=null)?name.getDatatypeURI():"None"));
		System.out.println("Age: " + age.getString() + "  Type: "+ ((age.getDatatypeURI()!=null)?age.getDatatypeURI():"None"));
		Resource res = _modelMem.getResource(defaultNameSpace + "Joe");
		Property prop = _modelMem.getProperty(defaultNameSpace + "hasfriend");
		
		// Two ways to add typed literals
		_modelMem.addLiteral(res, prop, 7);
		_modelMem.add(res, prop, age);
	}
	
	private void addReifiedStatements(){
		Resource resSubject = _modelMem.getResource(defaultNameSpace + "Joe");
		Property prop = _modelMem.getProperty(defaultNameSpace + "seenAt");
		Resource resObject = _modelMem.getResource(defaultNameSpace + "Wendys");
		Statement state = _modelMem.createStatement(resSubject, prop, resObject);
		Resource reifiedResource = _modelMem.createReifiedStatement(state);
		
		Property propseen = _modelMem.getProperty(defaultNameSpace + "atTime");
		_modelMem.add(reifiedResource, propseen, "12:30AM");
		
		RSIterator iter = _modelMem.listReifiedStatements();
		while(iter.hasNext()){
			ReifiedStatement rei = iter.nextRS();
			StmtIterator iter2 = rei.listProperties();
			System.out.println("Statement: " + rei.getStatement().getSubject().toString() + " " + rei.getStatement().getPredicate().toString() + " " + rei.getStatement().getObject().toString());
			while( iter2.hasNext()){
				Statement st = iter2.nextStatement();
				System.out.println("Reified Statement: " + st.getPredicate().toString() + " " + st.getObject().toString()  );
			}
		}
	}
	
	private void validateData(){
		validateDataFromModel(_jenaInferModel);
		validateDataFromModel(_jenaRuleModel);
		validateDataFromModel(_pelletInferModel);
	}
	
	private void validateDataFromModel(InfModel infModel){
		System.out.println("Validation of "+ infModel.getReasoner().toString());
	    ValidityReport report = infModel.validate();
	    // returns true if Model is logically consistent (i.e. valid) and generates no warnings
	    if ( report.isClean() != true ){
	    	    Iterator<ValidityReport.Report> iter = report.getReports();
	    	    while( iter.hasNext()){
	    	    	System.out.println(((ValidityReport.Report)iter).isError?"ERROR: ":"Warning:" + ((ValidityReport.Report)iter).description);    	
	    	    	}
	    	    }	
	    else
	    	System.out.println("Model is clean");		
	}
	
	private void aquireDBForData() throws SQLException, ClassNotFoundException {
	    IDBConnection conn = null;
	    Model modeltmp = null;

		Class.forName("com.mysql.jdbc.Driver");		    	
		System.out.println("JDBC Driver found");

    	String DB_URL = new String( "jdbc:mysql://172.16.133.50/jena");
    	String DB_USER = new String("jena");
    	String DB_PASSWD = new String("jena");
    	String DB_TYPE = new String("MySQL");
 
    	System.out.println("URL: " + DB_URL + " USER: " + DB_USER 
    			+ " DB_PASSWD: " + DB_PASSWD + " DB_TYPE: " + DB_TYPE);
       
    	conn = 
    		new DBConnection(DB_URL, DB_USER, DB_PASSWD, DB_TYPE);
    	
    	  if(conn.getConnection() != null)  // throws exception
    	       System.out.println("Connection Successful"); 	
   
    	ModelMaker maker = ModelFactory.createModelRDBMaker(conn);

    	//check to see if the model is already present in db
    	if(conn.containsModel("FoafInstancesDB")){
    		System.out.println("Opening existing model");
    		modeltmp=maker.openModel("FoafInstancesDB",true); //throws exception if not present
    		}
    	else {
    		System.out.println("Creating new model");
    		modeltmp = maker.createModel("FoafInstanceDB");
    		}
    	OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
    	_modelDB = ModelFactory.createOntologyModel(spec,modeltmp); 
		}
	
	private void addDataFromURL(){
		System.out.println("Loading FOAF ontology URL");
		_modelMem.read("http://xmlns.com/foaf/spec/index.rdf");
	}
	
    private void addDataFromFile() throws IOException{
    	System.out.println("Loading from FOAF instance File");
		InputStream inFoafInstance = FileManager.get().open("Ontologies/FOAFFriends.rdf");
		_modelDB.read(inFoafInstance,defaultNameSpace);
		inFoafInstance.close();	
    }
    
    private void addDataFromStatements(){
    	System.out.println("Adding statement to model");
    	// Create resources
		Resource resource = _modelDB.createResource(defaultNameSpace + "me");
		Property prop = _modelDB.createProperty("http://www.w3.org/2002/07/owl#sameAs");
		//Property prop2 = OWL.sameAs();
		Resource obj = _modelDB.createResource(defaultNameSpace + "Individual_5");
		_modelDB.add(resource,prop,obj);
        // or
		_modelDB.add(resource,OWL.sameAs,obj);
    }
    
	private void addDatafromOntology(){
		Ontology ont = _modelMem.createOntology("Memory Model");
		ont.addVersionInfo("1.0");
		
		OntClass people = _modelMem.createClass(defaultNameSpace + "People");
		OntClass individual = _modelMem.createClass(defaultNameSpace + "Individual");
		people.addEquivalentClass(individual);
		
		OntClass friend = _modelMem.createClass(defaultNameSpace + "Friend");
		friend.addSubClass(people);
		
		OntProperty hasFriend = _modelMem.createObjectProperty(defaultNameSpace + "hasFriend");
		
		// Create restriction
		OntResource joe = _modelMem.createOntResource(defaultNameSpace + "Joe");
		OntResource joseph = _modelMem.createOntResource(defaultNameSpace + "Joseph");
		OntResource jane = _modelMem.createOntResource(defaultNameSpace + "Jane");
		
		joe.addSameAs(joseph);
		
		_modelMem.add(joe, hasFriend, jane);
	}
    
    private void searchAndNavigateData(){
    	// Search to find me
    	Resource me = _modelDB.getResource(defaultNameSpace + "me");
    	
    	// Navigate around me
    	StmtIterator iter = me.listProperties();
    	int count = 1;
    	while (iter.hasNext()){
    		System.out.println("Property " + count++ + ": " + iter.nextStatement().getObject());
    	}
    }
    
    private void getDataStatus(Model m){
     	// Test of empty
    	System.out.println("Model is " + (m.isEmpty()?"":"not" )+ " empty!");
    	// Size of model
    	System.out.println("Model Size: " + m.size());
     	// Supports Transactions?
    	System.out.println("Model does " + (m.supportsTransactions()?"":"not" )+ "support transactions.");
 
    	// List namespaces used within Model
    	NsIterator iter = m.listNameSpaces();
    	int count = 1;
    	while( iter.hasNext()){
    		System.out.println("Namespace  " + count++ + ": " + iter.nextNs());
    	}   		
    }
    
    private void getModelInfo(){
    	getDataStatus(_modelMem);   	
    	getDataStatus(_modelDB);
    }
	
    private void queryData(){
		StringBuffer queryStr = new StringBuffer();
		// Establish Prefixes
		//Set default Name space first
		queryStr.append("PREFIX people" + ": <" + defaultNameSpace + "> ");
		queryStr.append("PREFIX foaf" + ": <" + "http://xmlns.com/foaf/0.1/" + "> ");
		
		//Now add query
		queryStr.append(" select DISTINCT ?name where{ people:me foaf:name ?name  }");
		Query query = QueryFactory.create(queryStr.toString());
		QueryExecution qexec = QueryExecutionFactory.create(query, _modelDB);
		try {
		ResultSet response = qexec.execSelect();
		
		while( response.hasNext()){
			QuerySolution soln = response.nextSolution();
			RDFNode name = soln.get("?name");
			if( name != null ){
				System.out.println( "Hello to " + name );
			}
			else
				System.out.println("No Friends found!");
		 }
		} finally { qexec.close();}			  
    	
    	
    }
    
    private InfModel bindJenaInferenceReasoner(){
	    Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
	    reasoner = reasoner.bindSchema(_modelMem);
	    InfModel inferredFriends = ModelFactory.createInfModel(reasoner, _modelDB);	
	    return inferredFriends;
    }
    
    private InfModel bindJenaRuleReasoner(){
		String rules = "[emailChange: (?person <http://xmlns.com/foaf/0.1/mbox> ?email), " +
				"strConcat(?email, ?lit), regex( ?lit, '(.*@gmail.com)') -> " +
				"(?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> " +
				"<http://org.semwebprogramming/chapter2/people#GmailPerson>)]";

		Reasoner ruleReasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		ruleReasoner = ruleReasoner.bindSchema(_modelMem);
	    InfModel inferredFriends = ModelFactory.createInfModel(ruleReasoner, _modelDB);
	    return inferredFriends;
    }
    
    private InfModel bindPelletReasoner(){
		Reasoner reasoner = PelletReasonerFactory.theInstance().create();
	    reasoner = reasoner.bindSchema(_modelMem);
	    InfModel inferredFriends = ModelFactory.createInfModel(reasoner, _modelDB);

	    return inferredFriends;  	
    }
    
    private InfModel bindDigReasoner(){
    	System.out.println("Entering DIG reasoner");
    	
       	Resource conf = _modelMem.createResource();
        conf.addProperty(ReasonerVocabulary.EXT_REASONER_URL, _modelMem.createResource("http://localhost:8081")	);
        
       	DIGReasonerFactory drf = (DIGReasonerFactory) ReasonerRegistry.theRegistry().getFactory(DIGReasonerFactory.URI);
        DIGReasoner r = (DIGReasoner)drf.create(conf);
       
    	OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
    	spec.setReasoner(r);
    	
    	OntModel m = ModelFactory.createOntologyModel(spec,null);
    	m.add(_modelMem);
    	
    	StmtIterator i = m.listStatements(null, OWL.equivalentClass,OWL.Nothing);
    	while(i.hasNext()){
    		System.out.println("Class " + i.nextStatement().getSubject() + " is unsatisfiable");
    	}
    	return m;
     }
    
    private void criticalRegionWrite(){
    	
    	// First just start the extra thread in parallel
    	JenaModelReader jenaT = new JenaModelReader(_modelMem);
    	Thread t = new Thread( jenaT);
    	t.start();
    	
    	try {
    	_modelMem.enterCriticalSection(Lock.WRITE);
    	try {
    		System.out.println("Going to Sleep with LOCK");
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		    e.getMessage();
			//e.printStackTrace();
		}
    	}
    	finally {  // make sure you always give it up
    	_modelMem.leaveCriticalSection();
    	System.out.println("Leaving Critical Section");
    	}
    }
    
	private void clearAndCloseData(){
		_modelMem.removeAll();
		_modelDB.removeAll();
	   	System.out.println("Closing Models");
	   	_modelMem.close();
	    _modelDB.close();
	}
	
	private void transactionModel() throws BackingStoreException {
		try {
		if (_modelDB.supportsTransactions() != true){
			BackingStoreException exc = 
				 new BackingStoreException("Model does not support transactions");
			throw exc;
		}
		_modelDB.getGraph().getTransactionHandler().begin();
		// Do transaction stuff
		_modelDB.getGraph().getTransactionHandler().commit();
		}
		catch (Exception e) {
			System.out.println("Transaction aborted due to " + e.getMessage());
			_modelDB.getGraph().getTransactionHandler().abort();
		}
	}
	
	private void setEventListener(){
		_modelDB.register(_listenerDB);
		_modelMem.register(_listenerMem);
		
	}
	
	public class JenaListener extends ObjectListener {
		String modelName=null;
		
		JenaListener( Model m, String name){
			modelName = name;
		}
		
		public void notifyEvent(Model m, Object o){
			System.out.println(modelName + ": Event Occured");
			
		}
		public void addedStatement(Statement s){
			System.out.println(modelName + ": Statement added");
		}
		
	}
}

