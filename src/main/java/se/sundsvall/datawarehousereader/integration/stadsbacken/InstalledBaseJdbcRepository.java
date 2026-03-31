package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseItem;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseResponse;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

@Repository
@CircuitBreaker(name = "installedBaseJdbcRepository")
public class InstalledBaseJdbcRepository {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public InstalledBaseJdbcRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * @param  pageNumber      the page number to fetch
	 * @param  pageSize        page size
	 * @param  organizationIds comma-separated list of organizationIds
	 * @param  date            date
	 * @param  uuid            customer identifier
	 * @param  sortBy          column to sort by.
	 * @return                 InstalledBaseResponse with items and pagination metadata
	 */
	public InstalledBaseResponse getInstalledBases(final Integer pageNumber, final Integer pageSize,
		final String organizationIds, final LocalDate date, final String uuid, final String sortBy) {

		var parameters = new MapSqlParameterSource()
			.addValue("pageNumber", pageNumber)
			.addValue("pageSize", pageSize)
			.addValue("organizationIds", organizationIds)
			.addValue("sortDate", date)
			.addValue("identifier", uuid)
			.addValue("sortBy", sortBy);

		return jdbcTemplate.query(
			"select * from [kundinfo].[fnInstalledBaseWithPagingAndSort] ( :pageNumber,:pageSize, :organizationIds, :sortDate, :identifier ,:sortBy)",
			parameters,
			new InstalledBaseResponseExtractor(pageNumber, pageSize, sortBy));
	}

	static class InstalledBaseResponseExtractor implements ResultSetExtractor<InstalledBaseResponse> {

		private final int pageNumber;
		private final int pageSize;
		private final String sortBy;

		InstalledBaseResponseExtractor(final int pageNumber, final int pageSize, final String sortBy) {
			this.pageNumber = pageNumber;
			this.pageSize = pageSize;
			this.sortBy = sortBy;
		}

		@Override
		public InstalledBaseResponse extractData(final ResultSet rs) throws SQLException, DataAccessException {
			final List<InstalledBaseItem> items = new ArrayList<>();
			int totalRecords = 0;
			int totalPages = 0;
			int count = 0;

			while (rs.next()) {
				items.add(mapRow(rs));
				if (items.size() == 1) {
					totalRecords = rs.getInt("TotalRecords");
					totalPages = (int) rs.getFloat("TotalPages");
					count = rs.getInt("Count");
				}
			}

			final var metaData = PagingAndSortingMetaData.create()
				.withPage(pageNumber)
				.withLimit(pageSize)
				.withCount(count)
				.withTotalRecords(totalRecords)
				.withTotalPages(totalPages)
				.withSortBy(sortBy != null ? List.of(sortBy) : null);

			return InstalledBaseResponse.create()
				.withMetaData(metaData)
				.withInstalledBase(items);
		}

		private InstalledBaseItem mapRow(final ResultSet rs) throws SQLException {
			return InstalledBaseItem.create()
				.withCompany(rs.getString("Company"))
				.withCustomerNumber(rs.getString("Customerid"))
				.withType(rs.getString("Type"))
				.withFacilityId(rs.getString("FacilityId"))
				.withPlacementId(rs.getInt("internalId"))
				.withCareOf(rs.getString("Careof"))
				.withStreet(rs.getString("Street"))
				.withPostCode(rs.getString("Postcode"))
				.withCity(rs.getString("City"))
				.withPropertyDesignation(rs.getString("HouseName"))
				.withDateFrom(toLocalDate(rs.getDate("DateFrom")))
				.withDateTo(toLocalDate(rs.getDate("DateTo")))
				.withDateLastModified(toLocalDate(rs.getDate("InstalledBaseLastChangedDate")));
		}

		private LocalDate toLocalDate(final java.sql.Date date) {
			return date != null ? date.toLocalDate() : null;
		}
	}

}
