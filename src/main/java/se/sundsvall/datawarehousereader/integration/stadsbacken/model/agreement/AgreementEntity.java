package se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(schema = "kundinfo", name = "vAgreements")
@IdClass(AgreementKey.class)
public class AgreementEntity {

	@Id
	@Column(name = "agreementId", nullable = false, insertable = false, updatable = false)
	private Integer agreementId;

	@Id
	@Column(name = "billingId", nullable = false, insertable = false, updatable = false)
	private Integer billingId;

	@Column(name = "uuid", insertable = false, updatable = false, columnDefinition = "uniqueidentifier")
	private String uuid;

	@Column(name = "customerorgid", insertable = false, updatable = false, columnDefinition = "varchar(8000)")
	private String customerOrgId;

	@Column(name = "customerid", nullable = false, insertable = false, updatable = false)
	private Integer customerId;

	@Column(name = "facilityId", insertable = false, updatable = false, columnDefinition = "varchar(255)")
	private String facilityId;

	@Column(name = "category", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String category;

	@Column(name = "description", insertable = false, updatable = false, columnDefinition = "varchar(4096)")
	private String description;

	@Column(name = "mainAgreement", nullable = false, insertable = false, updatable = false, columnDefinition = "varchar(5)")
	private String mainAgreement;

	@Column(name = "binding", nullable = false, insertable = false, updatable = false, columnDefinition = "varchar(5)")
	private String binding;

	@Column(name = "bindingRule", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String bindingRule;

	@Column(name = "PlacementStatus", nullable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String placementStatus;

	@Column(name = "NetAreaId", nullable = true, updatable = false, columnDefinition = "nvarchar(255)")
	private String netAreaId;

	@Column(name = "SiteAddress", nullable = true, updatable = false, columnDefinition = "varchar(MAX)")
	private String siteAddress;

	@Column(name = "IsProduction", nullable = true, updatable = false, columnDefinition = "varchar(5)")
	private String isProduction;

	@Column(name = "fromDate", insertable = false, updatable = false, columnDefinition = "datetime")
	private LocalDateTime fromDate;

	@Column(name = "toDate", insertable = false, updatable = false, columnDefinition = "datetime")
	private LocalDateTime toDate;

	public static AgreementEntity create() {
		return new AgreementEntity();
	}

	public Integer getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(Integer agreementId) {
		this.agreementId = agreementId;
	}

	public AgreementEntity withAgreementId(Integer agreementId) {
		this.setAgreementId(agreementId);
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public AgreementEntity withUuid(String uuid) {
		this.setUuid(uuid);
		return this;
	}

	public String getCustomerOrgId() {
		return customerOrgId;
	}

	public void setCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
	}

	public AgreementEntity withCustomerOrgId(String customerOrgId) {
		this.setCustomerOrgId(customerOrgId);
		return this;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public AgreementEntity withCustomerId(Integer customerId) {
		this.setCustomerId(customerId);
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public AgreementEntity withFacilityId(String facilityId) {
		this.setFacilityId(facilityId);
		return this;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public AgreementEntity withCategory(String category) {
		this.setCategory(category);
		return this;
	}

	public Integer getBillingId() {
		return billingId;
	}

	public void setBillingId(Integer billingId) {
		this.billingId = billingId;
	}

	public AgreementEntity withBillingId(Integer billingId) {
		this.setBillingId(billingId);
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AgreementEntity withDescription(String description) {
		this.setDescription(description);
		return this;
	}

	public String getMainAgreement() {
		return mainAgreement;
	}

	public void setMainAgreement(String mainAgreement) {
		this.mainAgreement = mainAgreement;
	}

	public AgreementEntity withMainAgreement(String mainAgreement) {
		this.setMainAgreement(mainAgreement);
		return this;
	}

	public String getBinding() {
		return binding;
	}

	public void setBinding(String binding) {
		this.binding = binding;
	}

	public AgreementEntity withBinding(String binding) {
		this.setBinding(binding);
		return this;
	}

	public String getBindingRule() {
		return bindingRule;
	}

	public void setBindingRule(String bindingRule) {
		this.bindingRule = bindingRule;
	}

	public AgreementEntity withBindingRule(String bindingRule) {
		this.setBindingRule(bindingRule);
		return this;
	}

	public LocalDateTime getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDateTime fromDate) {
		this.fromDate = fromDate;
	}

	public AgreementEntity withFromDate(LocalDateTime fromDate) {
		this.setFromDate(fromDate);
		return this;
	}

	public LocalDateTime getToDate() {
		return toDate;
	}

	public void setToDate(LocalDateTime toDate) {
		this.toDate = toDate;
	}

	public AgreementEntity withToDate(LocalDateTime toDate) {
		this.setToDate(toDate);
		return this;
	}

	public String getPlacementStatus() {
		return placementStatus;
	}

	public void setPlacementStatus(String placementStatus) {
		this.placementStatus = placementStatus;
	}

	public AgreementEntity withPlacementStatus(String placementStatus) {
		this.placementStatus = placementStatus;
		return this;
	}

	public String getNetAreaId() {
		return netAreaId;
	}

	public void setNetAreaId(String netAreaId) {
		this.netAreaId = netAreaId;
	}

	public AgreementEntity withNetAreaId(String netAreaId) {
		this.netAreaId = netAreaId;
		return this;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public AgreementEntity withSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
		return this;
	}

	public String getIsProduction() {
		return isProduction;
	}

	public void setIsProduction(String isProduction) {
		this.isProduction = isProduction;
	}

	public AgreementEntity withIsProduction(String isProduction) {
		this.isProduction = isProduction;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(agreementId, billingId, binding, bindingRule, category, customerId, customerOrgId, description, facilityId, fromDate, isProduction, mainAgreement, netAreaId, placementStatus, siteAddress, toDate, uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof final AgreementEntity other)) { return false; }
		return Objects.equals(agreementId, other.agreementId) && Objects.equals(billingId, other.billingId) && Objects.equals(binding, other.binding) && Objects.equals(bindingRule, other.bindingRule) && Objects.equals(category, other.category) && Objects
			.equals(customerId, other.customerId) && Objects.equals(customerOrgId, other.customerOrgId) && Objects.equals(description, other.description) && Objects.equals(facilityId, other.facilityId) && Objects.equals(fromDate, other.fromDate) && Objects
				.equals(isProduction, other.isProduction) && Objects.equals(mainAgreement, other.mainAgreement) && Objects.equals(netAreaId, other.netAreaId) && Objects.equals(placementStatus, other.placementStatus) && Objects.equals(siteAddress,
					other.siteAddress) && Objects.equals(toDate, other.toDate) && Objects.equals(uuid, other.uuid);
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder();
		builder.append("AgreementEntity [agreementId=").append(agreementId).append(", billingId=").append(billingId).append(", uuid=").append(uuid).append(", customerOrgId=").append(customerOrgId).append(", customerId=").append(customerId).append(
			", facilityId=").append(facilityId).append(", category=").append(category).append(", description=").append(description).append(", mainAgreement=").append(mainAgreement).append(", binding=").append(binding).append(", bindingRule=").append(
				bindingRule).append(", placementStatus=").append(placementStatus).append(", netAreaId=").append(netAreaId).append(", siteAddress=").append(siteAddress).append(", isProduction=").append(isProduction).append(", fromDate=").append(fromDate)
			.append(", toDate=").append(toDate).append("]");
		return builder.toString();
	}
}
