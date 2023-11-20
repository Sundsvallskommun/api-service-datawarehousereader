package se.sundsvall.datawarehousereader.integration.stadsbacken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemMetaDataEmbeddable;

/**
 * Installed base repository tests.
 * 
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
class InstalledBaseRepositoryTest {

	@Autowired
	private InstalledBaseRepository repository;

	@Test
	void getInstalledBaseNoMatch() {
		final var page = repository.findAllByParameters(createParameters("99999", null, null), PageRequest.of(0, 100));

		assertThat(page.getNumber()).isZero();
		assertThat(page.getNumberOfElements()).isZero();
		assertThat(page.getTotalPages()).isZero();
		assertThat(page.getTotalElements()).isZero();
		assertThat(page.getContent()).isEmpty();
	}

	@Test
	void getInstalledBaseNoFilters() {
		final var page = repository.findAllByParameters(InstalledBaseParameters.create(), PageRequest.of(0, 100));

		assertThat(page.getNumber()).isZero();
		assertThat(page.getNumberOfElements()).isEqualTo(100);
		assertThat(page.getTotalPages()).isEqualTo(4);
		assertThat(page.getTotalElements()).isEqualTo(322);
		assertThat(page.getContent()).hasSize(100);
	}

	@Test
	void getInstalledBaseByCustomerId() {
		final var page = repository.findAllByParameters(createParameters("38308", null, null), PageRequest.of(0, 100));

		assertThat(page.getNumber()).isZero();
		assertThat(page.getNumberOfElements()).isEqualTo(2);
		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getTotalElements()).isEqualTo(2);
		assertThat(page.getContent())
			.hasSize(2)
			.extracting(
				InstalledBaseItemEntity::getCareOf,
				InstalledBaseItemEntity::getCompany,
				InstalledBaseItemEntity::getCustomerId,
				InstalledBaseItemEntity::getInternalId,
				InstalledBaseItemEntity::getFacilityId,
				InstalledBaseItemEntity::getStreet,
				InstalledBaseItemEntity::getType)
			.containsExactlyInAnyOrder(
				tuple("Privat företag AB", "Sundsvall Energi AB", 38308, 1886, "9412001019", "Gatan 66", "Elhandel"),
				tuple("Privat företag AB", "Sundsvall Energi AB", 38308, 498, "38308", "", "Avfallsvåg"));

		assertThat(page.getContent().get(0).getMetaData()).isEmpty();
		assertThat(page.getContent().get(1).getMetaData()).containsExactly(createMetaDataPost("Sundsvall Energi AB", "Nätområde", "netarea", "location", "Sollentuna"));
	}

	@Test
	void getInstalledBaseByCustomerIdPageOneOfTwo() {
		final var page = repository.findAllByParameters(createParameters("38308", null, null), PageRequest.of(0, 1));

		assertThat(page.getNumber()).isZero();
		assertThat(page.getNumberOfElements()).isEqualTo(1);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.getTotalElements()).isEqualTo(2);
		assertThat(page.getContent())
			.hasSize(1)
			.extracting(
				InstalledBaseItemEntity::getCareOf,
				InstalledBaseItemEntity::getCompany,
				InstalledBaseItemEntity::getCustomerId,
				InstalledBaseItemEntity::getHouseName,
				InstalledBaseItemEntity::getInternalId,
				InstalledBaseItemEntity::getFacilityId,
				InstalledBaseItemEntity::getType)
			.containsExactly(tuple("Privat företag AB", "Sundsvall Energi AB", 38308, null, 498, "38308", "Avfallsvåg"));

		assertThat(page.getContent().get(0).getMetaData()).isEmpty();
	}

	@Test
	void getInstalledBaseByCustomerIdAndCompany() {
		final var page = repository.findAllByParameters(createParameters("600606", "Sundsvall Energi AB", null), PageRequest.of(0, 100));

		assertThat(page.getNumber()).isZero();
		assertThat(page.getNumberOfElements()).isEqualTo(4);
		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getTotalElements()).isEqualTo(4);
		assertThat(page.getContent())
			.hasSize(4)
			.extracting(
				InstalledBaseItemEntity::getCareOf,
				InstalledBaseItemEntity::getCity,
				InstalledBaseItemEntity::getCompany,
				InstalledBaseItemEntity::getCustomerId,
				InstalledBaseItemEntity::getInternalId,
				InstalledBaseItemEntity::getFacilityId,
				InstalledBaseItemEntity::getPostCode,
				InstalledBaseItemEntity::getStreet,
				InstalledBaseItemEntity::getType)
			.containsExactlyInAnyOrder(
				tuple("Fräscha fastigheter AB", null, "Sundsvall Energi AB", 600606, 3619, "9120601050", null, "Gatan 3", "Elhandel"),
				tuple("Fräscha fastigheter AB", null, "Sundsvall Energi AB", 600606, 17255, "735999109140402043", null, "Gatan 14", "Elhandel"),
				tuple("Fräscha fastigheter AB", "SUNDSVALL", "Sundsvall Energi AB", 600606, 3620, "9140205031", "85350", "Gatan 10", "Elhandel"),
				tuple("Fräscha fastigheter AB", "SUNDSVALL", "Sundsvall Energi AB", 600606, 3621, "9140205056", "85350", "Gatan 14", "Elhandel"));

		assertThat(page.getContent().get(0).getMetaData()).containsExactly(createMetaDataPost("Sundsvall Energi AB", "Nätområde", "netarea", "location", "Täby"));
		assertThat(page.getContent().get(1).getMetaData()).containsExactly(createMetaDataPost("Sundsvall Energi AB", "Nätområde", "netarea", "location", "Täby"));
		assertThat(page.getContent().get(2).getMetaData()).containsExactly(createMetaDataPost("Sundsvall Energi AB", "Nätområde", "netarea", "location", "Täby"));
		assertThat(page.getContent().get(3).getMetaData()).containsExactly(createMetaDataPost("Sundsvall Energi AB", "Nätområde", "netarea", "location", "Halmstad"));
	}

	@Test
	void getInstalledBaseByFacilityId() {
		final var page = repository.findAllByParameters(createParameters(null, null, "9111003092"), PageRequest.of(0, 100));

		assertThat(page.getNumber()).isZero();
		assertThat(page.getNumberOfElements()).isEqualTo(1);
		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getContent())
			.hasSize(1)
			.extracting(
				InstalledBaseItemEntity::getCareOf,
				InstalledBaseItemEntity::getCompany,
				InstalledBaseItemEntity::getCustomerId,
				InstalledBaseItemEntity::getHouseName,
				InstalledBaseItemEntity::getInternalId,
				InstalledBaseItemEntity::getFacilityId,
				InstalledBaseItemEntity::getStreet,
				InstalledBaseItemEntity::getType)
			.containsExactly(tuple("Fastighetsförmedling AB", "Sundsvall Energi AB", 10335, "Ankeborg 2", 2658, "9111003092", "Gatan 27", "Elhandel"));

		assertThat(page.getContent().get(0).getMetaData()).containsExactly(createMetaDataPost("Sundsvall Energi AB", "Nätområde", "netarea", "location", "Malmö-Burlöv"));
	}

	@Test
	void getInstalledBaseByCustomerIdAndModifiedBetween() {
		final var params = createParameters("10335", "Sundsvall Energi AB", null);
		params.setLastModifiedDateFrom(LocalDate.of(2017, 12, 3));
		params.setLastModifiedDateTom(LocalDate.of(2017, 12, 6));

		final var page = repository.findAllByParameters(params, PageRequest.of(0, 100));

		assertThat(page.getNumber()).isZero();
		assertThat(page.getNumberOfElements()).isEqualTo(4);
		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getTotalElements()).isEqualTo(4);
		assertThat(page.getContent())
			.hasSize(4)
			.extracting(
				InstalledBaseItemEntity::getCareOf,
				InstalledBaseItemEntity::getCompany,
				InstalledBaseItemEntity::getCustomerId,
				InstalledBaseItemEntity::getInternalId,
				InstalledBaseItemEntity::getFacilityId,
				InstalledBaseItemEntity::getStreet,
				InstalledBaseItemEntity::getType,
				InstalledBaseItemEntity::getLastChangedDate)
			.containsExactly(
				tuple("Fastighetsförmedling AB", "Sundsvall Energi AB", 10335, -38186, "735999109324902055", "Vägen 2223", "Elhandel", LocalDate.of(2017, 12, 3)),
				tuple("Fastighetsförmedling AB", "Sundsvall Energi AB", 10335, -38126, "735999109144502091", "Gatan 21 B 4010170", "Elhandel", LocalDate.of(2017, 12, 4)),
				tuple("Fastighetsförmedling AB", "Sundsvall Energi AB", 10335, -37817, "735999109144515107", "Vägen 1112", "Elhandel", LocalDate.of(2017, 12, 5)),
				tuple("Fastighetsförmedling AB", "Sundsvall Energi AB", 10335, -37402, "735999109144517538", "Gatan 17 F 4010965", "Elhandel", LocalDate.of(2017, 12, 6)));
	}

	@Test
	void getInstalledBaseByCustomerIdAndModifiedLaterThan() {
		final var params = createParameters("10335", "Sundsvall Energi AB", null);
		params.setLastModifiedDateFrom(LocalDate.of(2017, 12, 6));

		final var page = repository.findAllByParameters(params, PageRequest.of(0, 100));

		assertThat(page.getNumber()).isZero();
		assertThat(page.getNumberOfElements()).isEqualTo(5);
		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getTotalElements()).isEqualTo(5);
		assertThat(page.getContent())
			.hasSize(5)
			.extracting(
				InstalledBaseItemEntity::getCareOf,
				InstalledBaseItemEntity::getCompany,
				InstalledBaseItemEntity::getCustomerId,
				InstalledBaseItemEntity::getInternalId,
				InstalledBaseItemEntity::getFacilityId,
				InstalledBaseItemEntity::getStreet,
				InstalledBaseItemEntity::getType,
				InstalledBaseItemEntity::getLastChangedDate)
			.containsExactly(
				tuple("Fastighetsförmedling AB", "Sundsvall Energi AB", 10335, -37402, "735999109144517538", "Gatan 17 F 4010965", "Elhandel", LocalDate.of(2017, 12, 6)),
				tuple("Fastighetsförmedling AB", "Sundsvall Energi AB", 10335, -36920, "735999109122811214", "Gatan 27", "Elhandel", LocalDate.of(2017, 12, 7)),
				tuple("Fastighetsförmedling AB", "Sundsvall Energi AB", 10335, -36827, "735999109140702235", "Gatan 4 B 4170153", "Elhandel", LocalDate.of(2017, 12, 7)),
				tuple("Fastighetsförmedling AB", "Sundsvall Energi AB", 10335, -36437, "735999109141108111", "Gatan 32", "Elhandel", LocalDate.of(2017, 12, 8)),
				tuple("Fastighetsförmedling AB", "Sundsvall Energi AB", 10335, -36019, "735999109141106155", "Vägen 6", "Elhandel", LocalDate.of(2017, 12, 8)));
	}

	private static InstalledBaseParameters createParameters(String customerNumber, String company, String facilityId) {
		InstalledBaseParameters parameters = InstalledBaseParameters.create();
		parameters.setCustomerNumber(customerNumber);
		parameters.setCompany(company);
		parameters.setFacilityId(facilityId);
		return parameters;
	}

	private static InstalledBaseItemMetaDataEmbeddable createMetaDataPost(String company, String displayName, String key, String type, String value) {
		InstalledBaseItemMetaDataEmbeddable metadata = new InstalledBaseItemMetaDataEmbeddable();
		metadata.setCompany(company);
		metadata.setDisplayName(displayName);
		metadata.setKey(key);
		metadata.setType(type);
		metadata.setValue(value);
		return metadata;
	}
}
