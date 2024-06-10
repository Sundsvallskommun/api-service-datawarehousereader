-- Not how it works in datalagret but for us to be able to use the function we use the data from the view
create function kundinfo.fnCustomerDetail(@fromDate as datetime2, @orgId as varchar(50), @uuids as varchar(255))
	returns table as 
		return (select * from kundinfo.vCustomerDetail where organizationid = @orgId and (@uuids is null or uuid in (select value from STRING_SPLIT(@uuids, ','))));
