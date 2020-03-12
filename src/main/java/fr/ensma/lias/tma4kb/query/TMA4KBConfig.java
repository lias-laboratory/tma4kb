package fr.ensma.lias.tma4kb.query;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

/**
 * @author Mickael BARON (baron@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
@LoadPolicy(LoadType.MERGE)
@Sources({ "system:env", "classpath:TMA4KBConfig.properties" })
public interface TMA4KBConfig extends Config {

	@Key("jenatdb_repository")
	String jenaRepository();

	@Key("jdbc.database")
	String jdbcDatabase();

	@Key("jdbc.url")
	String jdbcUrl();

	@Key("jdbc.driver")
	String jdbcDriver();

	@Key("jdbc.login")
	String jdbcLogin();

	@Key("jdbc.password")
	String jdbcPassword();

	@Key("sparqlendpoint.url")
	String sparqlendpointUrl();

	@Key("sparqlendpoint.defaultgraphuri")
	String sparqlendpointDefaultGraphURI();

}
