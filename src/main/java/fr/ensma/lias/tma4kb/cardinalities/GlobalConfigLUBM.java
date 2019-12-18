package fr.ensma.lias.tma4kb.cardinalities;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:cardinalitiesGlobalLUBM.config")
public interface GlobalConfigLUBM extends Config { 

	 @Key("type.min")
	 Integer typeMin();
	 @Key("type.max")
	 Integer typeMax();
	 @Key("name.min")
	 Integer nameMin();
	 @Key("name.max")
	 Integer nameMax();
	 @Key("subOrganizationOf.min")
	 Integer subOrganizationOfMin();
	 @Key("subOrganizationOf.max")
	 Integer subOrganizationOfMax();
	 @Key("worksFor.min")
	 Integer worksForMin();
	 @Key("worksFor.max")
	 Integer worksForMax();
	 @Key("degreeFrom.min")
	 Integer degreeFromMin();
	 @Key("degreeFrom.max")
	 Integer degreeFromMax();
	 @Key("doctoralDegreeFrom.min")
	 Integer doctoralDegreeFromMin();
	 @Key("doctoralDegreeFrom.max")
	 Integer doctoralDegreeFromMax();
	 @Key("emailAddress.min")
	 Integer emailAddressMin();
	 @Key("emailAddress.max")
	 Integer emailAddressMax();
	 @Key("mastersDegreeFrom.min")
	 Integer mastersDegreeFromMin();
	 @Key("mastersDegreeFrom.max")
	 Integer mastersDegreeFromMax();
	 @Key("memberOf.min")
	 Integer memberOfMin();
	 @Key("memberOf.max")
	 Integer memberOfMax();
	 @Key("researchInterest.min")
	 Integer researchInterestMin();
	 @Key("researchInterest.max")
	 Integer researchInterestMax();
	 @Key("teacherOf.min")
	 Integer teacherOfMin();
	 @Key("teacherOf.max")
	 Integer teacherOfMax();
	 @Key("telephone.min")
	 Integer telephoneMin();
	 @Key("telephone.max")
	 Integer telephoneMax();
	 @Key("undergraduateDegreeFrom.min")
	 Integer undergraduateDegreeFromMin();
	 @Key("undergraduateDegreeFrom.max")
	 Integer undergraduateDegreeFromMax();
	 @Key("headOf.min")
	 Integer headOfMin();
	 @Key("headOf.max")
	 Integer headOfMax();
	 @Key("takesCourse.min")
	 Integer takesCourseMin();
	 @Key("takesCourse.max")
	 Integer takesCourseMax();
	 @Key("advisor.min")
	 Integer advisorMin();
	 @Key("advisor.max")
	 Integer advisorMax();
	 @Key("teachingAssistantOf.min")
	 Integer teachingAssistantOfMin();
	 @Key("teachingAssistantOf.max")
	 Integer teachingAssistantOfMax();
	 @Key("publicationAuthor.min")
	 Integer publicationAuthorMin();
	 @Key("publicationAuthor.max")
	 Integer publicationAuthorMax();
	
}