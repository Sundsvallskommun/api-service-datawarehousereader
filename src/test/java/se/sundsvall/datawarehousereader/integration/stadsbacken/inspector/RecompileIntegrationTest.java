package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceDetailRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test to verify that the recompile hint is properly applied to SQL queries.
 *
 * <p>
 * The statements are read off the org.hibernate.SQL logger rather than off the inspector, since Hibernate logs a
 * statement after handing it to the {@link org.hibernate.resource.jdbc.spi.StatementInspector}. What the logger sees is
 * therefore what is prepared against the connection, which is the thing worth asserting on: it holds only if the aspect
 * fired, the inspector is the one the entity manager factory was built with, and the hint survived to the statement.
 * </p>
 */
@SpringBootTest(classes = Application.class, properties = "spring.jpa.properties.hibernate.format_sql=false")
@ActiveProfiles("junit")
class RecompileIntegrationTest {

	private static final String SQL_LOGGER = "org.hibernate.SQL";
	private static final String RECOMPILE_HINT = "option (recompile)";
	private static final String ANNOTATED_TABLE = "vInvoiceDetail";

	@Autowired
	private InvoiceDetailRepository invoiceDetailRepository;

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
	void testQueryOnAnnotatedMethodCarriesTheRecompileHint() {
		// Act
		final var statements = captureStatements(() -> invoiceDetailRepository.findAllByInvoiceNumberIn(List.of(1L, 2L, 3L)));

		// Assert
		assertThat(statements)
			.isNotEmpty()
			.allSatisfy(statement -> assertThat(statement).contains(ANNOTATED_TABLE).endsWith(RECOMPILE_HINT));
	}

	/**
	 * The hint is meant to follow the annotation rather than the connection, so a method without one has to come out
	 * unhinted even though it runs against the same entity and the same session factory.
	 */
	@Test
	void testQueryOnUnannotatedMethodCarriesNoRecompileHint() {
		// Act
		final var statements = captureStatements(() -> invoiceDetailRepository.findById(1));

		// Assert
		assertThat(statements)
			.isNotEmpty()
			.allSatisfy(statement -> assertThat(statement).doesNotContain(RECOMPILE_HINT));
	}

	/**
	 * The padding of an IN-clause is applied while the statement is rendered and the hint is appended to the rendered
	 * statement, so the two do not cancel one another out. Three bound values are padded to four.
	 */
	@Test
	void testInClausePaddingSurvivesTheRecompileHint() {
		// Act
		final var statements = captureStatements(() -> invoiceDetailRepository.findAllByInvoiceNumberIn(List.of(1L, 2L, 3L)));

		// Assert
		assertThat(statements)
			.isNotEmpty()
			.allSatisfy(statement -> assertThat(statement).contains("in (?,?,?,?)").endsWith(RECOMPILE_HINT));
	}

	@Test
	void testInspectorIntegrationWithHibernate() {
		// Verify that the inspector can handle real SQL queries
		final var selectSql = "select 1 as test_value";

		RecompileContext.enable();
		final var inspectedSql = inspector.inspect(selectSql);

		assertThat(inspectedSql).isEqualTo("select 1 as test_value " + RECOMPILE_HINT);
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

	/**
	 * Runs the given query and returns the statements Hibernate prepared while it ran.
	 *
	 * @param  query the query to run
	 * @return       the prepared statements, in the order they were prepared
	 */
	private List<String> captureStatements(final Supplier<Object> query) {
		final var logger = (Logger) LoggerFactory.getLogger(SQL_LOGGER);
		final var appender = new ListAppender<ILoggingEvent>();
		final var originalLevel = logger.getLevel();

		appender.start();
		logger.addAppender(appender);
		logger.setLevel(Level.DEBUG);

		try {
			query.get();
		} finally {
			logger.setLevel(originalLevel);
			logger.detachAppender(appender);
			appender.stop();
		}

		return appender.list.stream()
			.map(ILoggingEvent::getFormattedMessage)
			.map(String::strip)
			.toList();
	}
}
