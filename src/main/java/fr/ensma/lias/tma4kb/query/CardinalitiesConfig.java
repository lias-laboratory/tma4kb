package fr.ensma.lias.tma4kb.query;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:cardinalities.config")
public interface CardinalitiesConfig extends Config {
	
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
	
}
