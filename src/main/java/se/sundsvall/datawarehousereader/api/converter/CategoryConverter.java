package se.sundsvall.datawarehousereader.api.converter;

import java.util.stream.Stream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.dept44.problem.Problem;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static se.sundsvall.datawarehousereader.Constants.INVALID_PARAMETER_CATEGORY;

@Component
public class CategoryConverter implements Converter<String, Category> {

	@Override
	public Category convert(String source) {
		if (isBlank(source)) {
			return null;
		}

		return Stream.of(Category.values())
			.filter(member -> member.name().equalsIgnoreCase(source.trim()))
			.findAny()
			.orElseThrow(() -> Problem.valueOf(BAD_REQUEST, format(INVALID_PARAMETER_CATEGORY, source)));
	}
}
