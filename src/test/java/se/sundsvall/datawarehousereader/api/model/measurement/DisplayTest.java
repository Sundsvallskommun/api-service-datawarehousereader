package se.sundsvall.datawarehousereader.api.model.measurement;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.datawarehousereader.api.model.measurement.Display.AGGREGATE;
import static se.sundsvall.datawarehousereader.api.model.measurement.Display.ONLYAGGREGATED;

class DisplayTest {

	@Test
	void enums() {
		assertThat(Display.values()).containsExactlyInAnyOrder(AGGREGATE, ONLYAGGREGATED);
	}

	@Test
	void enumValues() {
		assertThat(AGGREGATE).hasToString("AGGREGATE");
		assertThat(ONLYAGGREGATED).hasToString("ONLYAGGREGATED");
	}
}
