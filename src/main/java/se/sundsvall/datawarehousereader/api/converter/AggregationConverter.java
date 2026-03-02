package se.sundsvall.datawarehousereader.api.converter;

import java.util.stream.Stream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.dept44.problem.Problem;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static se.sundsvall.datawarehousereader.Constants.INVALID_PARAMETER_AGGREGATION;

@Component
public class AggregationConverter implements Converter<String, Aggregation> {

	@Override
	public Aggregation convert(String source) {
		if (isBlank(source)) {
			return null;
		}

		return Stream.of(Aggregation.values())
			.filter(member -> member.name().equalsIgnoreCase(source.trim()))
			.findAny()
			.orElseThrow(() -> Problem.valueOf(BAD_REQUEST, format(INVALID_PARAMETER_AGGREGATION, source)));
	}
}
