package fr.ensma.lias.tma4kb.triplestore.jenatdb;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;

import fr.ensma.lias.tma4kb.query.Session;



/**
 * @author Stephane JEAN
 */
public class JenaQueryHelper extends fr.ensma.lias.tma4kb.query.SPARQLQueryHelper {

	/**
	 * Creates a JenaQueryHelper for query q
	 * @param q the query that this helper uses
	 */
    public JenaQueryHelper(fr.ensma.lias.tma4kb.query.Query q) {
    	super(q);
    }
    
	@Override
	public boolean executeQuery(Session session, int k) {
		
		int i =0;
		String sparqlQueryString = toNativeQuery();
		//System.out.println(sparqlQueryString);
		org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, ((JenaSession) session).getDataset());
		// add the filter function to the Query Execution
		 //qexec.getContext().set(SystemTDB.symTupleFilter, createFilter(((JenaTDBGraphSession) session).getDataset(), alpha));

		// ResultSetFormatter.out(qexec.execSelect());
		 
		ResultSet results = qexec.execSelect();
		
		(session).setExecutedQueryCount((session).getExecutedQueryCount() + 1);
		//boolean res = !results.hasNext();
		while(results.hasNext()){
			i++;
			results.next();
		}	
		//System.out.println("results " + i + " >= " + k);
		qexec.close();
	
			// long end = System.currentTimeMillis();
		 //if((((float) (end - begin)) / 1000f)>1){	
		//	 System.out.println(((float) (end - begin)) / 1000f);
		//System.out.println(sparqlQueryString);}
		//  if(!res) System.out.println("success"); else		  System.out.println("fails");
		   
		return i > k;
	}
	
}
