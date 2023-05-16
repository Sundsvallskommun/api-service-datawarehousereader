package se.sundsvall.datawarehousereader.service.logic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Sort.Direction;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityHourEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MeasurementElectricityHourEntityComparatorTest {

	@ParameterizedTest
	@MethodSource({
		"emptyComparator",
		"nonExistingField",
		"equalFieldvalues",
		"differentFieldvaluesSortedAsc",
		"differentFieldvaluesSortedDesc",
		"multipleFieldvaluesDiffOnFirstSortedDesc",
		"multipleFieldvaluesDiffOnLastSortedDesc"
	})
	void testComparisons(MeasurementElectricityHourEntityComparator comparator, MeasurementElectricityHourEntity o1, MeasurementElectricityHourEntity o2, int result) {
		assertThat(comparator.compare(o1, o2)).describedAs("Object %s should be placed %s object %s", o1, result < 0 ? "before" : result > 0 ? "after" : "equal to", o2).isEqualTo(result);
	}

	private static Stream<Arguments> emptyComparator() {
		final var comparator = MeasurementElectricityHourEntityComparator.create(null, Direction.ASC);

		return Stream.of(
			Arguments.of(comparator, null, null, 0),
			Arguments.of(comparator, MeasurementElectricityHourEntity.create(), null, 0),
			Arguments.of(comparator, null, MeasurementElectricityHourEntity.create(), 0),
			Arguments.of(comparator, MeasurementElectricityHourEntity.create(), MeasurementElectricityHourEntity.create(), 0));
	}

	private static Stream<Arguments> nonExistingField() {
		final var comparator = MeasurementElectricityHourEntityComparator.create(List.of("nonExistingFieldname"), Direction.ASC);

		return Stream.of(
			Arguments.of(comparator, null, null, 0),
			Arguments.of(comparator, MeasurementElectricityHourEntity.create(), null, 0),
			Arguments.of(comparator, null, MeasurementElectricityHourEntity.create(), 0),
			Arguments.of(comparator, MeasurementElectricityHourEntity.create(), MeasurementElectricityHourEntity.create(), 0));
	}

	private static Stream<Arguments> equalFieldvalues() {
		final var comparator = MeasurementElectricityHourEntityComparator.create(List.of("customerOrgId"), Direction.ASC);
		final var o1 = MeasurementElectricityHourEntity.create().withCustomerOrgId("customerOrgId");
		final var o2 = MeasurementElectricityHourEntity.create().withCustomerOrgId("customerOrgId");

		return Stream.of(
			Arguments.of(comparator, null, null, 0),
			Arguments.of(comparator, o1, null, 1),
			Arguments.of(comparator, null, o2, -1),
			Arguments.of(comparator, o1, o2, 0));
	}

	private static Stream<Arguments> differentFieldvaluesSortedAsc() {
		final var comparator = MeasurementElectricityHourEntityComparator.create(List.of("customerOrgId"), Direction.ASC);
		final var o1 = MeasurementElectricityHourEntity.create().withCustomerOrgId("customerOrgId_1");
		final var o2 = MeasurementElectricityHourEntity.create().withCustomerOrgId("customerOrgId_2");

		return Stream.of(
			Arguments.of(comparator, o1, o2, -1),
			Arguments.of(comparator, o2, o1, 1));
	}

	private static Stream<Arguments> differentFieldvaluesSortedDesc() {
		final var comparator = MeasurementElectricityHourEntityComparator.create(List.of("customerOrgId"), Direction.DESC);
		final var o1 = MeasurementElectricityHourEntity.create().withCustomerOrgId("customerOrgId_1");
		final var o2 = MeasurementElectricityHourEntity.create().withCustomerOrgId("customerOrgId_2");

		return Stream.of(
			Arguments.of(comparator, o1, o2, 1),
			Arguments.of(comparator, o2, o1, -1));
	}

	private static Stream<Arguments> multipleFieldvaluesDiffOnFirstSortedDesc() {
		final var comparator = MeasurementElectricityHourEntityComparator.create(List.of("usage", "interpolation"), Direction.DESC);
		final var o1 = MeasurementElectricityHourEntity.create().withUsage(BigDecimal.valueOf(0.001d));
		final var o2 = MeasurementElectricityHourEntity.create().withUsage(BigDecimal.valueOf(0.02d));

		return Stream.of(
			Arguments.of(comparator, o2, o2, 0),
			Arguments.of(comparator, o1, o2, 1),
			Arguments.of(comparator, o2, o1, -1),
			Arguments.of(comparator, o1, o1, 0));
	}

	private static Stream<Arguments> multipleFieldvaluesDiffOnLastSortedDesc() {
		final var comparator = MeasurementElectricityHourEntityComparator.create(List.of("customerOrgId", "interpolation"), Direction.DESC);
		final var o1 = MeasurementElectricityHourEntity.create().withCustomerOrgId("customerOrgId").withInterpolation(1);
		final var o2 = MeasurementElectricityHourEntity.create().withCustomerOrgId("customerOrgId").withInterpolation(2);

		return Stream.of(
			Arguments.of(comparator, o1, o2, 1),
			Arguments.of(comparator, o2, o1, -1));
	}

}
