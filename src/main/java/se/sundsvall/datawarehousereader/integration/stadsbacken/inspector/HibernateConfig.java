package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.hibernate.cfg.JdbcSettings.STATEMENT_INSPECTOR;

/**
 * Hibernate configuration to register the {@link RecompileStatementInspector} as a spring bean instead of enabling it
 * via properties.
 */
@Configuration
public class HibernateConfig {

	@Bean
	public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(final RecompileStatementInspector inspector) {
		return properties -> properties.put(STATEMENT_INSPECTOR, inspector);
	}
}
