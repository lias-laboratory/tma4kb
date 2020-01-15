package fr.ensma.lias.tma4kb.cardinalities;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:cardinalitiesLocal.config")
public interface LocalConfig extends Config { 

	@Key("Ontologylabel.max")
	String OntologylabelMax();

	@Key("ProfessoremailAddress.max")
	String ProfessoremailAddressMax();

	@Key("UndergraduateStudentmemberOf.max")
	String UndergraduateStudentmemberOfMax();

	@Key("PersonmemberOf.max")
	String PersonmemberOfMax();

	@Key("Ontologyimports.max")
	String OntologyimportsMax();

	@Key("RestrictionsomeValuesFrom.max")
	String RestrictionsomeValuesFromMax();

	@Key("Employeetelephone.max")
	String EmployeetelephoneMax();

	@Key("ResearchAssistantname.max")
	String ResearchAssistantnameMax();

	@Key("UndergraduateStudentemailAddress.max")
	String UndergraduateStudentemailAddressMax();

	@Key("AssistantProfessorundergraduateDegreeFrom.max")
	String AssistantProfessorundergraduateDegreeFromMax();

	@Key("Studentname.max")
	String StudentnameMax();

	@Key("GraduateCoursename.max")
	String GraduateCoursenameMax();

	@Key("PersonresearchInterest.max")
	String PersonresearchInterestMax();

	@Key("EmployeeresearchInterest.max")
	String EmployeeresearchInterestMax();

	@Key("FullProfessortelephone.max")
	String FullProfessortelephoneMax();

	@Key("PersontakesCourse.max")
	String PersontakesCourseMax();

	@Key("FacultydoctoralDegreeFrom.max")
	String FacultydoctoralDegreeFromMax();

	@Key("ProfessordegreeFrom.max")
	String ProfessordegreeFromMax();

	@Key("AssociateProfessormastersDegreeFrom.max")
	String AssociateProfessormastersDegreeFromMax();

	@Key("PersonteachingAssistantOf.max")
	String PersonteachingAssistantOfMax();

	@Key("ProfessormastersDegreeFrom.max")
	String ProfessormastersDegreeFromMax();

	@Key("AssistantProfessormastersDegreeFrom.max")
	String AssistantProfessormastersDegreeFromMax();

	@Key("StudentemailAddress.max")
	String StudentemailAddressMax();

	@Key("GraduateStudentteachingAssistantOf.max")
	String GraduateStudentteachingAssistantOfMax();

	@Key("TransitivePropertylabel.max")
	String TransitivePropertylabelMax();

	@Key("Professortelephone.max")
	String ProfessortelephoneMax();

	@Key("Ontologycomment.max")
	String OntologycommentMax();

	@Key("ProfessorheadOf.max")
	String ProfessorheadOfMax();

	@Key("FullProfessorworksFor.max")
	String FullProfessorworksForMax();

	@Key("ResearchAssistantemailAddress.max")
	String ResearchAssistantemailAddressMax();

	@Key("Departmentname.max")
	String DepartmentnameMax();

	@Key("FacultymastersDegreeFrom.max")
	String FacultymastersDegreeFromMax();

	@Key("ResearchAssistantundergraduateDegreeFrom.max")
	String ResearchAssistantundergraduateDegreeFromMax();

	@Key("TeachingAssistanttakesCourse.max")
	String TeachingAssistanttakesCourseMax();

	@Key("FullProfessorname.max")
	String FullProfessornameMax();

	@Key("AssistantProfessorname.max")
	String AssistantProfessornameMax();

	@Key("StudentmemberOf.max")
	String StudentmemberOfMax();

	@Key("DatatypePropertydomain.max")
	String DatatypePropertydomainMax();

	@Key("UndergraduateStudenttelephone.max")
	String UndergraduateStudenttelephoneMax();

	@Key("PersonworksFor.max")
	String PersonworksForMax();

	@Key("PersonheadOf.max")
	String PersonheadOfMax();

	@Key("Organizationname.max")
	String OrganizationnameMax();

	@Key("EmployeeheadOf.max")
	String EmployeeheadOfMax();

	@Key("FacultyundergraduateDegreeFrom.max")
	String FacultyundergraduateDegreeFromMax();

	@Key("PersonundergraduateDegreeFrom.max")
	String PersonundergraduateDegreeFromMax();

	@Key("PersondoctoralDegreeFrom.max")
	String PersondoctoralDegreeFromMax();

	@Key("Universityname.max")
	String UniversitynameMax();

	@Key("Employeename.max")
	String EmployeenameMax();

	@Key("LecturerdegreeFrom.max")
	String LecturerdegreeFromMax();

	@Key("PersonemailAddress.max")
	String PersonemailAddressMax();

	@Key("Personname.max")
	String PersonnameMax();

	@Key("PublicationpublicationAuthor.max")
	String PublicationpublicationAuthorMax();

	@Key("TeachingAssistantemailAddress.max")
	String TeachingAssistantemailAddressMax();

	@Key("FullProfessoremailAddress.max")
	String FullProfessoremailAddressMax();

	@Key("GraduateStudenttakesCourse.max")
	String GraduateStudenttakesCourseMax();

	@Key("EmployeedoctoralDegreeFrom.max")
	String EmployeedoctoralDegreeFromMax();

	@Key("ResearchAssistantdegreeFrom.max")
	String ResearchAssistantdegreeFromMax();

	@Key("ProfessorresearchInterest.max")
	String ProfessorresearchInterestMax();

	@Key("LecturerdoctoralDegreeFrom.max")
	String LecturerdoctoralDegreeFromMax();

	@Key("FacultyteacherOf.max")
	String FacultyteacherOfMax();

	@Key("Workname.max")
	String WorknameMax();

	@Key("Persontelephone.max")
	String PersontelephoneMax();

	@Key("GraduateStudentname.max")
	String GraduateStudentnameMax();

	@Key("AssociateProfessoremailAddress.max")
	String AssociateProfessoremailAddressMax();

	@Key("AssistantProfessortelephone.max")
	String AssistantProfessortelephoneMax();

	@Key("DatatypePropertylabel.max")
	String DatatypePropertylabelMax();

	@Key("LecturerteacherOf.max")
	String LecturerteacherOfMax();

	@Key("Studenttelephone.max")
	String StudenttelephoneMax();

	@Key("ResearchAssistantadvisor.max")
	String ResearchAssistantadvisorMax();

	@Key("AssistantProfessorresearchInterest.max")
	String AssistantProfessorresearchInterestMax();

	@Key("ObjectPropertylabel.max")
	String ObjectPropertylabelMax();

	@Key("TeachingAssistantadvisor.max")
	String TeachingAssistantadvisorMax();

	@Key("StudenttakesCourse.max")
	String StudenttakesCourseMax();

	@Key("ObjectPropertysubPropertyOf.max")
	String ObjectPropertysubPropertyOfMax();

	@Key("PersonmastersDegreeFrom.max")
	String PersonmastersDegreeFromMax();

	@Key("RestrictiononProperty.max")
	String RestrictiononPropertyMax();

	@Key("FacultyresearchInterest.max")
	String FacultyresearchInterestMax();

	@Key("AssociateProfessorteacherOf.max")
	String AssociateProfessorteacherOfMax();

	@Key("AssistantProfessormemberOf.max")
	String AssistantProfessormemberOfMax();

	@Key("FullProfessordoctoralDegreeFrom.max")
	String FullProfessordoctoralDegreeFromMax();

	@Key("AssociateProfessorundergraduateDegreeFrom.max")
	String AssociateProfessorundergraduateDegreeFromMax();

	@Key("ProfessormemberOf.max")
	String ProfessormemberOfMax();

	@Key("EmployeeundergraduateDegreeFrom.max")
	String EmployeeundergraduateDegreeFromMax();

	@Key("Coursename.max")
	String CoursenameMax();

	@Key("TransitivePropertyrange.max")
	String TransitivePropertyrangeMax();

	@Key("GraduateStudentemailAddress.max")
	String GraduateStudentemailAddressMax();

	@Key("EmployeeteacherOf.max")
	String EmployeeteacherOfMax();

	@Key("EmployeedegreeFrom.max")
	String EmployeedegreeFromMax();

	@Key("FacultydegreeFrom.max")
	String FacultydegreeFromMax();

	@Key("TeachingAssistantname.max")
	String TeachingAssistantnameMax();

	@Key("ClasssubClassOf.max")
	String ClasssubClassOfMax();

	@Key("AssociateProfessordegreeFrom.max")
	String AssociateProfessordegreeFromMax();

	@Key("UndergraduateStudentname.max")
	String UndergraduateStudentnameMax();

	@Key("FullProfessorteacherOf.max")
	String FullProfessorteacherOfMax();

	@Key("ObjectPropertydomain.max")
	String ObjectPropertydomainMax();

	@Key("FacultymemberOf.max")
	String FacultymemberOfMax();

	@Key("Professorname.max")
	String ProfessornameMax();

	@Key("FullProfessormastersDegreeFrom.max")
	String FullProfessormastersDegreeFromMax();

	@Key("LecturermemberOf.max")
	String LecturermemberOfMax();

	@Key("AssociateProfessorresearchInterest.max")
	String AssociateProfessorresearchInterestMax();

	@Key("AssistantProfessorworksFor.max")
	String AssistantProfessorworksForMax();

	@Key("OrganizationsubOrganizationOf.max")
	String OrganizationsubOrganizationOfMax();

	@Key("AssistantProfessoremailAddress.max")
	String AssistantProfessoremailAddressMax();

	@Key("ProfessorteacherOf.max")
	String ProfessorteacherOfMax();

	@Key("ProfessorworksFor.max")
	String ProfessorworksForMax();

	@Key("ProfessordoctoralDegreeFrom.max")
	String ProfessordoctoralDegreeFromMax();

	@Key("TeachingAssistantmemberOf.max")
	String TeachingAssistantmemberOfMax();

	@Key("UndergraduateStudenttakesCourse.max")
	String UndergraduateStudenttakesCourseMax();

	@Key("TeachingAssistantteachingAssistantOf.max")
	String TeachingAssistantteachingAssistantOfMax();

	@Key("OntologyversionInfo.max")
	String OntologyversionInfoMax();

	@Key("TeachingAssistantundergraduateDegreeFrom.max")
	String TeachingAssistantundergraduateDegreeFromMax();

	@Key("EmployeeemailAddress.max")
	String EmployeeemailAddressMax();

	@Key("Studentadvisor.max")
	String StudentadvisorMax();

	@Key("ResearchGroupsubOrganizationOf.max")
	String ResearchGroupsubOrganizationOfMax();

	@Key("ResearchAssistantmemberOf.max")
	String ResearchAssistantmemberOfMax();

	@Key("EmployeememberOf.max")
	String EmployeememberOfMax();

	@Key("DepartmentsubOrganizationOf.max")
	String DepartmentsubOrganizationOfMax();

	@Key("FacultyworksFor.max")
	String FacultyworksForMax();

	@Key("GraduateStudentadvisor.max")
	String GraduateStudentadvisorMax();

	@Key("Classlabel.max")
	String ClasslabelMax();

	@Key("LecturerworksFor.max")
	String LecturerworksForMax();

	@Key("FullProfessorheadOf.max")
	String FullProfessorheadOfMax();

	@Key("TeachingAssistantdegreeFrom.max")
	String TeachingAssistantdegreeFromMax();

	@Key("ProfessorundergraduateDegreeFrom.max")
	String ProfessorundergraduateDegreeFromMax();

	@Key("EmployeemastersDegreeFrom.max")
	String EmployeemastersDegreeFromMax();

	@Key("Personadvisor.max")
	String PersonadvisorMax();

	@Key("AssociateProfessormemberOf.max")
	String AssociateProfessormemberOfMax();

	@Key("AssistantProfessordoctoralDegreeFrom.max")
	String AssistantProfessordoctoralDegreeFromMax();

	@Key("FullProfessorundergraduateDegreeFrom.max")
	String FullProfessorundergraduateDegreeFromMax();

	@Key("ResearchAssistanttelephone.max")
	String ResearchAssistanttelephoneMax();

	@Key("ObjectPropertyrange.max")
	String ObjectPropertyrangeMax();

	@Key("AssistantProfessordegreeFrom.max")
	String AssistantProfessordegreeFromMax();

	@Key("PersonteacherOf.max")
	String PersonteacherOfMax();

	@Key("Publicationname.max")
	String PublicationnameMax();

	@Key("Facultytelephone.max")
	String FacultytelephoneMax();

	@Key("TransitivePropertydomain.max")
	String TransitivePropertydomainMax();

	@Key("AssociateProfessorname.max")
	String AssociateProfessornameMax();

	@Key("EmployeeworksFor.max")
	String EmployeeworksForMax();

	@Key("FullProfessordegreeFrom.max")
	String FullProfessordegreeFromMax();

	@Key("Facultyname.max")
	String FacultynameMax();

	@Key("FacultyemailAddress.max")
	String FacultyemailAddressMax();

	@Key("AssistantProfessorteacherOf.max")
	String AssistantProfessorteacherOfMax();

	@Key("LecturermastersDegreeFrom.max")
	String LecturermastersDegreeFromMax();

	@Key("ResearchAssistanttakesCourse.max")
	String ResearchAssistanttakesCourseMax();

	@Key("Lecturertelephone.max")
	String LecturertelephoneMax();

	@Key("TeachingAssistanttelephone.max")
	String TeachingAssistanttelephoneMax();

	@Key("GraduateStudentmemberOf.max")
	String GraduateStudentmemberOfMax();

	@Key("GraduateStudentundergraduateDegreeFrom.max")
	String GraduateStudentundergraduateDegreeFromMax();

	@Key("AssociateProfessorworksFor.max")
	String AssociateProfessorworksForMax();

	@Key("GraduateStudenttelephone.max")
	String GraduateStudenttelephoneMax();

	@Key("FullProfessorresearchInterest.max")
	String FullProfessorresearchInterestMax();

	@Key("AssociateProfessortelephone.max")
	String AssociateProfessortelephoneMax();

	@Key("AssociateProfessordoctoralDegreeFrom.max")
	String AssociateProfessordoctoralDegreeFromMax();

	@Key("FacultyheadOf.max")
	String FacultyheadOfMax();

	@Key("Lecturername.max")
	String LecturernameMax();

	@Key("PersondegreeFrom.max")
	String PersondegreeFromMax();

	@Key("GraduateStudentdegreeFrom.max")
	String GraduateStudentdegreeFromMax();

	@Key("FullProfessormemberOf.max")
	String FullProfessormemberOfMax();

	@Key("LecturerundergraduateDegreeFrom.max")
	String LecturerundergraduateDegreeFromMax();

	@Key("LectureremailAddress.max")
	String LectureremailAddressMax();

	@Key("UndergraduateStudentadvisor.max")
	String UndergraduateStudentadvisorMax();

	@Key("ObjectPropertyinverseOf.max")
	String ObjectPropertyinverseOfMax();

	@Key("ClassintersectionOf.max")
	String ClassintersectionOfMax();

	@Key("publicationResearch.domain")
	String publicationResearchDomain();

	@Key("teachingAssistantOf.domain")
	String teachingAssistantOfDomain();

	@Key("emailAddress.domain")
	String emailAddressDomain();

	@Key("subOrganizationOf.domain")
	String subOrganizationOfDomain();

	@Key("listedCourse.domain")
	String listedCourseDomain();

	@Key("undergraduateDegreeFrom.domain")
	String undergraduateDegreeFromDomain();

	@Key("teacherOf.domain")
	String teacherOfDomain();

	@Key("researchProject.domain")
	String researchProjectDomain();

	@Key("doctoralDegreeFrom.domain")
	String doctoralDegreeFromDomain();

	@Key("affiliateOf.domain")
	String affiliateOfDomain();

	@Key("orgPublication.domain")
	String orgPublicationDomain();

	@Key("softwareVersion.domain")
	String softwareVersionDomain();

	@Key("publicationAuthor.domain")
	String publicationAuthorDomain();

	@Key("degreeFrom.domain")
	String degreeFromDomain();

	@Key("mastersDegreeFrom.domain")
	String mastersDegreeFromDomain();

	@Key("age.domain")
	String ageDomain();

	@Key("softwareDocumentation.domain")
	String softwareDocumentationDomain();

	@Key("hasAlumnus.domain")
	String hasAlumnusDomain();

	@Key("telephone.domain")
	String telephoneDomain();

	@Key("affiliatedOrganizationOf.domain")
	String affiliatedOrganizationOfDomain();

	@Key("tenured.domain")
	String tenuredDomain();

	@Key("advisor.domain")
	String advisorDomain();

	@Key("publicationDate.domain")
	String publicationDateDomain();

	@Key("member.domain")
	String memberDomain();

	@Key("title.domain")
	String titleDomain();

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