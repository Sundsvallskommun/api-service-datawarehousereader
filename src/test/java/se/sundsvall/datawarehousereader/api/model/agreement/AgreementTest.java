package se.sundsvall.datawarehousereader.api.model.agreement;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static se.sundsvall.datawarehousereader.api.model.Category.ELECTRICITY;

import com.google.code.beanmatchers.BeanMatchers;
import java.time.LocalDate;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AgreementTest {

	@BeforeAll
	static void setup() {
		BeanMatchers.registerValueGenerator(() -> LocalDate.now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(Agreement.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var customerNumber = "customerNumber";
		final var partyId = "partyId";
		final var category = ELECTRICITY;
		final var billingId = "billingId";
		final var facilityId = "facilityId";
		final var agreementId = "agreementId";
		final var description = "description";
		final var mainAgreement = false;
		final var binding = true;
		final var bindingRule = "bindingRule";
		final var placementStatus = "placementStatus";
		final var netAreaId = "netAreaId";
		final var siteAddress = "siteAddress";
		final var production = true;
		final var fromDate = LocalDate.now().minusMonths(10L);
		final var toDate = LocalDate.now();
		final var active = Boolean.TRUE;

		final var agreement = Agreement.create()
			.withCustomerNumber(customerNumber)
			.withPartyId(partyId)
			.withCategory(category)
			.withBillingId(billingId)
			.withAgreementId(agreementId)
			.withFacilityId(facilityId)
			.withDescription(description)
			.withMainAgreement(mainAgreement)
			.withBinding(binding)
			.withBindingRule(bindingRule)
			.withPlacementStatus(placementStatus)
			.withNetAreaId(netAreaId)
			.withSiteAddress(siteAddress)
			.withProduction(production)
			.withFromDate(fromDate)
			.withToDate(toDate)
			.withActive(active);

		assertThat(agreement).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(agreement.getCustomerNumber()).isEqualTo(customerNumber);
		assertThat(agreement.getPartyId()).isEqualTo(partyId);
		assertThat(agreement.getCategory()).isEqualTo(category);
		assertThat(agreement.getBillingId()).isEqualTo(billingId);
		assertThat(agreement.getAgreementId()).isEqualTo(agreementId);
		assertThat(agreement.getFacilityId()).isEqualTo(facilityId);
		assertThat(agreement.getDescription()).isEqualTo(description);
		assertThat(agreement.getMainAgreement()).isEqualTo(mainAgreement);
		assertThat(agreement.getBinding()).isEqualTo(binding);
		assertThat(agreement.getBindingRule()).isEqualTo(bindingRule);
		assertThat(agreement.getPlacementStatus()).isEqualTo(placementStatus);
		assertThat(agreement.getNetAreaId()).isEqualTo(netAreaId);
		assertThat(agreement.getSiteAddress()).isEqualTo(siteAddress);
		assertThat(agreement.getProduction()).isEqualTo(production);
		assertThat(agreement.getFromDate()).isEqualTo(fromDate);
		assertThat(agreement.getToDate()).isEqualTo(toDate);
		assertThat(agreement.getActive()).isEqualTo(active);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(Agreement.create()).hasAllNullFieldsOrProperties();
		assertThat(new Agreement()).hasAllNullFieldsOrProperties();
	}
}
