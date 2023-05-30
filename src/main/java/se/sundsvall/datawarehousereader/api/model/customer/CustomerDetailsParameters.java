package se.sundsvall.datawarehousereader.api.model.customer;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import se.sundsvall.datawarehousereader.api.model.AbstractParameterBase;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Customer details request parameters model")
public class CustomerDetailsParameters extends AbstractParameterBase {



    @ArraySchema(schema = @Schema(description = "PartyId (e.g. a personId or an organizationId)", example = "81471222-5798-11e9-ae24-57fa13b361e1"))
    private List<@ValidUuid String> partyId;
    @Schema(description = "Date and time for when to search for changes from. Format is yyyy-MM-dd'T'HH:mm:ss.SSSXXX, for example '2000-10-31T01:30:00.000-05:00'")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime fromDateTime;

    @Schema(description = "Date and time for when to search for change to. Format is yyyy-MM-dd'T'HH:mm:ss.SSSXXX, for example '2000-10-31T01:30:00.000-05:00'")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime toDateTime;

    public List<String> getPartyId() {
        return partyId;
    }

    public static CustomerDetailsParameters create() {
        return new CustomerDetailsParameters();
    }

    public void setPartyId(List<String> partyId) {
        this.partyId = partyId;
    }

    public CustomerDetailsParameters withPartyId(List<String> partyId) {
        this.partyId = partyId;
        return this;
    }

    public CustomerDetailsParameters withFromDateTime(OffsetDateTime fromDateTime) {
        this.fromDateTime = fromDateTime;
        return this;
    }

    public CustomerDetailsParameters withToDateTime(OffsetDateTime toDateTime) {
        this.toDateTime = toDateTime;
        return this;
    }

    public OffsetDateTime getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(final OffsetDateTime fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public OffsetDateTime getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(final OffsetDateTime toDateTime) {
        this.toDateTime = toDateTime;
    }

    @Override
    public String toString() {
        return "CustomerDetailsParameters{" +
            "partyId=" + partyId +
            ", fromDateTime=" + fromDateTime +
            ", toDateTime=" + toDateTime +
            ", page=" + page +
            ", limit=" + limit +
            ", sortBy=" + sortBy +
            ", sortDirection=" + sortDirection +
            '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final CustomerDetailsParameters that = (CustomerDetailsParameters) o;
        return Objects.equals(partyId, that.partyId) && Objects.equals(fromDateTime, that.fromDateTime) && Objects.equals(toDateTime, that.toDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), partyId, fromDateTime, toDateTime);
    }
}
