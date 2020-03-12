package fr.ensma.lias.tma4kb.triplestore.hsqlsb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import fr.ensma.lias.tma4kb.exception.TripleStoreException;
import fr.ensma.lias.tma4kb.query.AbstractQuery;
import fr.ensma.lias.tma4kb.query.Session;
import fr.ensma.lias.tma4kb.query.TriplePattern;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 */
public class HSQLDBQuery extends AbstractQuery {

	public HSQLDBQuery(HSQLDBQueryFactory factory, String query) {
		super(factory, query);
	}

	@Override
	public int isFailing(Session session, int k) {
		try {
			Statement stmt = ((HSQLDBSession) session).getConnection().createStatement();
			ResultSet rset = stmt.executeQuery(toNativeQuery());
			int i = 0;
			session.setExecutedQueryCount(session.getExecutedQueryCount() + 1);
			// This code is probably not efficient
			// since it's only used for test issue, it's fine
			while (rset.next()) { // Stop once more than k answers are found : && i<=k) { the result is inaccurate
									// but boolean failure is correct
				i++;
			}
			rset.close();
			stmt.close();
			return i;
		} catch (SQLException e) {
			System.err.println("Unable to execute the query: " + e.getMessage());
			e.printStackTrace();
			throw new TripleStoreException();
		}
	}

	private String toNativeQuery() {
		String res = "select * from ";
		List<TriplePattern> triplePatterns = getTriplePatterns();
		for (int i = 0; i < triplePatterns.size(); i++) {
			if (i > 0)
				res += " NATURAL JOIN ";
			res += "(" + triplePatterns.get(i).toSQL() + ") " + "t" + i;
		}
		return res;
	}

}
