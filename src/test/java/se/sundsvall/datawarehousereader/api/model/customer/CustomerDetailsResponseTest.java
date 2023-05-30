package se.sundsvall.datawarehousereader.api.model.customer;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import se.sundsvall.datawarehousereader.api.model.MetaData;

class CustomerDetailsResponseTest {


    @Test
    void testBean() {
        assertThat(CustomerDetailsResponse.class, allOf(
            hasValidBeanConstructor(),
            hasValidGettersAndSetters(),
            hasValidBeanHashCode(),
            hasValidBeanEquals(),
            hasValidBeanToString()));
    }

    @Test
    void testCreatePattern() {
        final var metaData = MetaData.create();
        final var customer = CustomerDetails.create();

        final var response = CustomerDetailsResponse.create()
            .withMetadata(metaData)
            .withCustomerDetails(List.of(customer));

        Assertions.assertThat(response.getMetaData()).isEqualTo(metaData);
        Assertions.assertThat(response.getCustomerDetails()).hasSize(1).contains(customer);
    }

    @Test
    void testNoDirtOnCreatedBean() {
        Assertions.assertThat(CustomerDetailsResponse.create()).hasAllNullFieldsOrProperties();
        Assertions.assertThat(new CustomerDetailsResponse()).hasAllNullFieldsOrProperties();
    }

}