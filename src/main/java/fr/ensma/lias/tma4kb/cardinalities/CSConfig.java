package fr.ensma.lias.tma4kb.cardinalities;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:cardinalitiesCS.config")
public interface CSConfig extends Config { 

	@Key("Characteristic.set")
	String CharacteristicSet();

}