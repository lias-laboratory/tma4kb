package fr.ensma.lias.tma4kb.cardinalities;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:cardinalities.config")
public interface CardinalitiesConfig extends Config {
	/**
	 * This interface creates a correspondence between fields in a .config file and functions used in the project 
	 */
	
	@Key("type.max")
	 Integer typeMax();
	@Key("age.max")
	 Integer ageMax();
	@Key("nationality.max")
	 Integer nationalityMax();
	@Key("advisor.max")
	 Integer advisorMax();
	@Key("teacherOf.max")
	 Integer teacherOfMax();
	
	@Key("FullProfessortype.max")
	 Integer FullProfessortypeMax();
	@Key("FullProfessorage.max")
	 Integer FullProfessorageMax();
	@Key("FullProfessornationality.max")
	 Integer FullProfessornationalityMax();
	@Key("FullProfessoradvisor.max")
	 Integer FullProfessoradvisorMax();
	@Key("FullProfessorteacherOf.max")
	 Integer FullProfessorteacherOfMax();
	
	@Key("Studenttype.max")
	 Integer StudenttypeMax();
	@Key("Studentage.max")
	 Integer StudentageMax();
	@Key("Studentnationality.max")
	 Integer StudentnationalityMax();
	@Key("Studentadvisor.max")
	 Integer StudentadvisorMax();
	@Key("StudentteacherOf.max")
	 Integer StudentteacherOfMax();
	
	@Key("type.domain")
	 String typeDomain();
	@Key("age.domain")
	 String ageDomain();
	@Key("nationality.domain")
	 String nationalityDomain();
	@Key("advisor.domain")
	 String advisorDomain();
	@Key("teacherOf.domain")
	 String teacherOfDomain();
	
}
