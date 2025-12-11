drop table if exists kundinfo.vCustomer;
drop table if exists kundinfo.vCustomerDetail;
drop table if exists kundinfo.vInstalledBaseMetadata;
drop table if exists kundinfo.vInstalledBase;
drop table if exists kundinfo.vInstallations;
drop table if exists kundinfo.vInvoiceDetail;
drop table if exists kundinfo.vInvoice_Test_251126;
drop table if exists kundinfo.vMeasurementDistrictHeatingHour;
drop table if exists kundinfo.vMeasurementDistrictHeatingDay;
drop table if exists kundinfo.vMeasurementDistrictHeatingMonth;
drop table if exists kundinfo.vMeasurementElectricityHour;
drop table if exists kundinfo.vMeasurementElectricityDay;
drop table if exists kundinfo.vMeasurementElectricityMonth;
drop table if exists kundinfo.vAgreements;
drop procedure if exists kundinfo.spMeasurementElectricityHour;
drop function if exists kundinfo.fnCustomerDetailWithPagingAndSort;

drop schema if exists kundinfo;
create schema kundinfo;

create procedure kundinfo.spMeasurementElectricityHour(@customerorgid as varchar(8000), @anlaggningsID as varchar(255), @datum_start as datetime2, @datum_stop as datetime2)
	as
begin
    select uuid,
           customerorgid,
           facilityId,
           feedType,
           unit, [usage], DateAndTime
    from vMeasurementElectricityHour
    where (@customerorgid is null
       or @customerorgid = customerorgid)
      and (@anlaggningsID is null
       or @anlaggningsID = facilityId)
      and (@datum_start is null
       or @datum_start <= DateAndTime)
      and (@datum_stop is null
       or @datum_stop >= DateAndTime)
end;
