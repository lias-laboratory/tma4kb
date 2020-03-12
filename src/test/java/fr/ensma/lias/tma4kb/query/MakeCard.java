package fr.ensma.lias.tma4kb.query;

import java.io.IOException;

import org.junit.Test;

import fr.ensma.lias.tma4kb.cardinalities.CardinalitiesMakeConfig;

public class MakeCard {
	
	@Test
	public static void MakeConfigTest(String[] args) throws IOException {
		CardinalitiesMakeConfig c = new CardinalitiesMakeConfig("src/main/resources/dbpcard.config", "src/test/resources/globalcardinalities/part-00000");
		c.makeGlobalConfig();
	}

}
