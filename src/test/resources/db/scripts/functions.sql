-- Not how it works in datalagret but for us to be able to use the function we use the data from the view. Also Count and TotalRecords are faked to provide support for all tests
create function kundinfo.fnCustomerDetailWithPagingAndSort(@fromDate as datetime2, @orgId as varchar(50), @uuids as varchar(255), @pageNumber as int, @pageSize as int, @sortBy as varchar(255))
	returns table as 
	return (
		select 
			Active, 
			(select count(*) from kundinfo.vCustomerDetail where organizationid = @orgId and (@uuids is null or uuid in (select value from STRING_SPLIT(@uuids, ',')))) as 'Count',
			CustomerCategoryId, 
			CustomerChangedFlg,
			Customerid, 
			Installedchangedflg,
			TotalPages,
			(select count(*) from kundinfo.vCustomerDetail where organizationid = @orgId and (@uuids is null or uuid in (select value from STRING_SPLIT(@uuids, ',')))) as 'TotalRecords', 
			MoveInDate,
			Address,
			City,
			CustomerCategoryDescription,
			CustomerOrgId,
			Email1,
			Email2,
			Name,
			Phone1,
			Phone2,
			Phone3,
			Zipcode,
			[c/o],
			organizationid,
			organizationname,
			uuid
		from kundinfo.vCustomerDetail where organizationid = @orgId and (@uuids is null or uuid in (select value from STRING_SPLIT(@uuids, ','))));

-- Hardcoded test data for fnInstalledBaseWithPagingAndSort - just verifies the function is called correctly
create function kundinfo.fnInstalledBaseWithPagingAndSort(@pageNumber as int, @pageSize as int, @organizationIds as varchar(255), @date as datetime2, @uuid as varchar(255), @sortBy as varchar(255))
	returns table as
	return (
		select * from (values
			(cast('898B3634-A2F9-483C-8808-37F3F25CF24E' as uniqueidentifier), 1, 'Company A', '1234567890', 99001, '0987654321', 100001, '100000000000000001', 'Type A', 0, 'Street 1', '11111', 'CITY A', 'Care Of A', cast('2024-01-01' as datetime), cast(null as datetime), 'HOUSE 1', cast('2024-06-01' as datetime), cast('2020-01-01' as datetime), cast('2024-12-01' as datetime), 1, 'B', 1, 2, 1, 2),
			(cast('898B3634-A2F9-483C-8808-37F3F25CF24E' as uniqueidentifier), 2, 'Company B', '1234567891', 99001, '0987654321', 100002, '100000000000000002', 'Type B', 0, 'Street 2', '22222', 'CITY B', 'Care Of B', cast('2024-02-01' as datetime), cast(null as datetime), 'HOUSE 2', cast('2024-07-01' as datetime), cast('2020-02-01' as datetime), cast('2024-12-02' as datetime), 1, 'B', 2, 2, 1, 2)
		) as t(uuid, BillLocationID, Company, Organizationid, Customerid, customerorgid, internalId, FacilityId, [Type], IsProduktion, Street, Postcode, City, Careof, DateFrom, DateTo, HouseName, LastChangedSiteDate, LastChangedPlacementDate, InstalledBaseLastChangedDate, SiteStatus, [Source], RowNum, TotalRecords, TotalPages, [Count])
		where t.uuid = cast(@uuid as uniqueidentifier));

CREATE OR ALTER FUNCTION kundinfo.fnInvoiceNumberWithPagingAndSort(
      @PageNumber      int,
      @PageSize        int,
      @organizationid  varchar(max),
      @CustomerId      varchar(50),
      @periodFrom      date,
      @periodTo        date,
      @SortBy          nvarchar(max))
  RETURNS TABLE
  AS
  RETURN
      WITH filtered AS (
          SELECT
              v.CustomerId, v.CustomerType, v.DueDate, v.FacilityId,
              v.OrganizationGroup, v.Administration, v.OrganizationId,
              v.TotalAmount, v.AmountVatIncluded, v.AmountVatExcluded,
              v.VatEligiblaAmount, v.Rounding, v.InvoiceDate, v.InvoiceNumber,
              v.InvoiceStatus, v.OcrNumber, v.InvoiceName, v.InvoiceType,
              v.InvoiceDescription, v.Street, v.PostalCode, v.City, v.CareOf,
              CAST(NULL AS nvarchar(255)) AS InvoiceReference,
              CAST(v.InvoiceDate AS date) AS periodFrom,           -- fake
              CAST(DATEADD(day, 30, v.InvoiceDate) AS date) AS periodTo,  -- fake
              v.pdfAvailable,
              CAST(-1 AS bigint) AS JointInvoiceid,                -- fake
              CAST(v.InvoiceNumber AS bigint) AS InvoiceID         -- fake
          FROM kundinfo.vInvoice_Test_251126 v
          WHERE
              (@CustomerId   IS NULL OR CAST(v.CustomerId AS varchar(50)) = @CustomerId)
              AND (@periodFrom IS NULL OR v.InvoiceDate >= @periodFrom)
              AND (@periodTo   IS NULL OR v.InvoiceDate <= @periodTo)
              AND (@organizationid IS NULL
                   OR v.OrganizationId IN (SELECT value FROM STRING_SPLIT(@organizationid, ',')))
      ),
      numbered AS (
          SELECT *,
                 ROW_NUMBER() OVER (ORDER BY
                     CASE WHEN @SortBy = 'periodFrom'    THEN periodFrom END,
                     CASE WHEN @SortBy = 'periodTo'      THEN periodTo END,
                     CASE WHEN @SortBy = 'DueDate'       THEN DueDate END,
                     CASE WHEN @SortBy = 'InvoiceDate'   THEN InvoiceDate END,
                     CASE WHEN @SortBy = 'InvoiceNumber' THEN InvoiceNumber END,
                     InvoiceNumber) AS RowNum,
                 COUNT(*) OVER () AS TotalRecords
          FROM filtered
      )
SELECT
    CustomerId, CustomerType, DueDate, FacilityId, OrganizationGroup,
    Administration, OrganizationId, TotalAmount, AmountVatIncluded,
    AmountVatExcluded, VatEligiblaAmount, Rounding, InvoiceDate,
    InvoiceNumber, InvoiceStatus, OcrNumber, InvoiceName, InvoiceType,
    InvoiceDescription, Street, PostalCode, City, CareOf, InvoiceReference,
    periodFrom, periodTo, pdfAvailable, JointInvoiceid, InvoiceID,
    RowNum,
    TotalRecords,
    CAST(CEILING(TotalRecords * 1.0 / @PageSize) AS int) AS TotalPages,
    CAST(@PageSize AS int) AS Count  -- replace, see note
FROM numbered
WHERE RowNum BETWEEN ((@PageNumber - 1) * @PageSize + 1) AND (@PageNumber * @PageSize);