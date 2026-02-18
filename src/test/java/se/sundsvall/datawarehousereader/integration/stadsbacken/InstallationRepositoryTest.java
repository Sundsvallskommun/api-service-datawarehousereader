package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.installation.InstallationParameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static se.sundsvall.datawarehousereader.api.model.Category.DISTRICT_COOLING;
import static se.sundsvall.datawarehousereader.api.model.Category.DISTRICT_HEATING;
import static se.sundsvall.datawarehousereader.api.model.Category.ELECTRICITY;
import static se.sundsvall.datawarehousereader.api.model.Category.ELECTRICITY_TRADE;
import static se.sundsvall.datawarehousereader.api.model.Category.WASTE_MANAGEMENT;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
class InstallationRepositoryTest {

	@Autowired
	private InstallationRepository repository;

	private static Stream<Arguments> findAllByParametersInstalledArguments() {
		return Stream.of(
			Arguments.of(true, 5),
			Arguments.of(false, 4));
	}

	private static Stream<Arguments> findAllByParametersCategoryArguments() {
		return Stream.of(
			Arguments.of(DISTRICT_COOLING, 1),
			Arguments.of(DISTRICT_HEATING, 1),
			Arguments.of(ELECTRICITY, 4),
			Arguments.of(ELECTRICITY_TRADE, 1),
			Arguments.of(WASTE_MANAGEMENT, 1));
	}

	@Test
	void findAllByParametersNoMatch() {
		final var page = repository.findAllByParameters(
			InstallationParameters.create()
				.withInstalled(true)
				.withLastModifiedDateFrom(LocalDate.parse("2020-01-01"))
				.withLastModifiedDateTo(LocalDate.parse("2023-01-01"))
				.withCategory(Category.ELECTRICITY)
				.withFacilityId("facilityId"),
			PageRequest.of(0, 100));

		assertThat(page.getTotalPages()).isZero();
		assertThat(page.getTotalElements()).isZero();
		assertThat(page.getNumber()).isZero();
		assertThat(page.getNumberOfElements()).isZero();
		assertThat(page.getContent()).isEmpty();
	}

	@Test
	void findAllByParametersNoFilters() {
		final var page = repository.findAllByParameters(
			InstallationParameters.create(),
			PageRequest.of(0, 100));

		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getNumberOfElements()).isEqualTo(9);
		assertThat(page.getTotalElements()).isEqualTo(9);
		assertThat(page.getContent()).hasSize(9);
	}

	@ParameterizedTest
	@MethodSource("findAllByParametersInstalledArguments")
	void findAllByParametersInstalled(final boolean installed, final int hits) {
		final var page = repository.findAllByParameters(
			InstallationParameters.create()
				.withInstalled(installed),
			PageRequest.of(0, 100));

		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getNumberOfElements()).isEqualTo(hits);
		assertThat(page.getTotalElements()).isEqualTo(hits);
		assertThat(page.getContent()).hasSize(hits);
	}

	@ParameterizedTest
	@MethodSource("findAllByParametersCategoryArguments")
	void findAllByParametersCategory(final Category category, final int hits) {
		final var page = repository.findAllByParameters(
			InstallationParameters.create()
				.withCategory(category),
			PageRequest.of(0, 100));

		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getNumberOfElements()).isEqualTo(hits);
		assertThat(page.getTotalElements()).isEqualTo(hits);
		assertThat(page.getContent()).hasSize(hits);
	}

	@Test
	void findAllByDateBetween() {
		final var page = repository.findAllByParameters(
			InstallationParameters.create()
				.withLastModifiedDateFrom(LocalDate.parse("2020-01-01"))
				.withLastModifiedDateTo(LocalDate.parse("2023-01-01")),
			PageRequest.of(0, 100));

		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getNumberOfElements()).isEqualTo(5);
		assertThat(page.getTotalElements()).isEqualTo(5);
		assertThat(page.getContent()).hasSize(5);
	}

	@Test
	void findAllByParametersFacilityId() {
		final var page = repository.findAllByParameters(
			InstallationParameters.create()
				.withFacilityId("323456789123456789"),
			PageRequest.of(0, 100));

		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getNumberOfElements()).isEqualTo(1);
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getContent()).hasSize(1);
	}

}
