package fr.ensma.lias.tma4kb.cardinalities;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:cardinalitiesGlobal.config")
public interface GlobalConfig extends Config { 

	@Key("teachingAssistantOf.max")
	String teachingAssistantOfMax();

	@Key("subOrganizationOf.max")
	String subOrganizationOfMax();

	@Key("rest.max")
	String restMax();

	@Key("teacherOf.max")
	String teacherOfMax();

	@Key("onProperty.max")
	String onPropertyMax();

	@Key("researchInterest.max")
	String researchInterestMax();

	@Key("type.max")
	String typeMax();

	@Key("publicationAuthor.max")
	String publicationAuthorMax();

	@Key("degreeFrom.max")
	String degreeFromMax();

	@Key("someValuesFrom.max")
	String someValuesFromMax();

	@Key("comment.max")
	String commentMax();

	@Key("imports.max")
	String importsMax();

	@Key("first.max")
	String firstMax();

	@Key("emailAddress.max")
	String emailAddressMax();

	@Key("versionInfo.max")
	String versionInfoMax();

	@Key("headOf.max")
	String headOfMax();

	@Key("takesCourse.max")
	String takesCourseMax();

	@Key("name.max")
	String nameMax();

	@Key("memberOf.max")
	String memberOfMax();

	@Key("undergraduateDegreeFrom.max")
	String undergraduateDegreeFromMax();

	@Key("range.max")
	String rangeMax();

	@Key("doctoralDegreeFrom.max")
	String doctoralDegreeFromMax();

	@Key("worksFor.max")
	String worksForMax();

	@Key("subClassOf.max")
	String subClassOfMax();

	@Key("mastersDegreeFrom.max")
	String mastersDegreeFromMax();

	@Key("intersectionOf.max")
	String intersectionOfMax();

	@Key("domain.max")
	String domainMax();

	@Key("telephone.max")
	String telephoneMax();

	@Key("inverseOf.max")
	String inverseOfMax();

	@Key("subPropertyOf.max")
	String subPropertyOfMax();

	@Key("label.max")
	String labelMax();

	@Key("advisor.max")
	String advisorMax();

}