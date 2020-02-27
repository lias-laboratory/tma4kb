package fr.ensma.lias.tma4kb.query;

import java.io.IOException;

import org.junit.Test;

import fr.ensma.lias.tma4kb.cardinalities.CardinalitiesMakeConfig;

public class MakeCard {
	
	
	@Test
	public void MakeConfigTest() throws IOException {
		CardinalitiesMakeConfig c = new CardinalitiesMakeConfig("src/main/resources/", "src/test/resources/");
		c.makeGlobalConfig();
	}

}
