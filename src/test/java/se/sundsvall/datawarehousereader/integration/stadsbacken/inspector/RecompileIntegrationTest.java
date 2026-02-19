package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.datawarehousereader.api.model.agreement.AgreementParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.AgreementRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * Integration test to verify that the recompile hint is properly applied to SQL queries.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
@Import({
	RecompileStatementInspector.class, HibernateConfig.class, RecompileAspect.class
})
class RecompileIntegrationTest {

	@Autowired
	private AgreementRepository agreementRepository;

	@Autowired
	private RecompileStatementInspector inspector;

	@Autowired
	private DataSource dataSource;

	@AfterEach
	void tearDown() {
		RecompileContext.disable();
	}

	@Test
	void testRecompileStatementInspectorIsRegisteredAsBean() {
		assertThat(inspector).isNotNull();
	}

	@Test
	void testRecompileContextThreadLocal() {
		// Initially disabled
		assertThat(RecompileContext.isEnabled()).isFalse();

		// Enable in current thread
		RecompileContext.enable();
		assertThat(RecompileContext.isEnabled()).isTrue();

		// Verify in a new thread that it's not enabled (ThreadLocal isolation)
		final Thread thread = new Thread(() -> assertThat(RecompileContext.isEnabled()).isFalse());
		thread.start();
		try {
			thread.join();
		} catch (final InterruptedException _) {
			Thread.currentThread().interrupt();
		}

		// Still enabled in current thread
		assertThat(RecompileContext.isEnabled()).isTrue();

		// Disable
		RecompileContext.disable();
		assertThat(RecompileContext.isEnabled()).isFalse();
	}

	@Test
	void testAgreementRepositoryQueryWithRecompileAnnotation() {
		// This test verifies that the @WithRecompile annotation triggers the recompile context
		// The actual SQL cannot be easily intercepted in this test, but we can verify
		// that the query executes successfully with the annotation

		// Execute query with @WithRecompile annotation
		final var result = agreementRepository.findAllByParameters(
			AgreementParameters.create().withAgreementId("1"),
			null,
			PageRequest.of(0, 10));

		// Verify that the query executed without errors
		assertThat(result).isNotNull();
	}

	@Test
	void testInspectorIntegrationWithHibernate() {
		// Verify that the inspector can handle real SQL queries
		final var selectSql = "select 1 as test_value";

		RecompileContext.enable();
		final var inspectedSql = inspector.inspect(selectSql);

		assertThat(inspectedSql).isEqualTo("select 1 as test_value option (recompile)");
	}

	@Test
	void testDatabaseConnectionWorks() throws SQLException {
		// Verify database connectivity
		try (final Connection conn = dataSource.getConnection();
			final var stmt = conn.createStatement();
			final var rs = stmt.executeQuery("SELECT 1")) {
			assertThat(rs.next()).isTrue();
			assertThat(rs.getInt(1)).isEqualTo(1);
		}
	}
}
