package se.sundsvall.datawarehousereader.api.model.measurement;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.DAY;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.HOUR;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.MONTH;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.QUARTER;

import org.junit.jupiter.api.Test;

class AggregationTest {

	@Test
	void enums() {
		assertThat(Aggregation.values()).containsExactlyInAnyOrder(QUARTER, HOUR, DAY, MONTH);
	}

	@Test
	void enumValues() {
		assertThat(QUARTER).hasToString("QUARTER");
		assertThat(HOUR).hasToString("HOUR");
		assertThat(DAY).hasToString("DAY");
		assertThat(MONTH).hasToString("MONTH");
	}
}
