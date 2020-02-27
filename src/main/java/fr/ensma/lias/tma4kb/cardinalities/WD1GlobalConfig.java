package fr.ensma.lias.tma4kb.cardinalities;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:WD1cardinalitiesGlobal.config")
public interface WD1GlobalConfig extends Config { 

	@Key("aggregateRating.max")
	String aggregateRatingMax();

	@Key("hits.max")
	String hitsMax();

	@Key("validThrough.max")
	String validThroughMax();

	@Key("legalName.max")
	String legalNameMax();

	@Key("serialNumber.max")
	String serialNumberMax();

	@Key("composer.max")
	String composerMax();

	@Key("expires.max")
	String expiresMax();

	@Key("director.max")
	String directorMax();

	@Key("printPage.max")
	String printPageMax();

	@Key("contactPoint.max")
	String contactPointMax();

	@Key("record_number.max")
	String record_numberMax();

	@Key("release.max")
	String releaseMax();

	@Key("description.max")
	String descriptionMax();

	@Key("nationality.max")
	String nationalityMax();

	@Key("datePublished.max")
	String datePublishedMax();

	@Key("type.max")
	String typeMax();

	@Key("publisher.max")
	String publisherMax();

	@Key("duration.max")
	String durationMax();

	@Key("likes.max")
	String likesMax();

	@Key("language.max")
	String languageMax();

	@Key("purchaseFor.max")
	String purchaseForMax();

	@Key("familyName.max")
	String familyNameMax();

	@Key("conductor.max")
	String conductorMax();

	@Key("email.max")
	String emailMax();

	@Key("printSection.max")
	String printSectionMax();

	@Key("numberOfPages.max")
	String numberOfPagesMax();

	@Key("telephone.max")
	String telephoneMax();

	@Key("openingHours.max")
	String openingHoursMax();

	@Key("purchaseDate.max")
	String purchaseDateMax();

	@Key("title.max")
	String titleMax();

	@Key("validFrom.max")
	String validFromMax();

	@Key("priceValidUntil.max")
	String priceValidUntilMax();

	@Key("parentCountry.max")
	String parentCountryMax();

	@Key("eligibleRegion.max")
	String eligibleRegionMax();

	@Key("url.max")
	String urlMax();

	@Key("performer.max")
	String performerMax();

	@Key("Location.max")
	String LocationMax();

	@Key("givenName.max")
	String givenNameMax();

	@Key("jobTitle.max")
	String jobTitleMax();

	@Key("printColumn.max")
	String printColumnMax();

	@Key("performed_in.max")
	String performed_inMax();

	@Key("trailer.max")
	String trailerMax();

	@Key("author.max")
	String authorMax();

	@Key("homepage.max")
	String homepageMax();

	@Key("price.max")
	String priceMax();

	@Key("caption.max")
	String captionMax();

	@Key("faxNumber.max")
	String faxNumberMax();

	@Key("makesPurchase.max")
	String makesPurchaseMax();

	@Key("text.max")
	String textMax();

	@Key("artist.max")
	String artistMax();

	@Key("contentSize.max")
	String contentSizeMax();

	@Key("producer.max")
	String producerMax();

	@Key("movement.max")
	String movementMax();

	@Key("keywords.max")
	String keywordsMax();

	@Key("contentRating.max")
	String contentRatingMax();

	@Key("opus.max")
	String opusMax();

	@Key("totalVotes.max")
	String totalVotesMax();

	@Key("userId.max")
	String userIdMax();

	@Key("includes.max")
	String includesMax();

	@Key("name.max")
	String nameMax();

	@Key("reviewer.max")
	String reviewerMax();

	@Key("employee.max")
	String employeeMax();

	@Key("offers.max")
	String offersMax();

	@Key("follows.max")
	String followsMax();

	@Key("eligibleQuantity.max")
	String eligibleQuantityMax();

	@Key("age.max")
	String ageMax();

	@Key("isbn.max")
	String isbnMax();

	@Key("rating.max")
	String ratingMax();

	@Key("hasGenre.max")
	String hasGenreMax();

	@Key("subscribes.max")
	String subscribesMax();

	@Key("tag.max")
	String tagMax();

	@Key("wordCount.max")
	String wordCountMax();

	@Key("actor.max")
	String actorMax();

	@Key("award.max")
	String awardMax();

	@Key("gender.max")
	String genderMax();

	@Key("printEdition.max")
	String printEditionMax();

	@Key("bookEdition.max")
	String bookEditionMax();

	@Key("editor.max")
	String editorMax();

	@Key("hasReview.max")
	String hasReviewMax();

	@Key("friendOf.max")
	String friendOfMax();

	@Key("birthDate.max")
	String birthDateMax();

	@Key("paymentAccepted.max")
	String paymentAcceptedMax();

}