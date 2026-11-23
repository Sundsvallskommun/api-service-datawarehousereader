package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecompileStatementInspectorTest {

	@AfterEach
	void tearDown() {
		RecompileContext.disable();
	}

	@Test
	void testInspectorAddsRecompileHintWhenEnabled() {
		// Arrange
		final var inspector = new RecompileStatementInspector();
		RecompileContext.enable(false);
		final var originalSql = "select * from table_name where id = 1";

		// Act
		final var result = inspector.inspect(originalSql);

		// Assert
		assertThat(result).isEqualTo("select * from table_name where id = 1 option (recompile)");
	}

	@Test
	void testInspectorDoesNotAddRecompileHintWhenDisabled() {
		// Arrange
		final var inspector = new RecompileStatementInspector();
		final var originalSql = "select * from table_name where id = 1";

		// Act
		final var result = inspector.inspect(originalSql);

		// Assert
		assertThat(result).isEqualTo(originalSql);
	}

	@Test
	void testInspectorIgnoresNonSelectStatements() {
		// Arrange
		final var inspector = new RecompileStatementInspector();
		RecompileContext.enable(false);
		final var insertSql = "insert into table_name (col) values (1)";
		final var updateSql = "update table_name set col = 1";
		final var deleteSql = "delete from table_name where id = 1";

		// Act & Assert
		assertThat(inspector.inspect(insertSql)).isEqualTo(insertSql);
		assertThat(inspector.inspect(updateSql)).isEqualTo(updateSql);
		assertThat(inspector.inspect(deleteSql)).isEqualTo(deleteSql);
	}

	@Test
	void testInspectorHandlesCaseInsensitiveSelect() {
		// Arrange
		final var inspector = new RecompileStatementInspector();
		RecompileContext.enable(false);
		final var upperCaseSql = "SELECT * FROM table_name";
		final var mixedCaseSql = "Select * From table_name";

		// Act & Assert
		assertThat(inspector.inspect(upperCaseSql)).isEqualTo("SELECT * FROM table_name option (recompile)");
		assertThat(inspector.inspect(mixedCaseSql)).isEqualTo("Select * From table_name option (recompile)");
	}

	@Test
	void testInspectorAddsRecompileAndHashJoinHintWhenBothEnabled() {
		// Arrange
		final var inspector = new RecompileStatementInspector();
		RecompileContext.enable(true);

		// Act
		final var result = inspector.inspect("select * from table_name where id = 1");

		// Assert
		assertThat(result).isEqualTo("select * from table_name where id = 1 option (recompile, hash join)");
	}

	@Test
	void testInspectorLeavesSelectUnchangedWhenRecompileDisabledAfterHashJoinEnabled() {
		// Arrange
		final var inspector = new RecompileStatementInspector();
		RecompileContext.enable(true);
		RecompileContext.disable();
		final var originalSql = "select * from table_name where id = 1";

		// Act
		final var result = inspector.inspect(originalSql);

		// Assert
		assertThat(result).isEqualTo(originalSql);
	}

	@Test
	void testInspectorIgnoresNonSelectStatementsWhenHashJoinEnabled() {
		// Arrange
		final var inspector = new RecompileStatementInspector();
		RecompileContext.enable(true);
		final var insertSql = "insert into table_name (col) values (1)";
		final var updateSql = "update table_name set col = 1";
		final var withSql = "with cte as (select 1 as x) select x from cte";

		// Act & Assert
		assertThat(inspector.inspect(insertSql)).isEqualTo(insertSql);
		assertThat(inspector.inspect(updateSql)).isEqualTo(updateSql);
		assertThat(inspector.inspect(withSql)).isEqualTo(withSql);
	}

	@Test
	void testContextEnableAndDisable() {
		// Arrange & Act & Assert
		assertThat(RecompileContext.isEnabled()).isFalse();

		RecompileContext.enable(false);
		assertThat(RecompileContext.isEnabled()).isTrue();

		RecompileContext.disable();
		assertThat(RecompileContext.isEnabled()).isFalse();
	}
}
