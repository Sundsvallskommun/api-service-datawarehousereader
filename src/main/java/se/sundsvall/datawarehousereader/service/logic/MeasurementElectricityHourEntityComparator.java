package se.sundsvall.datawarehousereader.service.logic;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ObjectUtils.allNull;
import static org.springframework.data.domain.Sort.Direction.ASC;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.data.domain.Sort.Direction;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityHourEntity;

public class MeasurementElectricityHourEntityComparator implements Comparator<MeasurementElectricityHourEntity> {

	private final List<String> sortBy;
	private final Direction direction;

	public static MeasurementElectricityHourEntityComparator create(List<String> sortBy, Direction direction) {
		return new MeasurementElectricityHourEntityComparator(sortBy, direction);
	}

	MeasurementElectricityHourEntityComparator(List<String> sortBy, Direction direction) {
		this.sortBy = sortBy;
		this.direction = direction;
	}

	@Override
	public int compare(MeasurementElectricityHourEntity o1, MeasurementElectricityHourEntity o2) {
		final var comparison = new AtomicInteger(0);

		Optional.ofNullable(sortBy).orElse(Collections.emptyList()).stream()
			.forEach(fieldName -> {
				if (comparison.get() == 0) {
					comparison.set(retrieveAndCompareValues(o1, o2, fieldName));
				}
			});

		return comparison.get();
	}

	private int retrieveAndCompareValues(MeasurementElectricityHourEntity entity1, MeasurementElectricityHourEntity entity2, String fieldName) {
		final var value1 = getValue(ASC == direction ? entity1 : entity2, fieldName);
		final var value2 = getValue(ASC == direction ? entity2 : entity1, fieldName);

		if (allNull(value1, value2)) {
			return 0;
		}
		if (isNull(value1)) {
			return -1;
		}
		if (isNull(value2)) {
			return 1;
		} else {
			return doComparison(value1, value2);
		}
	}

	private int doComparison(Object object1, Object object2) {
		if (object1 instanceof final LocalDateTime localDateTime1 && object2 instanceof final LocalDateTime localDateTime2) {
			return localDateTime1.compareTo(localDateTime2);
		}
		if (object1 instanceof final Integer integer1 && object2 instanceof final Integer integer2) {
			return integer1.compareTo(integer2);
		}
		if (object1 instanceof final BigDecimal bigDecimal1 && object2 instanceof final BigDecimal bigDecimal2) {
			return bigDecimal1.compareTo(bigDecimal2);
		}

		// If not one of the above has triggered, default back to string comparison
		return String.valueOf(object1).compareTo(String.valueOf(object2));

	}

	private Object getValue(MeasurementElectricityHourEntity object, String fieldName) {
		if (isNull(object) || isFieldAbsent(object, fieldName)) {
			return null;
		}

		try {
			final var field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true); // NOSONAR
			return field.get(object);
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
			throw Problem.valueOf(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private boolean isFieldAbsent(MeasurementElectricityHourEntity object, String fieldName) {
		return Arrays.stream(object.getClass().getDeclaredFields())
			.noneMatch(f -> f.getName().equals(fieldName));
	}
}
