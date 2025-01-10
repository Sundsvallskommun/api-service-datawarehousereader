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
