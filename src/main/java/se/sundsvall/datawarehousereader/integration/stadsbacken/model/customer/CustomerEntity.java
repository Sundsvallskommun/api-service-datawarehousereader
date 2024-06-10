package se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(schema = "kundinfo", name = "vCustomer")
@IdClass(CustomerKey.class)
public class CustomerEntity {

	@Id
	@Column(name = "customerid", nullable = false, insertable = false, updatable = false)
	private Integer customerId;

	@Id
	@Column(name = "organizationid", insertable = false, updatable = false, columnDefinition = "varchar(10)")
	private String organizationId;

	@Column(name = "customerorgid", insertable = false, updatable = false)
	private String customerOrgId;

	@Column(name = "customertype", nullable = false, insertable = false, updatable = false, columnDefinition = "varchar(10)")
	private String customerType;

	@Column(name = "organizationname", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String organizationName;

	@Column(name = "active", columnDefinition = "varchar(1)", nullable = false, insertable = false, updatable = false)
	private boolean active;

	@Column(name = "moveindate", insertable = false, updatable = false, columnDefinition = "datetime")
	private LocalDateTime moveInDate;

	public static CustomerEntity create() {
		return new CustomerEntity();
	}

	public String getCustomerOrgId() {
		return customerOrgId;
	}

	public void setCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
	}

	public CustomerEntity withCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
		return this;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public CustomerEntity withCustomerType(String customerType) {
		this.customerType = customerType;
		return this;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public CustomerEntity withCustomerId(Integer customerId) {
		this.customerId = customerId;
		return this;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public CustomerEntity withOrganizationId(String organizationId) {
		this.organizationId = organizationId;
		return this;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public CustomerEntity withOrganizationName(String organizationName) {
		this.organizationName = organizationName;
		return this;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public CustomerEntity withActive(boolean active) {
		this.active = active;
		return this;
	}

	public LocalDateTime getMoveInDate() {
		return moveInDate;
	}

	public void setMoveInDate(LocalDateTime moveInDate) {
		this.moveInDate = moveInDate;
	}

	public CustomerEntity withMoveInDate(LocalDateTime moveInDate) {
		this.moveInDate = moveInDate;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(active, customerId, customerOrgId, customerType, moveInDate, organizationId, organizationName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof final CustomerEntity other)) {
			return false;
		}
		return active == other.active && Objects.equals(customerId, other.customerId) && Objects.equals(customerOrgId, other.customerOrgId) && Objects.equals(customerType, other.customerType) && Objects.equals(moveInDate, other.moveInDate) && Objects
			.equals(organizationId, other.organizationId) && Objects.equals(organizationName, other.organizationName);
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder();
		builder.append("CustomerEntity [customerId=").append(customerId).append(", organizationId=").append(organizationId).append(", customerOrgId=").append(customerOrgId).append(", customerType=").append(customerType).append(", organizationName=")
			.append(organizationName).append(", active=").append(active).append(", moveInDate=").append(moveInDate).append("]");
		return builder.toString();
	}
}
