package se.sundsvall.datawarehousereader.api.model.measurement;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Display mode for aggregated series", enumAsRef = true)
public enum Display {
	AGGREGATE,
	ONLYAGGREGATED
}
