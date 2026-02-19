package se.sundsvall.datawarehousereader.api.model.invoice;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Objects;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Schema(description = "Invoice detail model")
public class InvoiceDetail {

	@Schema(description = "Invoice number", examples = "767915994", accessMode = READ_ONLY)
	private Long invoiceNumber;

	@Schema(description = "Amount", examples = "66.97", accessMode = READ_ONLY)
	private BigDecimal amount;

	@Schema(description = "Amount excluded VAT", examples = "53.57", accessMode = READ_ONLY)
	private BigDecimal amountVatExcluded;

	@Schema(description = "VAT", examples = "13.40", accessMode = READ_ONLY)
	private BigDecimal vat;

	@Schema(description = "VAT rate", examples = "25.0", accessMode = READ_ONLY)
	private Double vatRate;

	@Schema(description = "Quantity", examples = "154.39", accessMode = READ_ONLY)
	private Double quantity;

	@Schema(description = "Unit", examples = "kWh", accessMode = READ_ONLY)
	private String unit;

	@Schema(description = "Price per unit", examples = "0.347", accessMode = READ_ONLY)
	private BigDecimal unitPrice;

	@Schema(description = "Period from", examples = "2022-01-01", accessMode = READ_ONLY)
	private String periodFrom;

	@Schema(description = "Period to", examples = "2022-01-31", accessMode = READ_ONLY)
	private String periodTo;

	@Schema(description = "Description", examples = "This is a description", accessMode = READ_ONLY)
	private String description;

	@Schema(description = "Product code", examples = "1404", accessMode = READ_ONLY)
	private Integer productCode;

	@Schema(description = "Product name", examples = "Elöverföring", accessMode = READ_ONLY)
	private String productName;

	@Schema(description = "Organization number of invoice issuer", examples = "5565027223", accessMode = READ_ONLY)
	private String organizationNumber;

	@Schema(description = "Administration", examples = "Name of the administration", accessMode = READ_ONLY)
	private String administration;

	@Schema(description = "Facility id", examples = "735999109151401011", accessMode = READ_ONLY)
	private String facilityId;

	public static InvoiceDetail create() {
		return new InvoiceDetail();
	}

	public String getAdministration() {
		return administration;
	}

	public InvoiceDetail withAdministration(String administration) {
		this.administration = administration;
		return this;
	}

	public void setAdministration(String administration) {
		this.administration = administration;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public InvoiceDetail withFacilityId(String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public Long getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public InvoiceDetail withInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
		return this;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public InvoiceDetail withAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public BigDecimal getAmountVatExcluded() {
		return amountVatExcluded;
	}

	public void setAmountVatExcluded(BigDecimal amountVatExcluded) {
		this.amountVatExcluded = amountVatExcluded;
	}

	public InvoiceDetail withAmountVatExcluded(BigDecimal amountVatExcluded) {
		this.amountVatExcluded = amountVatExcluded;
		return this;
	}

	public BigDecimal getVat() {
		return vat;
	}

	public void setVat(BigDecimal vat) {
		this.vat = vat;
	}

	public InvoiceDetail withVat(BigDecimal vat) {
		this.vat = vat;
		return this;
	}

	public Double getVatRate() {
		return vatRate;
	}

	public void setVatRate(Double vatRate) {
		this.vatRate = vatRate;
	}

	public InvoiceDetail withVatRate(Double vatRate) {
		this.vatRate = vatRate;
		return this;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public InvoiceDetail withQuantity(Double quantity) {
		this.quantity = quantity;
		return this;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public InvoiceDetail withUnit(String unit) {
		this.unit = unit;
		return this;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public InvoiceDetail withUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
		return this;
	}

	public String getPeriodFrom() {
		return periodFrom;
	}

	public void setPeriodFrom(String periodFrom) {
		this.periodFrom = periodFrom;
	}

	public InvoiceDetail withPeriodFrom(String periodFrom) {
		this.periodFrom = periodFrom;
		return this;
	}

	public String getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(String periodTo) {
		this.periodTo = periodTo;
	}

	public InvoiceDetail withPeriodTo(String periodTo) {
		this.periodTo = periodTo;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public InvoiceDetail withDescription(String description) {
		this.description = description;
		return this;
	}

	public Integer getProductCode() {
		return productCode;
	}

	public void setProductCode(Integer productCode) {
		this.productCode = productCode;
	}

	public InvoiceDetail withProductCode(Integer productCode) {
		this.productCode = productCode;
		return this;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public InvoiceDetail withProductName(String productName) {
		this.productName = productName;
		return this;
	}

	public String getOrganizationNumber() {
		return organizationNumber;
	}

	public void setOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
	}

	public InvoiceDetail withOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		InvoiceDetail that = (InvoiceDetail) o;
		return Objects.equals(invoiceNumber, that.invoiceNumber) && Objects.equals(amount, that.amount) && Objects.equals(amountVatExcluded, that.amountVatExcluded) && Objects.equals(vat, that.vat)
			&& Objects.equals(vatRate, that.vatRate) && Objects.equals(quantity, that.quantity) && Objects.equals(unit, that.unit) && Objects.equals(unitPrice, that.unitPrice) && Objects.equals(periodFrom,
				that.periodFrom) && Objects.equals(periodTo, that.periodTo) && Objects.equals(description, that.description) && Objects.equals(productCode, that.productCode) && Objects.equals(productName, that.productName)
			&& Objects.equals(organizationNumber, that.organizationNumber) && Objects.equals(administration, that.administration) && Objects.equals(facilityId, that.facilityId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(invoiceNumber, amount, amountVatExcluded, vat, vatRate, quantity, unit, unitPrice, periodFrom, periodTo, description, productCode, productName, organizationNumber, administration, facilityId);
	}

	@Override
	public String toString() {
		return "InvoiceDetail{" +
			"invoiceNumber=" + invoiceNumber +
			", amount=" + amount +
			", amountVatExcluded=" + amountVatExcluded +
			", vat=" + vat +
			", vatRate=" + vatRate +
			", quantity=" + quantity +
			", unit='" + unit + '\'' +
			", unitPrice=" + unitPrice +
			", periodFrom='" + periodFrom + '\'' +
			", periodTo='" + periodTo + '\'' +
			", description='" + description + '\'' +
			", productCode=" + productCode +
			", productName='" + productName + '\'' +
			", organizationNumber='" + organizationNumber + '\'' +
			", administration='" + administration + '\'' +
			", facilityId='" + facilityId + '\'' +
			'}';
	}
}
