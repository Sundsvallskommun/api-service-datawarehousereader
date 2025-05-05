package se.sundsvall.datawarehousereader.api.model.agreement;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import se.sundsvall.datawarehousereader.api.model.Category;

@Schema(description = "Agreement model")
public class Agreement {

	@Schema(description = "PartyId (e.g. a personId or an organizationId)", example = "81471222-5798-11e9-ae24-57fa13b361e1", accessMode = READ_ONLY)
	private String partyId;

	@Schema(description = "Customer number", example = "10007", accessMode = READ_ONLY)
	private String customerNumber;

	@Schema(description = "Facility Id", example = "1223334", accessMode = READ_ONLY)
	private String facilityId;

	@Schema(implementation = Category.class, accessMode = READ_ONLY)
	private Category category;

	@Schema(description = "Billing Id", example = "222333", accessMode = READ_ONLY)
	private String billingId;

	@Schema(description = "Agreement Id", example = "333444", accessMode = READ_ONLY)
	private String agreementId;

	@Schema(description = "Description", example = "Avtalet avser fjärrvärme", accessMode = READ_ONLY)
	private String description;

	@Schema(description = "Shows if agreement is a main-agreement or not", example = "true", accessMode = READ_ONLY)
	private Boolean mainAgreement;

	@Schema(description = "Shows if agreement include binding or not", example = "false", accessMode = READ_ONLY)
	private Boolean binding;

	@Schema(description = "Rule of binding if exists", example = "10 mån binding", accessMode = READ_ONLY)
	private String bindingRule;

	@Schema(description = "Placement status for agreement", example = "Tillkopplad", accessMode = READ_ONLY)
	private String placementStatus;

	@Schema(description = "Net area id for agreement", example = "SUV", accessMode = READ_ONLY)
	private String netAreaId;

	@Schema(description = "Site address connected to the agreement", example = "Första gatan 2", accessMode = READ_ONLY)
	private String siteAddress;

	@Schema(description = "Signal if the agreement is a production agreement or not (can be null if not applicable)", example = "true", accessMode = READ_ONLY)
	private Boolean production;

	@Schema(description = "From-date in validity of agreement", accessMode = READ_ONLY)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDate fromDate;

	@Schema(description = "To-date in validity of agreement", accessMode = READ_ONLY)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDate toDate;

	@Schema(description = "Shows if agreement is active ('fromDate' <= today and 'toDate' => today)", example = "true", accessMode = READ_ONLY)
	private Boolean active;

	public static Agreement create() {
		return new Agreement();
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public Agreement withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public Agreement withCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public Agreement withFacilityId(String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Agreement withCategory(Category category) {
		this.category = category;
		return this;
	}

	public String getBillingId() {
		return billingId;
	}

	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}

	public Agreement withBillingId(String billingId) {
		this.billingId = billingId;
		return this;
	}

	public String getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}

	public Agreement withAgreementId(String agreementId) {
		this.agreementId = agreementId;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Agreement withDescription(String description) {
		this.description = description;
		return this;
	}

	public Boolean getMainAgreement() {
		return mainAgreement;
	}

	public void setMainAgreement(Boolean mainAgreement) {
		this.mainAgreement = mainAgreement;
	}

	public Agreement withMainAgreement(Boolean mainAgreement) {
		this.mainAgreement = mainAgreement;
		return this;
	}

	public Boolean getBinding() {
		return binding;
	}

	public void setBinding(Boolean binding) {
		this.binding = binding;
	}

	public Agreement withBinding(Boolean binding) {
		this.binding = binding;
		return this;
	}

	public String getBindingRule() {
		return bindingRule;
	}

	public void setBindingRule(String bindingRule) {
		this.bindingRule = bindingRule;
	}

	public Agreement withBindingRule(String bindingRule) {
		this.bindingRule = bindingRule;
		return this;
	}

	public String getPlacementStatus() {
		return this.placementStatus;
	}

	public void setPlacementStatus(String placementStatus) {
		this.placementStatus = placementStatus;
	}

	public Agreement withPlacementStatus(String placementStatus) {
		this.placementStatus = placementStatus;
		return this;
	}

	public String getNetAreaId() {
		return netAreaId;
	}

	public void setNetAreaId(String netAreaId) {
		this.netAreaId = netAreaId;
	}

	public Agreement withNetAreaId(String netAreaId) {
		this.netAreaId = netAreaId;
		return this;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public Agreement withSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
		return this;
	}

	public Boolean getProduction() {
		return this.production;
	}

	public void setProduction(Boolean production) {
		this.production = production;
	}

	public Agreement withProduction(Boolean production) {
		this.production = production;
		return this;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public Agreement withFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
		return this;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public Agreement withToDate(LocalDate toDate) {
		this.toDate = toDate;
		return this;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Agreement withActive(Boolean active) {
		this.active = active;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(active, agreementId, billingId, binding, bindingRule, category, customerNumber, description, facilityId, fromDate, mainAgreement, netAreaId, partyId, placementStatus, production, siteAddress, toDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof final Agreement other)) { return false; }
		return Objects.equals(active, other.active) && Objects.equals(agreementId, other.agreementId) && Objects.equals(billingId, other.billingId) && Objects.equals(binding, other.binding) && Objects.equals(bindingRule, other.bindingRule)
			&& category == other.category && Objects.equals(customerNumber, other.customerNumber) && Objects.equals(description, other.description) && Objects.equals(facilityId, other.facilityId) && Objects.equals(fromDate, other.fromDate) && Objects
				.equals(mainAgreement, other.mainAgreement) && Objects.equals(netAreaId, other.netAreaId) && Objects.equals(partyId, other.partyId) && Objects.equals(placementStatus, other.placementStatus) && Objects.equals(production, other.production)
			&& Objects.equals(siteAddress, other.siteAddress) && Objects.equals(toDate, other.toDate);
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder();
		builder.append("Agreement [partyId=").append(partyId).append(", customerNumber=").append(customerNumber).append(", facilityId=").append(facilityId).append(", category=").append(category).append(", billingId=").append(billingId).append(
			", agreementId=").append(agreementId).append(", description=").append(description).append(", mainAgreement=").append(mainAgreement).append(", binding=").append(binding).append(", bindingRule=").append(bindingRule).append(", placementStatus=")
			.append(placementStatus).append(", netAreaId=").append(netAreaId).append(", siteAddress=").append(siteAddress).append(", production=").append(production).append(", fromDate=").append(fromDate).append(", toDate=").append(toDate).append(
				", active=").append(active).append("]");
		return builder.toString();
	}
}
