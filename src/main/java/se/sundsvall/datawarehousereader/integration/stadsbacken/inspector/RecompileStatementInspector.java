package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@Component
public class RecompileStatementInspector implements StatementInspector {

	private static final String SQL_START = "select";
	private static final String RECOMPILE_HINT = " option (recompile)"; // Leading space is intentional

	@Override
	public String inspect(final String sql) {
		if (RecompileContext.isEnabled() && sql.toLowerCase().startsWith(SQL_START)) {
			return sql + RECOMPILE_HINT;
		}

		return sql;
	}
}
