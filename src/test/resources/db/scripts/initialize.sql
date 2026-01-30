drop table if exists kundinfo.vCustomer;
drop table if exists kundinfo.vCustomerDetail;
drop table if exists kundinfo.vInstalledBaseMetadata;
drop table if exists kundinfo.vInstalledBase;
drop table if exists kundinfo.vInstallations;
drop table if exists kundinfo.vInvoiceDetail_Test_251126;
drop table if exists kundinfo.vInvoice_Test_251126;
drop table if exists kundinfo.vMeasurementDistrictHeating;
drop table if exists kundinfo.vMeasurementElectricityHour;
drop table if exists kundinfo.vMeasurementElectricityDay;
drop table if exists kundinfo.vMeasurementElectricityMonth;
drop table if exists kundinfo.vAgreements;
drop procedure if exists kundinfo.spMeasurementElectricityHour;
drop procedure if exists kundinfo.spMeasurementDistrictHeating;
drop function if exists kundinfo.fnCustomerDetailWithPagingAndSort;

drop schema if exists kundinfo;
create schema kundinfo;

create procedure kundinfo.spMeasurementElectricityHour(@customerorgid as varchar(8000), @anlaggningsID as varchar(255),
                                                       @datum_start as datetime2, @datum_stop as datetime2)
as
begin
    select uuid,
           customerorgid,
           facilityId,
           feedType,
           unit,
           [usage],
           DateAndTime
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

    create procedure kundinfo.spMeasurementDistrictHeating(@customerorgid as varchar(8000),
                                                           @anlaggningsID as varchar(255), @datum_start as datetime2,
                                                           @datum_stop as datetime2, @aggregationLevel as varchar(50))
    as
    begin
        if @aggregationLevel = 'HOUR' or @aggregationLevel is null
            begin
                -- Return hourly data as-is
                select uuid,
                       customerorgid,
                       facilityId,
                       feedType,
                       unit,
                       [usage],
                       DateAndTime,
                       isInterpolted
                from vMeasurementDistrictHeating
                where (@customerorgid is null or @customerorgid = customerorgid)
                  and (@anlaggningsID is null or @anlaggningsID = facilityId)
                  and (@datum_start is null or @datum_start <= DateAndTime)
                  and (@datum_stop is null or @datum_stop >= DateAndTime)
            end
        else
            if @aggregationLevel = 'DAY'
                begin
                    -- Aggregate to daily level
                    select min(uuid)                                   as uuid,
                           customerorgid,
                           facilityId,
                           feedType,
                           unit,
                           sum([usage])                                as [usage],
                           cast(cast(DateAndTime as date) as datetime) as DateAndTime,
                           max(cast(isInterpolted as int))             as isInterpolted
                    from vMeasurementDistrictHeating
                    where (@customerorgid is null or @customerorgid = customerorgid)
                      and (@anlaggningsID is null or @anlaggningsID = facilityId)
                      and (@datum_start is null or @datum_start <= DateAndTime)
                      and (@datum_stop is null or @datum_stop >= DateAndTime)
                    group by customerorgid, facilityId, feedType, unit, cast(DateAndTime as date)
                end
            else
                if @aggregationLevel = 'MONTH'
                    begin
                        -- Aggregate to monthly level
                        select min(uuid)                                               as uuid,
                               customerorgid,
                               facilityId,
                               feedType,
                               unit,
                               sum([usage])                                            as [usage],
                               datefromparts(year(DateAndTime), month(DateAndTime), 1) as DateAndTime,
                               max(cast(isInterpolted as int))                         as isInterpolted
                        from vMeasurementDistrictHeating
                        where (@customerorgid is null or @customerorgid = customerorgid)
                          and (@anlaggningsID is null or @anlaggningsID = facilityId)
                          and (@datum_start is null or @datum_start <= DateAndTime)
                          and (@datum_stop is null or @datum_stop >= DateAndTime)
                        group by customerorgid, facilityId, feedType, unit, year(DateAndTime), month(DateAndTime)
                    end
    end;
