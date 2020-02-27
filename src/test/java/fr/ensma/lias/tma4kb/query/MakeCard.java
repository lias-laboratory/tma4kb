package fr.ensma.lias.tma4kb.query;

import java.io.IOException;

import org.junit.Test;

import fr.ensma.lias.tma4kb.cardinalities.CardinalitiesMakeConfig;

public class MakeCard {
	
	@Test
	public void GlobalMakeConfigTest() throws IOException {
		CardinalitiesMakeConfig c = new CardinalitiesMakeConfig("C:/Doctorat/TMA/", "src/test/resources/");
		c.makeGlobalConfig();
	}
	
	@Test
	public void LocalMakeConfigTest() throws IOException {
		CardinalitiesMakeConfig c = new CardinalitiesMakeConfig("C:/Doctorat/TMA/", "src/test/resources/");
		c.makeLocalConfig();
	}
	
	@Test
	public void CSMakeConfigTest() throws IOException {
		CardinalitiesMakeConfig c = new CardinalitiesMakeConfig("C:/Doctorat/TMA/", "src/test/resources/");
		c.makeCSConfig();
	}
	
	@Test
	public void WDMakeConfigTest() throws IOException {
		CardinalitiesMakeConfig c = new CardinalitiesMakeConfig("C:/Doctorat/TMA/WD100", "src/test/resources/");
		c.makeGlobalConfig();
	}

}
