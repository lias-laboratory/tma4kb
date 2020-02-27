package fr.ensma.lias.tma4kb.execution;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.aeonbits.owner.ConfigFactory;

import fr.ensma.lias.tma4kb.query.TMA4KBConfig;

public class MyTest {
	public static void main(String[] args) throws IOException {
		TMA4KBConfig test = ConfigFactory.create(TMA4KBConfig.class);
		System.out.println(test.jenaRepository());
		
		Properties properties = new Properties();
		
		InputStream input = MyTest.class.getResourceAsStream("/wd100cardglobal.config");
	      try{

	         properties.load(input);
	         	      }

	              finally{

	         input.close();

	      } 
		
		System.out.println(properties.get("email.max"));
	}
}
