package fr.ensma.lias.tma4kb.triplestore.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;

import fr.ensma.lias.tma4kb.exception.TripleStoreException;
import fr.ensma.lias.tma4kb.query.AbstractQueryFactory;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.Session;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 */
public class HSQLDBQueryFactory extends AbstractQueryFactory {

    @Override
    public Query createQuery(String rdfQuery) {
    	return new HSQLDBQuery(this, rdfQuery);
    }
	

	@Override
	public Session createSession() {
		try {
			Class.forName(this.getConfig().jdbcDriver());
			Connection cnxJDBC = DriverManager.getConnection(this.getConfig().jdbcUrl(), this.getConfig().jdbcLogin(),
					this.getConfig().jdbcPassword());
			return new HSQLDBSession(cnxJDBC);
		} catch (Exception e) {
			System.err.println("Unable to create session");
			e.printStackTrace();
			throw new TripleStoreException(e.getMessage());
		}
	}

}
