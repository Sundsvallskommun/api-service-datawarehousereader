package se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(schema = "kundinfo", name = "vInvoiceDetail_Test_251126")
public class InvoiceDetailEntity {

	@Id
	@Column(name = "InvoiceProductSeq", nullable = false, insertable = false, updatable = false)
	private int invoiceProductSeq;

	@Column(name = "invoiceid", nullable = false, insertable = false, updatable = false)
	private int invoiceId;

	@Column(name = "Productcode", nullable = false, insertable = false, updatable = false, columnDefinition = "smallint")
	private int productCode;

	@Column(name = "periodFrom", insertable = false, updatable = false, columnDefinition = "nvarchar(4000)")
	private String periodFrom;

	@Column(name = "periodTo", insertable = false, updatable = false, columnDefinition = "nvarchar(4000)")
	private String periodTo;

	@Column(name = "Amount", insertable = false, updatable = false, columnDefinition = "money")
	private BigDecimal amount;

	@Column(name = "Invoicenumber", insertable = false, updatable = false)
	private Long invoiceNumber;

	@Column(name = "AmountVatExcluded", insertable = false, updatable = false, columnDefinition = "money")
	private BigDecimal amountVatExcluded;

	@Column(name = "Vat", insertable = false, updatable = false, columnDefinition = "money")
	private BigDecimal vat;

	@Column(name = "Vatrate", insertable = false, updatable = false)
	private Double vatRate;

	@Column(name = "Quantity", insertable = false, updatable = false)
	private Double quantity;

	@Column(name = "unit", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String unit;

	@Column(name = "Unitprice", insertable = false, updatable = false, columnDefinition = "money")
	private BigDecimal unitPrice;

	@Column(name = "Description", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String description;

	@Column(name = "Productname", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String productName;

	@Column(name = "OrganizationId", insertable = false, updatable = false, columnDefinition = "varchar(10)")
	private String organizationId;

	@Column(name = "FacilityId", insertable = false, updatable = false, columnDefinition = "varchar(50)")
	private String facilityId;

	@Column(name = "Administration", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String administration;

	public String getAdministration() {
		return administration;
	}

	public void setAdministration(String administration) {
		this.administration = administration;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public int getInvoiceProductSeq() {
		return invoiceProductSeq;
	}

	public void setInvoiceProductSeq(int invoiceProductSeq) {
		this.invoiceProductSeq = invoiceProductSeq;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Long getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmountVatExcluded() {
		return amountVatExcluded;
	}

	public void setAmountVatExcluded(BigDecimal amountVatExcluded) {
		this.amountVatExcluded = amountVatExcluded;
	}

	public BigDecimal getVat() {
		return vat;
	}

	public void setVat(BigDecimal vat) {
		this.vat = vat;
	}

	public Double getVatRate() {
		return vatRate;
	}

	public void setVatRate(Double vatRate) {
		this.vatRate = vatRate;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getPeriodFrom() {
		return periodFrom;
	}

	public void setPeriodFrom(String periodFrom) {
		this.periodFrom = periodFrom;
	}

	public String getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(String periodTo) {
		this.periodTo = periodTo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getProductCode() {
		return productCode;
	}

	public void setProductCode(int productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		InvoiceDetailEntity that = (InvoiceDetailEntity) o;
		return invoiceProductSeq == that.invoiceProductSeq && invoiceId == that.invoiceId && productCode == that.productCode && Objects.equals(periodFrom, that.periodFrom) && Objects.equals(periodTo, that.periodTo)
			&& Objects.equals(amount, that.amount) && Objects.equals(invoiceNumber, that.invoiceNumber) && Objects.equals(amountVatExcluded, that.amountVatExcluded) && Objects.equals(vat, that.vat)
			&& Objects.equals(vatRate, that.vatRate) && Objects.equals(quantity, that.quantity) && Objects.equals(unit, that.unit) && Objects.equals(unitPrice, that.unitPrice) && Objects.equals(description,
				that.description) && Objects.equals(productName, that.productName) && Objects.equals(organizationId, that.organizationId) && Objects.equals(facilityId, that.facilityId) && Objects.equals(administration,
					that.administration);
	}

	@Override
	public int hashCode() {
		return Objects.hash(invoiceProductSeq, invoiceId, productCode, periodFrom, periodTo, amount, invoiceNumber, amountVatExcluded, vat, vatRate, quantity, unit, unitPrice, description, productName, organizationId, facilityId, administration);
	}

	@Override
	public String toString() {
		return "InvoiceDetailEntity{" +
			"invoiceProductSeq=" + invoiceProductSeq +
			", invoiceId=" + invoiceId +
			", productCode=" + productCode +
			", periodFrom='" + periodFrom + '\'' +
			", periodTo='" + periodTo + '\'' +
			", amount=" + amount +
			", invoiceNumber=" + invoiceNumber +
			", amountVatExcluded=" + amountVatExcluded +
			", vat=" + vat +
			", vatRate=" + vatRate +
			", quantity=" + quantity +
			", unit='" + unit + '\'' +
			", unitPrice=" + unitPrice +
			", description='" + description + '\'' +
			", productName='" + productName + '\'' +
			", organizationId='" + organizationId + '\'' +
			", facilityId='" + facilityId + '\'' +
			", administration='" + administration + '\'' +
			'}';
	}
}
