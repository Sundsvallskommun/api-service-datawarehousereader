-- Not how it works in datalagret but for us to be able to use the function we use the data from the view
create function kundinfo.fnCustomerDetails(@fromDate as datetime2, @orgId as varchar(50))
        returns table
            as
            return(select * from kundinfo.vCustomerDetail where organizationid = @orgId);
