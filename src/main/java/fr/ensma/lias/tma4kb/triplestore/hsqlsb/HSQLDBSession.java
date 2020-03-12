package fr.ensma.lias.tma4kb.triplestore.hsqlsb;

import java.sql.Connection;

import fr.ensma.lias.tma4kb.query.AbstractSession;

/**
 * @author Stephane JEAN
 */
public class HSQLDBSession extends AbstractSession {

	private Connection connection;

	public HSQLDBSession(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void close() throws Exception {
		if (connection != null) {
			connection.close();
		}
	}

	public Connection getConnection() {
		return connection;
	}
}
