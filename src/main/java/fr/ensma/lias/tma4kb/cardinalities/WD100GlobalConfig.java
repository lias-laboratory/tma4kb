package fr.ensma.lias.tma4kb.cardinalities;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("file:src/main/resources/WD100cardinalitiesGlobal.config")
public interface WD100GlobalConfig extends Config { 

	@Key("gender.max")
	String genderMax();

	@Key("makesPurchase.max")
	String makesPurchaseMax();

	@Key("type.max")
	String typeMax();

	@Key("trailer.max")
	String trailerMax();

	@Key("movement.max")
	String movementMax();

	@Key("director.max")
	String directorMax();

	@Key("eligibleRegion.max")
	String eligibleRegionMax();

	@Key("duration.max")
	String durationMax();

	@Key("printColumn.max")
	String printColumnMax();

	@Key("friendOf.max")
	String friendOfMax();

	@Key("printPage.max")
	String printPageMax();

	@Key("hits.max")
	String hitsMax();

	@Key("numberOfPages.max")
	String numberOfPagesMax();

	@Key("datePublished.max")
	String datePublishedMax();

	@Key("performer.max")
	String performerMax();

	@Key("expires.max")
	String expiresMax();

	@Key("name.max")
	String nameMax();

	@Key("opus.max")
	String opusMax();

	@Key("givenName.max")
	String givenNameMax();

	@Key("url.max")
	String urlMax();

	@Key("priceValidUntil.max")
	String priceValidUntilMax();

	@Key("serialNumber.max")
	String serialNumberMax();

	@Key("publisher.max")
	String publisherMax();

	@Key("offers.max")
	String offersMax();

	@Key("hasGenre.max")
	String hasGenreMax();

	@Key("jobTitle.max")
	String jobTitleMax();

	@Key("email.max")
	String emailMax();

	@Key("includes.max")
	String includesMax();

	@Key("release.max")
	String releaseMax();

	@Key("likes.max")
	String likesMax();

	@Key("parentCountry.max")
	String parentCountryMax();

	@Key("hasReview.max")
	String hasReviewMax();

	@Key("artist.max")
	String artistMax();

	@Key("composer.max")
	String composerMax();

	@Key("wordCount.max")
	String wordCountMax();

	@Key("performed_in.max")
	String performed_inMax();

	@Key("paymentAccepted.max")
	String paymentAcceptedMax();

	@Key("contactPoint.max")
	String contactPointMax();

	@Key("openingHours.max")
	String openingHoursMax();

	@Key("contentRating.max")
	String contentRatingMax();

	@Key("keywords.max")
	String keywordsMax();

	@Key("homepage.max")
	String homepageMax();

	@Key("validThrough.max")
	String validThroughMax();

	@Key("isbn.max")
	String isbnMax();

	@Key("validFrom.max")
	String validFromMax();

	@Key("actor.max")
	String actorMax();

	@Key("totalVotes.max")
	String totalVotesMax();

	@Key("language.max")
	String languageMax();

	@Key("nationality.max")
	String nationalityMax();

	@Key("purchaseDate.max")
	String purchaseDateMax();

	@Key("Location.max")
	String LocationMax();

	@Key("eligibleQuantity.max")
	String eligibleQuantityMax();

	@Key("printSection.max")
	String printSectionMax();

	@Key("editor.max")
	String editorMax();

	@Key("title.max")
	String titleMax();

	@Key("aggregateRating.max")
	String aggregateRatingMax();

	@Key("familyName.max")
	String familyNameMax();

	@Key("text.max")
	String textMax();

	@Key("printEdition.max")
	String printEditionMax();

	@Key("faxNumber.max")
	String faxNumberMax();

	@Key("rating.max")
	String ratingMax();

	@Key("telephone.max")
	String telephoneMax();

	@Key("tag.max")
	String tagMax();

	@Key("caption.max")
	String captionMax();

	@Key("record_number.max")
	String record_numberMax();

	@Key("conductor.max")
	String conductorMax();

	@Key("follows.max")
	String followsMax();

	@Key("contentSize.max")
	String contentSizeMax();

	@Key("bookEdition.max")
	String bookEditionMax();

	@Key("description.max")
	String descriptionMax();

	@Key("award.max")
	String awardMax();

	@Key("legalName.max")
	String legalNameMax();

	@Key("purchaseFor.max")
	String purchaseForMax();

	@Key("age.max")
	String ageMax();

	@Key("price.max")
	String priceMax();

	@Key("subscribes.max")
	String subscribesMax();

	@Key("userId.max")
	String userIdMax();

	@Key("reviewer.max")
	String reviewerMax();

	@Key("producer.max")
	String producerMax();

	@Key("employee.max")
	String employeeMax();

	@Key("author.max")
	String authorMax();

	@Key("birthDate.max")
	String birthDateMax();

}