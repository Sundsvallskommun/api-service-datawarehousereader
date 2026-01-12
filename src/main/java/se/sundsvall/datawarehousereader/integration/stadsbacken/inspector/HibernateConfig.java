package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Hibernate configuration to register the {@link RecompileStatementInspector} as a spring bean instead of enabling it
 * via properties.
 */
@Configuration
public class HibernateConfig {

	@Bean
	public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(RecompileStatementInspector inspector) {
		return properties -> properties.put(
			AvailableSettings.STATEMENT_INSPECTOR, inspector);
	}
}
