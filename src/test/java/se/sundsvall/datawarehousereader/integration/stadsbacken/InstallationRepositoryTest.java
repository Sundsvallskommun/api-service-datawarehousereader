package se.sundsvall.datawarehousereader.integration.stadsbacken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.installation.InstallationParameters;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
class InstallationRepositoryTest {

	@Autowired
	private InstallationRepository repository;

	@Test
	void findAllByParametersNoMatch() {
		final var page = repository.findAllByParameters(
			InstallationParameters.create()
				.withInstalled(true)
				.withDateFrom(LocalDate.now())
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
		final var page = repository.findAllByParameters(InstallationParameters.create(), PageRequest.of(0, 100));

		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getNumberOfElements()).isEqualTo(6);
		assertThat(page.getTotalElements()).isEqualTo(6);
		assertThat(page.getContent()).hasSize(6);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void findAllByParametersInstalled(final boolean installed) {
		final var page = repository.findAllByParameters(InstallationParameters.create().withInstalled(installed), PageRequest.of(0, 100));

		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getNumberOfElements()).isEqualTo(3);
		assertThat(page.getTotalElements()).isEqualTo(3);
		assertThat(page.getContent()).hasSize(3);
	}

	@ParameterizedTest
	@EnumSource(value = Category.class, mode = EnumSource.Mode.EXCLUDE, names = {"WATER", "COMMUNICATION"})
	void findAllByParametersCategory(final Category category) {
		final var page = repository.findAllByParameters(InstallationParameters.create().withCategory(category), PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isEqualTo(1);
		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getContent()).hasSize(1);
	}

	@Test
	void findAllByParametersDateFrom() {
		final var page = repository.findAllByParameters(InstallationParameters.create().withDateFrom(LocalDate.parse("2023-01-01")), PageRequest.of(0, 100));

		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getNumberOfElements()).isEqualTo(3);
		assertThat(page.getTotalElements()).isEqualTo(3);
		assertThat(page.getContent()).hasSize(3);
	}

	@Test
	void findAllByParametersFacilityId() {
		final var page = repository.findAllByParameters(InstallationParameters.create().withFacilityId("323456789123456789"), PageRequest.of(0, 100));

		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getNumberOfElements()).isEqualTo(1);
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getContent()).hasSize(1);
	}

}
